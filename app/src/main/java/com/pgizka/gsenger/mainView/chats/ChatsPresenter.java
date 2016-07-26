package com.pgizka.gsenger.mainView.chats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.conversationView.ConversationActivity;
import com.pgizka.gsenger.provider.Chat;

import io.realm.Realm;
import io.realm.RealmResults;

public class ChatsPresenter implements ChatsContract.Presenter {


    private ChatsContract.View view;
    private AppCompatActivity activity;
    private RealmResults<Chat> chats;

    private Realm realm;

    @Override
    public void onCreate(ChatsContract.View view) {
        this.view = view;
        realm = Realm.getDefaultInstance();
        activity = this.view.getHoldingActivity();
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onStart() {
        chats = realm.where(Chat.class)
                .equalTo("type", Chat.Type.GROUP.code)
                .or()
                .beginGroup()
                .equalTo("type", Chat.Type.SINGLE_CONVERSATION.code)
                .isNotEmpty("chats")
                .endGroup()
                .findAll();

        view.displayChatsList(chats);

        chats.addChangeListener(element -> {
            chats = element;
            view.displayChatsList(chats);
        });
    }

    @Override
    public void chatClicked(Chat chat) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        if (chat.getType() == Chat.Type.GROUP.code) {
            intent.putExtra(ConversationActivity.CHAT_ID_ARGUMENT, chat.getId());
        }
        activity.startActivity(intent);
    }

}
