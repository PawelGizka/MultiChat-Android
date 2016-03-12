package com.pgizka.gsenger.mainView.chats;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public interface ChatsView<M> {

    AppCompatActivity getHoldingActivity();

    void displayChatsList(M model);

    void displayErrorMessage(AlertDialog alertDialog);

    void setPresenter(ChatsPresenter presenter);

}
