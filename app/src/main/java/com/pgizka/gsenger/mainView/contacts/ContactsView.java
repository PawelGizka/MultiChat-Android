package com.pgizka.gsenger.mainView.contacts;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public interface ContactsView<M> {

    AppCompatActivity getHoldingActivity();

    void setPresenter(ContactsPresenter presenter);

    void displayContactsList(M model);

    void displayErrorMessage(AlertDialog alertDialog);

}
