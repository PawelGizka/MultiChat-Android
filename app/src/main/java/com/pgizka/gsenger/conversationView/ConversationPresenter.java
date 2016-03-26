package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Receiver;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.util.UserAccountManager;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ConversationPresenter implements ConversationContract.Presenter {

    private ConversationContract.View conversationView;
    private AppCompatActivity activity;
    private Realm realm;

    private int friendId;

    private User friend;
    private User owner;
    private Chat chat;

    private RealmResults<Message> messages;

    @Inject
    JobManager jobManager;

    @Inject
    UserAccountManager userAccountManager;

    @Inject
    Repository repository;

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
        owner = userAccountManager.getOwner();
        friend = realm.where(User.class)
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
                .equalTo("users.id", friendId)
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
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (chat == null) {
            chat = new Chat();
            chat.setId(repository.getChatNextId());
            chat.setType(Chat.Type.SINGLE_CONVERSATION.code);
            chat.setStartedDate(System.currentTimeMillis());
            chat = realm.copyToRealm(chat);
        }

        chat.setUsers(new RealmList<>(owner, friend));
        friend.getChats().add(chat);
        owner.getChats().add(chat);

        TextMessage textMessage = new TextMessage();
        textMessage.setText(text);
        textMessage = realm.copyToRealm(textMessage);

        Message message = new Message();
        message.setId(repository.getMessageNextId());
        message.setType(Message.Type.TEXT_MESSAGE.code);
        message.setSendDate(System.currentTimeMillis());
        message.setState(Message.State.WAITING_TO_SEND.code);
        message = realm.copyToRealm(message);

        message.setSender(owner);
        message.setChat(chat);
        message.setTextMessage(textMessage);
        chat.getMessages().add(message);

        Receiver receiver = new Receiver();
        receiver = realm.copyToRealm(receiver);
        receiver.setMessage(message);
        receiver.setUser(friend);

        message.setReceivers(new RealmList<>(receiver));

        realm.commitTransaction();

    }
}
