package com.pgizka.gsenger.mainView.chats;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.GSengerContract;

public class ChatsPresenterImpl extends Fragment implements ChatsPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    ChatsView<ChatsToDisplayModel> chatsView;
    AppCompatActivity activity;
    ChatsToDisplayModel chatsToDisplayModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = chatsView.getHoldingActivity();
        chatsToDisplayModel = new ChatsToDisplayModel();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    public void setChatsView(ChatsView chatsView) {
        this.chatsView = chatsView;
    }

    @Override
    public void chatClicked(int chatId, int position, ChatToDisplay chatToDisplay) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = GSengerContract.Chats.buildChatsToDisplayUri();
        CursorLoader cursorLoader = new CursorLoader(activity, uri, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean successfullyReadDate = chatsToDisplayModel.readDataFromCursor(data);
        if(successfullyReadDate) {
            chatsView.displayChatsList(chatsToDisplayModel);
        } else {
            chatsView.displayErrorMessage(buildErrorReadingDataAlert());
        }
    }

    private AlertDialog buildErrorReadingDataAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Error while reading data")
                .setNeutralButton("Ok", null);
        return builder.create();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
