package com.pgizka.gsenger.provider;


import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.data.NewMessageData;
import com.pgizka.gsenger.util.UserAccountManager;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageRepository {

    private Repository repository;
    private UserAccountManager userAccountManager;

    public MessageRepository(Repository repository, UserAccountManager userAccountManager) {
        this.repository = repository;
        this.userAccountManager = userAccountManager;
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
