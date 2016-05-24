package com.pgizka.gsenger.conversationView.mediaView;

import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MediaDetailPresenter implements MediaDetailContract.Presenter {

    private MediaDetailContract.View view;

    private Chat chat;
    private Message initialMessage;
    private RealmResults<Message> messages;

    private Realm realm;

    private int savedPosition;

    @Override
    public void onCreate(MediaDetailContract.View view, int messageId, int savedPosition) {
        this.view = view;
        this.savedPosition = savedPosition;

        realm = Realm.getDefaultInstance();

        initialMessage = realm.where(Message.class)
                .equalTo("id", messageId)
                .findFirst();

        chat = realm.where(Chat.class)
                .equalTo("id", initialMessage.getChat().getId())
                .findFirst();
    }

    @Override
    public void onResume() {
        getMessages();
        view.displayMessages(messages);

        if (savedPosition != -1) {
            view.setInitialPosition(savedPosition);
        } else {
            view.setInitialPosition(messages.indexOf(initialMessage));
        }

        messages.addChangeListener(messages -> {
            this.messages = messages;
            view.displayMessages(messages);
        });
    }

    private void getMessages() {
        messages = realm.where(Message.class)
                .equalTo("chat.id", chat.getId())
                .equalTo("type", Message.Type.MEDIA_MESSAGE.code)
                .beginGroup()
                .equalTo("mediaMessage.mediaType", MediaMessage.Type.PHOTO.code)
                .or()
                .equalTo("mediaMessage.mediaType", MediaMessage.Type.VIDEO.code)
                .findAll();
    }

}
