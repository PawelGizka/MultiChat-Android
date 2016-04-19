package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.data.NewMediaMessageData;
import com.pgizka.gsenger.gcm.data.NewMessageData;
import com.pgizka.gsenger.gcm.data.NewTextMessageData;
import com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.TextMessage;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob.Type.SET_DELIVERED;

public class NewMediaMessageCommand extends GCMCommand {

    private NewMediaMessageData messageData;

    @Inject
    JobManager jobManager;

    @Inject
    MessageRepository messageRepository;


    @Override
    public void execute(Context context, String action, String extraData) {
        GSengerApplication.getApplicationComponent().inject(this);

        try {
            messageData = new Gson().getAdapter(NewMediaMessageData.class).fromJson(extraData);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.refresh();
        realm.beginTransaction();

        Message message = messageRepository.handleIncomingMessage(extraData);

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
    }


}
