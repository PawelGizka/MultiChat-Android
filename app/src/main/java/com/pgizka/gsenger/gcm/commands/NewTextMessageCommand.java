package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GcmCommand;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;
import com.pgizka.gsenger.jobqueue.setMessageState.UpdateMessageStateJob;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;

import javax.inject.Inject;

import io.realm.Realm;


public class NewTextMessageCommand implements GcmCommand {

    private TextMessageData messageData;

    @Inject
    JobManager jobManager;

    @Inject
    MessageRepository messageRepository;

    @Inject
    Gson gson;

    public NewTextMessageCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        messageData = gson.fromJson(extraData, TextMessageData.class);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Message message = messageRepository.handleIncomingTextMessage(messageData);
        realm.commitTransaction();

        jobManager.addJob(new UpdateMessageStateJob(message.getId()));
    }

}
