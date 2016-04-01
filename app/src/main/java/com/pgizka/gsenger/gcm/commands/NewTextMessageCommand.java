package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.data.NewTextMessageData;
import com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.Receiver;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;

import static com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob.Type.SET_DELIVERED;

public class NewTextMessageCommand extends GCMCommand {

    private NewTextMessageData messageData;

    @Inject
    Repository repository;

    @Inject
    UserAccountManager userAccountManager;

    @Inject
    JobManager jobManager;

    @Inject
    MessageRepository messageRepository;

    @Inject
    ChatRepository chatRepository;

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
        realm.refresh();
        realm.beginTransaction();

        //TODO handle case when sender will not be in contacts
        User sender = realm.where(User.class)
                .equalTo("serverId", messageData.getSenderId())
                .findFirst();

        Chat chat = null;

        boolean singleConversation = messageData.getChatId() == -1;
        if (singleConversation) {
            chat = chatRepository.getOrCreateSingleConversationChatWith(sender);
        } else {
            //TODO handle group conversation
        }

        TextMessage textMessage = new TextMessage();
        textMessage.setText(messageData.getText());
        textMessage = realm.copyToRealm(textMessage);

        Message message = messageRepository.createIncomingMessageWithReceiver(messageData, sender, chat);
        message.setType(Message.Type.TEXT_MESSAGE.code);
        message.setTextMessage(textMessage);
        chat.getMessages().add(message);

        realm.commitTransaction();
        realm.refresh();

        jobManager.addJob(new SetMessageStateJob(message.getId(), SET_DELIVERED));
    }

}
