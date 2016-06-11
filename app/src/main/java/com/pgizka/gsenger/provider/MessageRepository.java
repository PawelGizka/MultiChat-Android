package com.pgizka.gsenger.provider;


import com.pgizka.gsenger.gcm.data.NewMessageData;
import com.pgizka.gsenger.util.UserAccountManager;

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

    public Message handleIncomingMessage(NewMessageData messageData) {
        Realm realm = Realm.getDefaultInstance();
        User sender = userRepository.getOrCreateLocalUser(messageData.getSender());

        Chat chat;
        boolean singleConversation = messageData.getChatId() == -1;
        if (singleConversation) {
            chat = chatRepository.getOrCreateSingleConversationChatWith(sender);
        } else {
            chat = realm.where(Chat.class)
                    .equalTo("serverId", messageData.getChatId())
                    .findFirst();
        }

        Message message = createIncomingMessageWithReceivers(messageData, sender, chat);
        return message;
    }

    public Message createIncomingMessageWithReceivers(NewMessageData messageData, User sender, Chat chat) {
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

        List<User> participants = chat.getUsers();
        for (User participant : participants) {
            if (participant.getId() == sender.getId()) {
                continue;
            }
            Receiver receiver = new Receiver();

            boolean isOwner = participant.getId() == 0;
            if (isOwner) {
                receiver.setDelivered(System.currentTimeMillis());
            }

            receiver = realm.copyToRealm(receiver);

            receiver.setMessage(message);
            message.getReceivers().add(receiver);
            receiver.setUser(participant);
            participant.getReceivers().add(receiver);
        }

        return message;
    }

}
