package com.pgizka.gsenger.mainView.friends;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.realm.Friend;

import java.util.List;

public interface FriendsContract {

    interface Presenter {

        void onCreate(View view);

        void onDestroy();

        void onStart();

        void friendClicked(int position, Friend friend);

        void refreshFriends();
    }

    interface View {

        AppCompatActivity getHoldingActivity();

        void displayContactsList(List<Friend> friends);

        void displayErrorMessage(AlertDialog alertDialog);

        void dismissRefreshing();

    }

}
