package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;
import com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.TextMessage;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob.Type.SET_DELIVERED;

public class NewTextMessageCommand extends GCMCommand {

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

        jobManager.addJob(new SetMessageStateJob(message.getId(), SET_DELIVERED));
    }

}
