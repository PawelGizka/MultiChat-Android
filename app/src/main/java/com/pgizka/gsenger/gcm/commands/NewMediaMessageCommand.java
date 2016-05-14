package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.data.NewMediaMessageData;
import com.pgizka.gsenger.jobqueue.getMediaMessageData.GetMediaMessageDataJob;
import com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob.Type.SET_DELIVERED;

public class NewMediaMessageCommand extends GCMCommand {

    private NewMediaMessageData messageData;

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
        messageData = gson.fromJson(extraData, NewMediaMessageData.class);

        Realm realm = Realm.getDefaultInstance();
        realm.refresh();
        realm.beginTransaction();

        Message message = messageRepository.handleIncomingMessage(messageData);

        message.setState(Message.State.WAITING_TO_DOWNLOAD.code);

        MediaMessage mediaMessage = new MediaMessage();
        mediaMessage.setFileName(messageData.getFileName());
        mediaMessage.setDescription(messageData.getDescription());
        mediaMessage.setMediaType(messageData.getType());
        mediaMessage = realm.copyToRealm(mediaMessage);

        message.setType(Message.Type.MEDIA_MESSAGE.code);
        message.setMediaMessage(mediaMessage);

        realm.commitTransaction();
        realm.refresh();

        jobManager.addJob(new SetMessageStateJob(message.getId(), SET_DELIVERED));
        jobManager.addJob(new GetMediaMessageDataJob(message.getId()));
    }


}
