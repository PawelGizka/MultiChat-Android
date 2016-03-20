package com.pgizka.gsenger.conversationView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.pojos.Chat;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.pojos.Message;
import com.pgizka.gsenger.provider.pojos.ToFriend;
import com.pgizka.gsenger.provider.repositories.ChatRepository;
import com.pgizka.gsenger.provider.repositories.FriendRepository;
import com.pgizka.gsenger.provider.repositories.MessageRepository;
import com.pgizka.gsenger.provider.repositories.ToFriendRepository;

import javax.inject.Inject;

public class ConversationPresenter extends Fragment implements ConversationContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private ConversationContract.View view;
    private AppCompatActivity activity;
    private ConversationModel conversationModel;

    private int chatId;
    private int friendId;

    private Friend friend;
    private Chat chat;

    @Inject
    FriendRepository friendRepository;

    @Inject
    ChatRepository chatRepository;

    @Inject
    MessageRepository messageRepository;

    @Inject
    ToFriendRepository toFriendRepository;

    @Inject
    JobManager jobManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);

        view = (ConversationContract.View) getTargetFragment();

        activity = view.getHoldingActivity();
        conversationModel = new ConversationModel();

        Bundle arguments = getArguments();
        chatId = arguments.getInt(ConversationActivity.CHAT_ID_ARGUMENT, -1);
        friendId = arguments.getInt(ConversationActivity.FRIEND_ID_ARGUMENT, -1);

        friend = friendRepository.getFriendById(friendId);

        if (chatId == -1) {
            chat = chatRepository.getConversationChatByFriendId(friendId);
        } else {
            chat = chatRepository.getChatById(chatId);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = GSengerContract.Chats.buildChatConversationUri(String.valueOf(chat.getId()));
        CursorLoader cursorLoader = new CursorLoader(activity, uri, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        conversationModel.readDataFromCursor(data);
        view.displayConversationItems(conversationModel.getConversationItems());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void sendMessage(String text) {
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
        }

//        jobManager.addJob(new SendMessageJob(message.getId()));
    }
}
