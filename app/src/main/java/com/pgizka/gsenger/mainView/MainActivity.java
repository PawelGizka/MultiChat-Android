package com.pgizka.gsenger.mainView;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.dagger2.SimpleComponent;
import com.pgizka.gsenger.dagger2.TestDependency;
import com.pgizka.gsenger.mainView.chats.ChatsFragment;
import com.pgizka.gsenger.mainView.chats.ChatsPresenterImpl;
import com.pgizka.gsenger.mainView.contacts.ContactsFragment;
import com.pgizka.gsenger.mainView.contacts.ContactsPresenterImpl;
import com.pgizka.gsenger.welcome.WelcomeActivity;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    private static final String CHATS_FRAGMENT_TAG = "chatsFragmentTag";
    private static final String CHATS_PRESENTER_TAG = "chatsPresenterTag";

    private static final String CONTACTS_FRAGMENT_TAG = "contactsFragmentTag";
    private static final String CONTACTS_PRESENTER_TAG = "contactsPresenterTag";

    TestDependency testDependency;

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

        SimpleComponent simpleComponent = GSengerApplication.getSimpleComponent();
        testDependency = simpleComponent.wgetTestDependency();

        Toast.makeText(this, testDependency.getString(), Toast.LENGTH_SHORT).show();
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
                ChatsFragment chatsFragment = (ChatsFragment) fragmentManager.findFragmentByTag(CHATS_FRAGMENT_TAG);
                if(chatsFragment == null) {
                    chatsFragment = new ChatsFragment();
                }

                ChatsPresenterImpl chatsPresenter = (ChatsPresenterImpl) fragmentManager.findFragmentByTag(CHATS_PRESENTER_TAG);
                if(chatsPresenter == null) {
                    chatsPresenter = new ChatsPresenterImpl();
                    fragmentManager.beginTransaction().add(chatsPresenter, CHATS_PRESENTER_TAG).commit();
                    chatsPresenter.setChatsView(chatsFragment);
                }
                chatsFragment.setPresenter(chatsPresenter);

                return chatsFragment;
            } else {
                ContactsFragment contactsFragment = (ContactsFragment) fragmentManager.findFragmentByTag(CONTACTS_FRAGMENT_TAG);
                if(contactsFragment == null) {
                    contactsFragment = new ContactsFragment();
                }

                ContactsPresenterImpl contactsPresenter = (ContactsPresenterImpl) fragmentManager.findFragmentByTag(CONTACTS_PRESENTER_TAG);
                if(contactsPresenter == null) {
                    contactsPresenter = new ContactsPresenterImpl();
                    fragmentManager.beginTransaction().add(contactsPresenter, CONTACTS_PRESENTER_TAG).commit();
                    contactsPresenter.setContactsView(contactsFragment);
                }
                contactsFragment.setPresenter(contactsPresenter);

                return contactsFragment;
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
