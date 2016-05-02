package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.data.NewChatData;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;

public class NewGroupChatCommand extends GCMCommand {

    private NewChatData newChatData;

    @Inject
    ChatRepository chatRepository;

    @Override
    public void execute(Context context, String action, String extraData) {
        GSengerApplication.getApplicationComponent().inject(this);

        try {
            newChatData = new Gson().getAdapter(NewChatData.class).fromJson(extraData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Realm realm = Realm.getDefaultInstance();
        realm.refresh();

        realm.beginTransaction();
        chatRepository.createGroupChatFrom(newChatData);
        realm.commitTransaction();
    }
}
