package com.pgizka.gsenger.gcm.commands;


import android.content.Context;

import com.google.gson.Gson;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GcmCommand;
import com.pgizka.gsenger.api.dtos.chats.ChatData;
import com.pgizka.gsenger.provider.ChatRepository;

import javax.inject.Inject;

import io.realm.Realm;

public class NewGroupChatCommand implements GcmCommand {

    @Inject
    ChatRepository chatRepository;

    @Inject
    Gson gson;

    private ChatData chatData;

    public NewGroupChatCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        chatData = gson.fromJson(extraData, ChatData.class);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        chatRepository.createGroupChat(chatData);
        realm.commitTransaction();
    }
}
