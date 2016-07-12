package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.api.dtos.messages.ReceiverData;
import com.pgizka.gsenger.provider.Receiver;

import javax.inject.Inject;

import io.realm.Realm;

public class MessageStateChangedCommand extends GCMCommand {

    ReceiverData receiverData;

    @Inject
    Gson gson;

    public MessageStateChangedCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        receiverData = gson.fromJson(extraData, ReceiverData.class);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Receiver receiver = realm.where(Receiver.class)
                .equalTo("user.serverId", receiverData.getReceiverId())
                .equalTo("message.serverId", receiverData.getMessageId())
                .findFirst();

        if (receiver != null) {
            receiver.setDelivered(receiverData.getDeliveredDate());
            receiver.setViewed(receiverData.getViewedDate());
        }

        realm.commitTransaction();
    }

}
