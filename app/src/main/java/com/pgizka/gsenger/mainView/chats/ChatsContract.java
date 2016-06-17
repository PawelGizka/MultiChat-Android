package com.pgizka.gsenger.mainView.chats;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.Chat;

import java.util.List;

public interface ChatsContract {

    interface View {

        AppCompatActivity getHoldingActivity();

        void displayChatsList(List<Chat> chats);

        void displayErrorMessage(AlertDialog alertDialog);

    }

    interface Presenter {

        void onCreate(View view);

        void onDestroy();

        void onStart();

        void chatClicked(Chat chat);

    }

}
