package com.pgizka.gsenger.conversationView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.mainView.chats.ChatToDisplay;
import com.pgizka.gsenger.mainView.chats.ChatsToDisplayModel;
import com.pgizka.gsenger.mainView.chats.ChatsView;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.repositories.FriendRepository;

import javax.inject.Inject;

public class ConversationPresenter extends Fragment implements ConversationContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private ConversationContract.View view;
    private AppCompatActivity activity;
    private ConversationModel conversationModel;

    private int chatId;
    private int friendId;

    @Inject
    FriendRepository friendRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = view.getHoldingActivity();
        conversationModel = new ConversationModel();

        Bundle arguments = getArguments();
        chatId = arguments.getInt(ConversationActivity.CHAT_ID_ARGUMENT, -1);
        friendId = arguments.getInt(ConversationActivity.FRIEND_ID_ARGUMENT, -1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (chatId == -1) {

        }

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = GSengerContract.Chats.buildChatConversationUri(String.valueOf(chatId));
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

    }
}
