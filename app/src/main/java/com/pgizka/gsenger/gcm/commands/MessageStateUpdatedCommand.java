package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GcmCommand;
import com.pgizka.gsenger.api.dtos.messages.ReceiverInfoData;
import com.pgizka.gsenger.provider.MessageRepository;

import javax.inject.Inject;

import io.realm.Realm;

public class MessageStateUpdatedCommand implements GcmCommand {

    ReceiverInfoData receiverInfoData;

    @Inject
    Gson gson;

    @Inject
    MessageRepository messageRepository;

    public MessageStateUpdatedCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        receiverInfoData = gson.fromJson(extraData, ReceiverInfoData.class);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        messageRepository.updateMessagesState(receiverInfoData);
        realm.commitTransaction();
    }

}
