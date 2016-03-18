package com.pgizka.gsenger.mainView.chats;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public interface ChatsContract {

    interface View<Model> {

        AppCompatActivity getHoldingActivity();

        void displayChatsList(Model model);

        void displayErrorMessage(AlertDialog alertDialog);

        void setPresenter(Presenter presenter);

    }

    interface Presenter {

        void chatClicked(int chatId, int position, ChatToDisplay chatToDisplay);

    }

}
