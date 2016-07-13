package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GcmCommand;
import com.pgizka.gsenger.api.dtos.messages.MediaMessageData;
import com.pgizka.gsenger.jobqueue.getMediaMessageData.GetMediaMessageDataJob;
import com.pgizka.gsenger.jobqueue.setMessageState.UpdateMessageStateJob;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;

import javax.inject.Inject;

import io.realm.Realm;

public class NewMediaMessageCommand implements GcmCommand {

    private MediaMessageData messageData;

    @Inject
    JobManager jobManager;

    @Inject
    MessageRepository messageRepository;

    @Inject
    Gson gson;

    public NewMediaMessageCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        messageData = gson.fromJson(extraData, MediaMessageData.class);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Message message = messageRepository.handleIncomingMediaMessage(messageData);
        realm.commitTransaction();

        jobManager.addJob(new UpdateMessageStateJob(message.getId()));
        jobManager.addJob(new GetMediaMessageDataJob(message.getId()));
    }


}
