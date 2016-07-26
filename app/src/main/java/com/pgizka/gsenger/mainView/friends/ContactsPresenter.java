package com.pgizka.gsenger.mainView.friends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.conversationView.ConversationActivity;
import com.pgizka.gsenger.jobqueue.getContacts.GetContactsFinishedEvent;
import com.pgizka.gsenger.jobqueue.getContacts.GetContactsJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class ContactsPresenter implements ContactsContract.Presenter {

    private ContactsContract.View view;
    private AppCompatActivity activity;

    private Realm realm;

    private RealmResults<User> users;

    @Inject
    JobManager jobManager;

    @Inject
    EventBus eventBus;

    @Inject
    ChatRepository chatRepository;

    @Override
    public void onCreate(ContactsContract.View view) {
        this.view = view;
        GSengerApplication.getApplicationComponent().inject(this);
        realm = Realm.getDefaultInstance();
        eventBus.register(this);
        activity = this.view.getHoldingActivity();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void onStart() {
        getUsers();
        view.displayContactsList(users);

        users.addChangeListener(element -> {
            getUsers();
            view.displayContactsList(users);
        });
    }

    private void getUsers() {
        //get all users except owner of this phone
        users = realm.where(User.class)
                .notEqualTo("id", 0) //owner id = 0
                .equalTo("inContacts", true) // only show users in contacts
                .findAll();
    }

    @Override
    public void friendClicked(int position, User user) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        Chat chat = chatRepository.getSingleConversationChatWith(user);
        intent.putExtra(ConversationActivity.CHAT_ID_ARGUMENT, chat.getId());
        activity.startActivity(intent);
    }

    @Override
    public void refreshFriends() {
        jobManager.addJob(new GetContactsJob());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GetContactsFinishedEvent friendsFinishedEvent) {
        view.dismissRefreshing();
    }


}
