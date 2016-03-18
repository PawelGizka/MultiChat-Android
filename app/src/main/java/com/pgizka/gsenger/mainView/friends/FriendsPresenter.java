package com.pgizka.gsenger.mainView.friends;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsFinishedEvent;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsJob;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.pojos.Friend;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class FriendsPresenter extends Fragment implements FriendsContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private FriendsContract.View<FriendsModel> contactsView;
    private AppCompatActivity activity;
    private FriendsModel friendsModel;

    @Inject
    JobManager jobManager;

    @Inject
    EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsView = (FriendsContract.View<FriendsModel>) getTargetFragment();
        GSengerApplication.getApplicationComponent().inject(this);
        eventBus.register(this);
        activity = (AppCompatActivity) getActivity();
        friendsModel = new FriendsModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = GSengerContract.Friends.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(activity, uri, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean successfullyReadData = friendsModel.readDataFromCursor(data);
        if(successfullyReadData) {
            contactsView.displayContactsList(friendsModel);
        } else {
            contactsView.displayErrorMessage(buildErrorReadingDataAlert());
        }
    }

    private AlertDialog buildErrorReadingDataAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Error while reading data")
                .setNeutralButton("Ok", null);
        return builder.create();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void friendClicked(int contactId, int position, Friend friend) {

    }

    @Override
    public void refreshFriends() {
        jobManager.addJob(new RefreshFriendsJob());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshFriendsFinishedEvent friendsFinishedEvent) {
        contactsView.dismissRefreshing();
    }


}
