package com.pgizka.gsenger.mainView.chats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.conversationView.ConversationActivity;
import com.pgizka.gsenger.provider.Chat;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ChatsPresenter implements ChatsContract.Presenter {


    private ChatsContract.View chatsView;
    private AppCompatActivity activity;
    private RealmResults<Chat> chats;

    private Realm realm;

    @Override
    public void onCreate(ChatsContract.View view) {
        chatsView = view;
        realm = Realm.getDefaultInstance();
        activity = chatsView.getHoldingActivity();
    }

    @Override
    public void onStart() {
        chats = realm.where(Chat.class).findAll();
        chatsView.displayChatsList(chats);

        chats.addChangeListener(() -> {
            chats = realm.where(Chat.class).findAll();
            chatsView.displayChatsList(chats);
        });
    }

    @Override
    public void chatClicked(Chat chat) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        if (chat.getType() == Chat.Type.GROUP.code) {
            intent.putExtra(ConversationActivity.CHAT_ID_ARGUMENT, chat.getId());
        } else {
            //FIXME possible errors when owner id is get
            intent.putExtra(ConversationActivity.USER_ID_ARGUMENT, chat.getUsers().last().getId());
        }
        activity.startActivity(intent);
    }

}
