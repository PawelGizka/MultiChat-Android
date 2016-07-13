package com.pgizka.gsenger.provider;


import com.pgizka.gsenger.api.dtos.messages.MediaMessageData;
import com.pgizka.gsenger.api.dtos.messages.MessageData;
import com.pgizka.gsenger.api.dtos.messages.ReceiverData;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;
import com.pgizka.gsenger.util.UserAccountManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageRepository {

    private Repository repository;
    private ChatRepository chatRepository;
    private UserRepository userRepository;
    private UserAccountManager userAccountManager;

    public MessageRepository(Repository repository, ChatRepository chatRepository,
                             UserRepository userRepository, UserAccountManager userAccountManager) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userAccountManager = userAccountManager;
        this.chatRepository = chatRepository;
    }

    public Message createOutgoingTextMessageWithReceivers(Chat chat, String text) {
        Realm realm = Realm.getDefaultInstance();

        Message message = createOutgoingMessageWithReceivers(chat);

        TextMessage textMessage = new TextMessage();
        textMessage.setText(text);
        textMessage = realm.copyToRealm(textMessage);

        message.setType(Message.Type.TEXT_MESSAGE.code);
        message.setTextMessage(textMessage);

        return message;
    }

    public Message createOutgoingMediaMessageWithReceivers(Chat chat, int type, String fileName, String path, String description) {
        Realm realm = Realm.getDefaultInstance();

        Message message = createOutgoingMessageWithReceivers(chat);

        MediaMessage mediaMessage = new MediaMessage();
        mediaMessage.setMediaType(type);
        mediaMessage.setFileName(fileName);
        mediaMessage.setPath(path);
        mediaMessage.setDescription(description);
        mediaMessage = realm.copyToRealm(mediaMessage);

        message.setType(Message.Type.MEDIA_MESSAGE.code);
        message.setMediaMessage(mediaMessage);

        return message;
    }


    public Message createOutgoingMessageWithReceivers(Chat chat) {
        Realm realm = Realm.getDefaultInstance();

        Message message = new Message();
        message.setId(repository.getMessageNextId());
        message.setSendDate(System.currentTimeMillis());
        message.setState(Message.State.WAITING_TO_SEND.code);
        message = realm.copyToRealm(message);

        User owner = userAccountManager.getOwner();
        message.setSender(owner);
        owner.getSentMessages().add(message);
        message.setChat(chat);
        chat.getMessages().add(message);

        List<User> participants = chat.getUsers();
        RealmList<Receiver> receivers = new RealmList<>();
        for (User participant : participants) {
            boolean isOwner = participant.getId() == 0;
            if (isOwner) {
                continue;
            }

            Receiver receiver = new Receiver();
            receiver = realm.copyToRealm(receiver);
            receiver.setMessage(message);
            receiver.setUser(participant);
            participant.getReceivers().add(receiver);

            receivers.add(receiver);
        }

        message.setReceivers(receivers);

        return message;
    }

    public Message handleIncomingTextMessage(TextMessageData textMessageData) {
        Realm realm = Realm.getDefaultInstance();
        Message message = handleIncomingMessage(textMessageData);

        TextMessage textMessage = new TextMessage();
        textMessage.setText(textMessageData.getText());
        textMessage = realm.copyToRealm(textMessage);

        message.setType(Message.Type.TEXT_MESSAGE.code);
        message.setTextMessage(textMessage);

        return message;
    }

    public Message handleIncomingMediaMessage(MediaMessageData mediaMessageData) {
        Realm realm = Realm.getDefaultInstance();
        Message message = handleIncomingMessage(mediaMessageData);

        message.setState(Message.State.WAITING_TO_DOWNLOAD.code);

        MediaMessage mediaMessage = new MediaMessage();
        mediaMessage.setFileName(mediaMessageData.getFileName());
        mediaMessage.setDescription(mediaMessageData.getDescription());
        mediaMessage.setMediaType(mediaMessageData.getType());
        mediaMessage = realm.copyToRealm(mediaMessage);

        message.setType(Message.Type.MEDIA_MESSAGE.code);
        message.setMediaMessage(mediaMessage);

        return message;
    }

    public Message handleIncomingMessage(MessageData messageData) {
        Realm realm = Realm.getDefaultInstance();
        User sender = userRepository.getOrCreateLocalUser(messageData.getSender());

        Chat chat;
        boolean singleConversation = messageData.getChatId() == -1;
        if (singleConversation) {
            chat = chatRepository.getOrCreateSingleConversationChatWith(sender);
        } else {
            chat = realm.where(Chat.class).equalTo("serverId", messageData.getChatId()).findFirst();
        }

        Message message = createIncomingMessageWithReceivers(messageData, sender, chat);
        setMessagesDelivered(chat.getId());

        return message;
    }

    public Message createIncomingMessageWithReceivers(MessageData messageData, User sender, Chat chat) {
        Realm realm = Realm.getDefaultInstance();

        Message message = new Message();
        message.setId(repository.getMessageNextId());
        message.setServerId(messageData.getMessageId());
        message.setSendDate(messageData.getSendDate());
        message.setState(Message.State.RECEIVED.code);
        message = realm.copyToRealm(message);

        message.setSender(sender);
        sender.getSentMessages().add(message);
        message.setChat(chat);
        chat.getMessages().add(message);

        List<ReceiverData> receiversData = messageData.getReceiversData();
        boolean isMessageWithReceiversData = receiversData != null & !receiversData.isEmpty();
        if (isMessageWithReceiversData) {
            insertExistingReceivers(receiversData, message);
        } else {
            createReceivers(message, sender, chat);
        }

        return message;
    }

    private void insertExistingReceivers(List<ReceiverData> receiversData, Message message) {
        Realm realm = Realm.getDefaultInstance();

        for (ReceiverData receiverData : receiversData) {
            Receiver receiver = new Receiver();

            User user = realm.where(User.class).equalTo("serverId", receiverData.getReceiverId()).findFirst();

            receiver.setUser(user);
            receiver.setMessage(message);
            receiver.setDelivered(receiverData.getDeliveredDate());
            receiver.setViewed(receiverData.getViewedDate());

            user.getReceivers().add(receiver);
            message.getReceivers().add(receiver);
        }
    }

    private void createReceivers(Message message, User sender, Chat chat) {
        Realm realm = Realm.getDefaultInstance();

        List<User> participants = chat.getUsers();
        for (User participant : participants) {
            if (participant.getId() == sender.getId()) {
                continue;
            }
            Receiver receiver = new Receiver();
            receiver = realm.copyToRealm(receiver);

            receiver.setMessage(message);
            receiver.setUser(participant);
            message.getReceivers().add(receiver);
            participant.getReceivers().add(receiver);
        }
    }

    public void updateMessagesState(ReceiverData receiverData) {
        Realm realm = Realm.getDefaultInstance();

        for (int messageId : receiverData.getMessagesIds()) {
            Receiver receiver = realm.where(Receiver.class)
                    .equalTo("user.serverId", receiverData.getReceiverId())
                    .equalTo("message.serverId", messageId)
                    .findFirst();

            if (receiver != null) {
                receiver.setDelivered(receiverData.getDeliveredDate());
                receiver.setViewed(receiverData.getViewedDate());
            }
        }
    }

    public List<Message> setMessagesViewed(int chatId) {
        Realm realm = Realm.getDefaultInstance();

        List<Receiver> receivers = realm.where(Receiver.class)
                .equalTo("message.chat.id", chatId)
                .equalTo("user.id", userAccountManager.getOwner().getId())
                .equalTo("viewed", 0)
                .findAll();

        List<Message> messages = new ArrayList<>(receivers.size());

        long viewedTime = System.currentTimeMillis();
        for (Receiver receiver : receivers) {
            receiver.setViewed(viewedTime);
            messages.add(receiver.getMessage());
        }

        return messages;
    }

    public List<Message> setMessagesDelivered(int chatId) {
        Realm realm = Realm.getDefaultInstance();

        List<Receiver> receivers = realm.where(Receiver.class)
                .equalTo("message.chat.id", chatId)
                .equalTo("user.id", userAccountManager.getOwner().getId())
                .equalTo("delivered", 0)
                .findAll();

        List<Message> messages = new ArrayList<>(receivers.size());

        long deliverTime = System.currentTimeMillis();
        for (Receiver receiver : receivers) {
            receiver.setDelivered(deliverTime);
            messages.add(receiver.getMessage());
        }

        return messages;
    }


}
