package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.Message;

import java.util.List;

public interface ConversationContract {

    interface View {

        AppCompatActivity getHoldingActivity();

        void displayConversationItems(List<Message> conversationItems);

        void displayErrorMessage(String message);

        void setPresenter(ConversationContract.Presenter presenter);

    }

    interface Presenter {

        void sendMessage(String text);

        void onCreate(View view, int friendId);

        void onResume();

        void onPause();

    }

}
