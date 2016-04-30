package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.User;

import java.util.List;

public interface ConversationContract {

    interface View {

        AppCompatActivity getHoldingActivity();

        void displayConversationItems(List<Message> conversationItems);

        void displayErrorMessage(String message);

        void displayChatImage(User user);

        void displayChatName(String userName);

        void setPresenter(ConversationContract.Presenter presenter);

    }

    interface Presenter {

        void sendMessage(String text);

        void onCreate(View view, int friendId, int chatId);

        void onResume();

        void onPause();

    }

}
