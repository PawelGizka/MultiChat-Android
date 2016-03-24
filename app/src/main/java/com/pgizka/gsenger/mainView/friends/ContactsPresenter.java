package com.pgizka.gsenger.mainView.friends;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.conversationView.ConversationActivity;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.jobqueue.getContacts.GetContactsFinishedEvent;
import com.pgizka.gsenger.jobqueue.getContacts.GetContactsJob;
import com.pgizka.gsenger.provider.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ContactsPresenter implements ContactsContract.Presenter {

    private ContactsContract.View contactsView;
    private AppCompatActivity activity;

    private Realm realm;

    private RealmResults<User> users;

    @Inject
    JobManager jobManager;

    @Inject
    EventBus eventBus;

    @Override
    public void onCreate(ContactsContract.View view) {
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
        getUsers();
        contactsView.displayContactsList(users);

        users.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                getUsers();
                contactsView.displayContactsList(users);
            }
        });
    }

    private void getUsers() {
        //get all users except owner of this phone
        users = realm.where(User.class)
                .notEqualTo("id", 0) //owner id = 0
                .findAll();
    }

    @Override
    public void friendClicked(int position, User user) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        intent.putExtra(ConversationActivity.USER_ID_ARGUMENT, user.getId());
        activity.startActivity(intent);
    }

    @Override
    public void refreshFriends() {
        jobManager.addJob(new GetContactsJob());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GetContactsFinishedEvent friendsFinishedEvent) {
        contactsView.dismissRefreshing();
    }


}
