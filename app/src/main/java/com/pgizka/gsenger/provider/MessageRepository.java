package com.pgizka.gsenger.provider;


import com.google.gson.Gson;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.data.NewMessageData;
import com.pgizka.gsenger.gcm.data.NewTextMessageData;
import com.pgizka.gsenger.util.UserAccountManager;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageRepository {

    private Repository repository;
    private ChatRepository chatRepository;
    private UserAccountManager userAccountManager;

    public MessageRepository(Repository repository, ChatRepository chatRepository, UserAccountManager userAccountManager) {
        this.repository = repository;
        this.userAccountManager = userAccountManager;
        this.chatRepository = chatRepository;
    }

    public Message createOutgoingMessageWithReceiver(Chat chat, User friend) {
        Realm realm = Realm.getDefaultInstance();

        Message message = new Message();
        message.setId(repository.getMessageNextId());
        message.setSendDate(System.currentTimeMillis());
        message.setState(Message.State.WAITING_TO_SEND.code);
        message = realm.copyToRealm(message);

        message.setSender(userAccountManager.getOwner());
        message.setChat(chat);
        chat.getMessages().add(message);

        Receiver receiver = new Receiver();
        receiver = realm.copyToRealm(receiver);
        receiver.setMessage(message);
        receiver.setUser(friend);

        message.setReceivers(new RealmList<>(receiver));

        return message;
    }

    public Message handleIncomingMessage(String extraData) {
        NewMessageData messageData;
        try {
            messageData = new Gson().getAdapter(NewMessageData.class).fromJson(extraData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Realm realm = Realm.getDefaultInstance();

        User sender = realm.where(User.class)
                .equalTo("serverId", messageData.getSender().getServerId())
                .findFirst();

        boolean senderExists = sender != null;
        if (!senderExists) {
            sender = messageData.getSender();
            sender.setId(repository.getUserNextId());
            sender.setInContacts(false);
            sender = realm.copyToRealm(sender);
        }

        Chat chat = null;

        boolean singleConversation = messageData.getChatId() == -1;
        if (singleConversation) {
            chat = chatRepository.getOrCreateSingleConversationChatWith(sender);
        } else {
            //TODO handle group conversation
        }

        Message message = createIncomingMessageWithReceiver(messageData, sender, chat);
        chat.getMessages().add(message);
        return message;
    }

    public Message createIncomingMessageWithReceiver(NewMessageData messageData, User sender, Chat chat) {
        Realm realm = Realm.getDefaultInstance();

        Message message = new Message();
        message.setId(repository.getMessageNextId());
        message.setServerId(messageData.getMessageId());
        message.setSendDate(messageData.getSendDate());
        message.setState(Message.State.RECEIVED.code);
        message = realm.copyToRealm(message);

        message.setSender(sender);
        message.setChat(chat);

        Receiver receiver = new Receiver();
        receiver.setDelivered(System.currentTimeMillis());
        receiver = realm.copyToRealm(receiver);
        receiver.setMessage(message);
        receiver.setUser(userAccountManager.getOwner());
        message.getReceivers().add(receiver);

        return message;
    }

}
