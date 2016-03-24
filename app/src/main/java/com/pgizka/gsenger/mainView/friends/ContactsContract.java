package com.pgizka.gsenger.mainView.friends;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.User;

import java.util.List;

public interface ContactsContract {

    interface Presenter {

        void onCreate(View view);

        void onDestroy();

        void onStart();

        void friendClicked(int position, User user);

        void refreshFriends();
    }

    interface View {

        AppCompatActivity getHoldingActivity();

        void displayContactsList(List<User> users);

        void displayErrorMessage(AlertDialog alertDialog);

        void dismissRefreshing();

    }

}
