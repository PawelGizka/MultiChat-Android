package com.pgizka.gsenger.provider;


import com.pgizka.gsenger.api.dtos.messages.MediaMessageData;
import com.pgizka.gsenger.api.dtos.messages.MessageData;
import com.pgizka.gsenger.api.dtos.messages.ReceiverInfoData;
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
        RealmList<ReceiverInfo> receiverInfos = new RealmList<>();
        for (User participant : participants) {
            boolean isOwner = participant.getId() == 0;
            if (isOwner) {
                continue;
            }

            ReceiverInfo receiverInfo = new ReceiverInfo();
            receiverInfo = realm.copyToRealm(receiverInfo);
            receiverInfo.setMessage(message);
            receiverInfo.setUser(participant);
            participant.getReceiverInfos().add(receiverInfo);

            receiverInfos.add(receiverInfo);
        }

        message.setReceiverInfos(receiverInfos);

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

        List<ReceiverInfoData> receiversData = messageData.getReceiversData();
        boolean isMessageWithReceiversData = receiversData != null && !receiversData.isEmpty();
        if (isMessageWithReceiversData) {
            insertExistingReceivers(receiversData, message);
        } else {
            createReceivers(message, sender, chat);
        }

        return message;
    }

    private void insertExistingReceivers(List<ReceiverInfoData> receiversData, Message message) {
        Realm realm = Realm.getDefaultInstance();

        for (ReceiverInfoData receiverInfoData : receiversData) {
            ReceiverInfo receiverInfo = new ReceiverInfo();

            User user = realm.where(User.class).equalTo("serverId", receiverInfoData.getReceiverId()).findFirst();

            receiverInfo.setUser(user);
            receiverInfo.setMessage(message);
            receiverInfo.setDelivered(receiverInfoData.getDeliveredDate());
            receiverInfo.setViewed(receiverInfoData.getViewedDate());

            user.getReceiverInfos().add(receiverInfo);
            message.getReceiverInfos().add(receiverInfo);
        }
    }

    private void createReceivers(Message message, User sender, Chat chat) {
        Realm realm = Realm.getDefaultInstance();

        List<User> participants = chat.getUsers();
        for (User participant : participants) {
            if (participant.getId() == sender.getId()) {
                continue;
            }
            ReceiverInfo receiverInfo = new ReceiverInfo();
            receiverInfo = realm.copyToRealm(receiverInfo);

            receiverInfo.setMessage(message);
            receiverInfo.setUser(participant);
            message.getReceiverInfos().add(receiverInfo);
            participant.getReceiverInfos().add(receiverInfo);
        }
    }

    public void updateMessagesState(ReceiverInfoData receiverInfoData) {
        Realm realm = Realm.getDefaultInstance();

        for (int messageId : receiverInfoData.getMessagesIds()) {
            ReceiverInfo receiverInfo = realm.where(ReceiverInfo.class)
                    .equalTo("user.serverId", receiverInfoData.getReceiverId())
                    .equalTo("message.serverId", messageId)
                    .findFirst();

            if (receiverInfo != null) {
                receiverInfo.setDelivered(receiverInfoData.getDeliveredDate());
                receiverInfo.setViewed(receiverInfoData.getViewedDate());
            }
        }
    }

    public List<Message> setMessagesViewed(int chatId) {
        return setMessagesState("viewed", chatId);
    }

    public List<Message> setMessagesDelivered(int chatId) {
        return setMessagesState("delivered", chatId);
    }

    public List<Message> setMessagesState(String method, int chatId) {
        Realm realm = Realm.getDefaultInstance();

        List<ReceiverInfo> receiverInfos = realm.where(ReceiverInfo.class)
                .equalTo("message.chat.id", chatId)
                .equalTo("user.id", userAccountManager.getOwner().getId())
                .equalTo(method, 0)
                .findAll();

        List<Message> messages = new ArrayList<>(receiverInfos.size());

        long updateTime = System.currentTimeMillis();
        for (ReceiverInfo receiverInfo : receiverInfos) {

            if (method.equals("delivered")) {
                receiverInfo.setDelivered(updateTime);
            } else if (method.equals("viewed")) {
                receiverInfo.setViewed(updateTime);
            }

            messages.add(receiverInfo.getMessage());
        }

        return messages;
    }

}
