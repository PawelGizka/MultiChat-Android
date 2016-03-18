package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

public interface ConversationContract {

    interface View {

        AppCompatActivity getHoldingActivity();

        void displayConversationItems(List<ConversationItem> conversationItems);

        void displayErrorMessage(String message);

        void setPresenter(ConversationContract.Presenter presenter);

    }

    interface Presenter {

        void sendMessage(String text);

    }

}
