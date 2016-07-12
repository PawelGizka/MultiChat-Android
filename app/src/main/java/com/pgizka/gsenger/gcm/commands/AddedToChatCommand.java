package com.pgizka.gsenger.gcm.commands;

import android.content.Context;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.api.dtos.chats.ChatData;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.jobqueue.getMessages.GetMessagesJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;

import javax.inject.Inject;

import io.realm.Realm;


public class AddedToChatCommand extends GCMCommand {

    @Inject
    ChatRepository chatRepository;

    @Inject
    JobManager jobManager;

    @Inject
    Gson gson;

    private ChatData chatData;

    public AddedToChatCommand() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void execute(Context context, String action, String extraData) {
        chatData = gson.fromJson(extraData, ChatData.class);

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Chat chat = chatRepository.createGroupChat(chatData);
        realm.commitTransaction();

        jobManager.addJob(new GetMessagesJob(chat.getId()));
    }
}
