package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.data.NewChatData;
import com.pgizka.gsenger.provider.ChatRepository;

import javax.inject.Inject;

import io.realm.Realm;

public class NewGroupChatCommand extends GCMCommand {

    private NewChatData newChatData;

    @Inject
    ChatRepository chatRepository;

    @Inject
    Gson gson;

    public NewGroupChatCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        newChatData = gson.fromJson(extraData, NewChatData.class);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        chatRepository.createGroupChatFrom(newChatData);
        realm.commitTransaction();
    }
}
