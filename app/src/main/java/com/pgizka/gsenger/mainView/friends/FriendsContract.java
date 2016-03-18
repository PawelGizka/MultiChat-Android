package com.pgizka.gsenger.mainView.friends;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.pojos.Friend;

public interface FriendsContract {

    interface Presenter {

        void friendClicked(int contactId, int position, Friend friend);

        void refreshFriends();
    }

    interface View<Model> {

        AppCompatActivity getHoldingActivity();

        void displayContactsList(Model model);

        void displayErrorMessage(AlertDialog alertDialog);

        void dismissRefreshing();

    }

}
