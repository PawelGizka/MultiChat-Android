package com.pgizka.gsenger.mainView;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.mainView.chats.ChatsFragment;
import com.pgizka.gsenger.mainView.chats.ChatsPresenterImpl;
import com.pgizka.gsenger.mainView.friends.FriendsFragment;
import com.pgizka.gsenger.mainView.friends.FriendsPresenter;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.pojos.Chat;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.pojos.Message;
import com.pgizka.gsenger.provider.pojos.ToFriend;
import com.pgizka.gsenger.provider.repositories.ChatRepository;
import com.pgizka.gsenger.provider.repositories.FriendHasChatRepository;
import com.pgizka.gsenger.provider.repositories.FriendRepository;
import com.pgizka.gsenger.provider.repositories.MessageRepository;
import com.pgizka.gsenger.provider.repositories.ToFriendRepository;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    private static final String CHATS_PRESENTER_TAG = "chatsPresenterTag";
    private static final String CONTACTS_PRESENTER_TAG = "contactsPresenterTag";

    private ChatsFragment chatsFragment;
    private FriendsFragment friendsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
//        createChat();
    }

    private void createChat() {
        ApplicationComponent applicationComponent = GSengerApplication.getApplicationComponent();
        FriendRepository friendRepository = applicationComponent.friendRepository();
        MessageRepository messageRepository = applicationComponent.messageRepository();
        ToFriendRepository toFriendRepository = applicationComponent.toFriendRepository();
        ChatRepository chatRepository = applicationComponent.chatRepository();
        FriendHasChatRepository friendHasChatRepository = applicationComponent.friendHasChatRepository();

        Friend friend = new Friend();
        friend.setUserName("pawel");
        friend.setServerId(124);
        friend.setAddedDate(131231);
        friend.setStatus("haha");
        friendRepository.insertFriend(friend);

        Chat chat = new Chat();
        chat.setServerId(13);
        chat.setType(GSengerContract.Chats.CHAT_TYPE_CONVERSATION);
        chat.setStartedDate(12312);
        chatRepository.insertChat(chat);

        friendHasChatRepository.insertFriendHasChat(friend.getId(), chat.getId());

        Message message = new Message();
        message.setText("hello !!!");
        message.setChatId(chat.getId());
        message.setOutgoing(true);
        message.setSendDate(System.currentTimeMillis());
        message.setState(GSengerContract.CommonTypes.State.WAITING_TO_SEND.code);
        messageRepository.insertMessage(message);

        ToFriend toFriend = new ToFriend();
        toFriend.setCommonTypeId(message.getId());
        toFriend.setToFriendId(friend.getId());
        toFriendRepository.insertToFriend(toFriend);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(position == 0) {
                if(chatsFragment == null) {
                    chatsFragment = new ChatsFragment();
                }

                ChatsPresenterImpl chatsPresenter = (ChatsPresenterImpl) fragmentManager.findFragmentByTag(CHATS_PRESENTER_TAG);
                if(chatsPresenter == null) {
                    chatsPresenter = new ChatsPresenterImpl();
                    fragmentManager.beginTransaction().add(chatsPresenter, CHATS_PRESENTER_TAG).commit();
                }

                chatsFragment.setTargetFragment(chatsPresenter, 0);
                chatsPresenter.setTargetFragment(chatsFragment, 0);

                return chatsFragment;
            } else {
                if(friendsFragment == null) {
                    friendsFragment = new FriendsFragment();
                }

                FriendsPresenter contactsPresenter = (FriendsPresenter) fragmentManager.findFragmentByTag(CONTACTS_PRESENTER_TAG);
                if(contactsPresenter == null) {
                    contactsPresenter = new FriendsPresenter();
                    fragmentManager.beginTransaction().add(contactsPresenter, CONTACTS_PRESENTER_TAG).commit();
                }
                friendsFragment.setTargetFragment(contactsPresenter, 0);
                contactsPresenter.setTargetFragment(friendsFragment, 0);

                return friendsFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Chats";
                case 1:
                    return "Contacts";
            }
            return null;
        }
    }
}
