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

public class ConversationPresenter extends Fragment implements ConversationContract.Presenter {

    private ConversationContract.View conversationView;
    private AppCompatActivity activity;
    private Realm realm;

    private int chatId;
    private int friendId;

    private Friend friend;
    private Chat chat;

    private RealmResults<Message> messages;

    @Inject
    JobManager jobManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);
        realm = Realm.getDefaultInstance();

        conversationView = (ConversationContract.View) getTargetFragment();

        activity = conversationView.getHoldingActivity();

        Bundle arguments = getArguments();
        chatId = arguments.getInt(ConversationActivity.CHAT_ID_ARGUMENT, -1);
        friendId = arguments.getInt(ConversationActivity.FRIEND_ID_ARGUMENT, -1);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friend = realm.where(Friend.class)
                .equalTo("id", friendId)
                .findFirst();

        chat = realm.where(Chat.class)
                .equalTo("friends.id", friendId)
                .equalTo("type", Chat.Type.SINGLE_CONVERSATION.code)
                .findFirst();

        messages = realm.where(Message.class)
                .equalTo("chat.id", chat.getId())
                .findAll();

        conversationView.displayConversationItems(messages);

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
        /*
        Message message = new Message();
        message.setText(text);
        message.setChatId(chat.getId());
        message.setSendDate(System.currentTimeMillis());
        message.setState(GSengerContract.CommonTypes.State.WAITING_TO_SEND.code);
        messageRepository.insertMessage(message);

        ToFriend toFriend = new ToFriend();
        toFriend.setCommonTypeId(message.getId());
        toFriend.setToFriendId(friend.getId());
        toFriendRepository.insertToFriend(toFriend);

        if (chat.getStartedDate() == 0) {
            chat.setStartedDate(System.currentTimeMillis());
            chatRepository.updateChat(chat);
        }*/

//        jobManager.addJob(new SendMessageJob(message.getId()));
    }
}
