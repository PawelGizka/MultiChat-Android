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
import com.pgizka.gsenger.mainView.chats.ChatsFragment;
import com.pgizka.gsenger.mainView.chats.ChatsPresenter;
import com.pgizka.gsenger.mainView.friends.FriendsFragment;
import com.pgizka.gsenger.provider.realm.Chat;
import com.pgizka.gsenger.provider.realm.Friend;
import com.pgizka.gsenger.provider.realm.Message;
import com.pgizka.gsenger.provider.realm.Receiver;
import com.pgizka.gsenger.provider.realm.TextMessage;

import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

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
        Friend friend = new Friend();
        friend.setId(0);
        friend.setServerId(123);
        friend.setUserName("Pawel");
        friend.setStatus("my super status");

        Chat chat = new Chat();
        chat.setId(0);
        chat.setType(Chat.Type.SINGLE_CONVERSATION.code);

        TextMessage textMessage = new TextMessage();
        textMessage.setText("hello everyone");

        Message message = new Message();
        message.setId(0);
        message.setType(Message.Type.TEXT_MESSAGE.code);
        message.setOutgoing(true);

        Receiver receiver = new Receiver();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        friend = realm.copyToRealm(friend);
        chat = realm.copyToRealm(chat);
        chat.setFriends(new RealmList<>(friend));
        friend.setChats(new RealmList<Chat>(chat));
        textMessage = realm.copyToRealm(textMessage);
        message = realm.copyToRealm(message);
        message.setTextMessage(textMessage);
        message.setChat(chat);
        chat.setMessages(new RealmList<Message>(message));
        receiver = realm.copyToRealm(receiver);
        receiver.setMessage(message);
        receiver.setFriend(friend);
        realm.commitTransaction();
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

                return chatsFragment;
            } else {
                if(friendsFragment == null) {
                    friendsFragment = new FriendsFragment();
                }

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
