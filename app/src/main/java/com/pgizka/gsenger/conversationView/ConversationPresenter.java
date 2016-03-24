package com.pgizka.gsenger.conversationView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.realm.Chat;
import com.pgizka.gsenger.provider.realm.Friend;
import com.pgizka.gsenger.provider.realm.Message;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ConversationPresenter implements ConversationContract.Presenter {

    private ConversationContract.View conversationView;
    private AppCompatActivity activity;
    private Realm realm;

    private int friendId;

    private Friend friend;
    private Chat chat;

    private RealmResults<Message> messages;

    @Inject
    JobManager jobManager;

    @Override
    public void onCreate(ConversationContract.View view, int friendId) {
        GSengerApplication.getApplicationComponent().inject(this);
        conversationView = view;
        realm = Realm.getDefaultInstance();
        this.friendId = friendId;
        activity = conversationView.getHoldingActivity();
    }

    @Override
    public void onStart() {
        friend = realm.where(Friend.class)
                .equalTo("id", friendId)
                .findFirst();

        getChat();

        if (chat != null) {
            getMessages();
            conversationView.displayConversationItems(messages);
        } else {
            realm.where(Chat.class).findAll().addChangeListener(new RealmChangeListener() {
                @Override
                public void onChange() {
                    getChat();
                    if (chat != null) {
                        getMessages();
                    }
                }
            });
        }
    }

    private void getChat() {
        chat = realm.where(Chat.class)
                .equalTo("friends.id", friendId)
                .equalTo("type", Chat.Type.SINGLE_CONVERSATION.code)
                .findFirst();
    }

    private void getMessages() {
        messages = realm.where(Message.class)
                .equalTo("chat.id", chat.getId())
                .findAll();

        messages.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                messages = realm.where(Message.class)
                        .equalTo("chat.id", chat.getId())
                        .findAll();
                conversationView.displayConversationItems(messages);
            }
        });
    }

    @Override
    public void sendMessage(String text) {

    }
}
