package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.data.NewTextMessageData;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.Receiver;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;

public class NewTextMessageCommand extends GCMCommand {

    private NewTextMessageData messageData;

    @Inject
    Repository repository;

    @Inject
    UserAccountManager userAccountManager;

    @Override
    public void execute(Context context, String action, String extraData) {
        GSengerApplication.getApplicationComponent().inject(this);

        try {
            messageData = new Gson().getAdapter(NewTextMessageData.class).fromJson(extraData);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        //TODO handle case when sender will not be in contacts
        User sender = realm.where(User.class)
                .equalTo("serverId", messageData.getSenderId())
                .findFirst();

        List<User> receiverUsers = new RealmList<>();
        Chat chat = null;

        boolean singleConversation = messageData.getChatId() == -1;
        if (singleConversation) {
            User owner = userAccountManager.getOwner();
            receiverUsers.add(owner);

            chat = realm.where(Chat.class)
                    .equalTo("users.id", sender.getId())
                    .equalTo("users.id", owner.getId())
                    .equalTo("type", Chat.Type.SINGLE_CONVERSATION.code)
                    .findFirst();

            if (chat == null) {
                chat = new Chat();
                chat.setId(repository.getChatNextId());
                chat.setType(Chat.Type.SINGLE_CONVERSATION.code);
                chat.setStartedDate(System.currentTimeMillis());
                chat = realm.copyToRealm(chat);
                chat.getUsers().add(sender);
                chat.getUsers().add(owner);
                sender.getChats().add(chat);
                owner.getChats().add(chat);
            }
        } else {
            //TODO handle group conversation
        }

        TextMessage textMessage = new TextMessage();
        textMessage.setText(messageData.getText());
        textMessage = realm.copyToRealm(textMessage);

        Message message = new Message();
        message.setId(repository.getMessageNextId());
        message.setState(messageData.getMessageId());
        message.setSendDate(messageData.getSendDate());
        message.setState(Message.State.RECEIVED.code);
        message = realm.copyToRealm(message);

        message.setSender(sender);
        message.setChat(chat);
        message.setType(Message.Type.TEXT_MESSAGE.code);
        message.setTextMessage(textMessage);
        chat.getMessages().add(message);

        for (User user : receiverUsers) {
            Receiver receiver = new Receiver();
            receiver.setDelivered(System.currentTimeMillis());
            receiver = realm.copyToRealm(receiver);
            receiver.setMessage(message);
            receiver.setUser(user);
            message.getReceivers().add(receiver);
        }

        realm.commitTransaction();

        //TODO add message delivered job
    }

}
