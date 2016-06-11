package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.data.MessageStateChangedData;
import com.pgizka.gsenger.provider.Receiver;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.gcm.data.MessageStateChangedData.MESSAGE_DELIVERED_ACTION;
import static com.pgizka.gsenger.gcm.data.MessageStateChangedData.MESSAGE_VIEWED_ACTION;

public class MessageStateChangedCommand extends GCMCommand {

    MessageStateChangedData messageStateChangedData;

    @Inject
    Gson gson;

    public MessageStateChangedCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        messageStateChangedData = gson.fromJson(extraData, MessageStateChangedData.class);

        Realm realm = Realm.getDefaultInstance();

        Receiver receiver = realm.where(Receiver.class)
                .equalTo("user.serverId", messageStateChangedData.getReceiverId())
                .equalTo("message.serverId", messageStateChangedData.getMessageId())
                .findFirst();

        realm.beginTransaction();

        if (action.equals(MESSAGE_DELIVERED_ACTION)) {
            receiver.setDelivered(messageStateChangedData.getDate());
        } else if (action.equals(MESSAGE_VIEWED_ACTION)) {
            receiver.setViewed(messageStateChangedData.getDate());
        }

        realm.commitTransaction();
    }

}
