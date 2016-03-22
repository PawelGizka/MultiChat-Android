package com.pgizka.gsenger.mainView.friends;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.conversationView.ConversationActivity;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsFinishedEvent;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsJob;
import com.pgizka.gsenger.provider.realm.Friend;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class FriendsPresenter implements FriendsContract.Presenter {

    private FriendsContract.View contactsView;
    private AppCompatActivity activity;

    private Realm realm;

    private RealmResults<Friend> friends;

    @Inject
    JobManager jobManager;

    @Inject
    EventBus eventBus;

    @Override
    public void onCreate(FriendsContract.View view) {
        contactsView = view;
        GSengerApplication.getApplicationComponent().inject(this);
        realm = Realm.getDefaultInstance();
        eventBus.register(this);
        activity = contactsView.getHoldingActivity();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void onStart() {
        friends = realm.where(Friend.class).findAll();
        contactsView.displayContactsList(friends);

        friends.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                friends = realm.where(Friend.class).findAll();
                contactsView.displayContactsList(friends);
            }
        });
    }

    @Override
    public void friendClicked(int position, Friend friend) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        intent.putExtra(ConversationActivity.FRIEND_ID_ARGUMENT, friend.getId());
        activity.startActivity(intent);
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
