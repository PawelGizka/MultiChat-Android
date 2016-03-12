package com.pgizka.gsenger.mainView.contacts;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.GSengerContract;

public class ContactsPresenterImpl extends Fragment implements ContactsPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    ContactsView<ContactsModel> contactsView;
    AppCompatActivity activity;
    ContactsModel contactsModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        contactsModel = new ContactsModel();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    public void setContactsView(ContactsView<ContactsModel> contactsView) {
        this.contactsView = contactsView;
    }

    @Override
    public void contactClicked(int contactId, int position, Contact contact) {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = GSengerContract.Friends.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(activity, uri, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean successfullyReadData = contactsModel.readDataFromCursor(data);
        if(successfullyReadData) {
            contactsView.displayContactsList(contactsModel);
        } else {
            contactsView.displayErrorMessage(buildErrorReadingDataAlert());
        }
    }

    private AlertDialog buildErrorReadingDataAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Error while reading data")
                .setNeutralButton("Ok", null);
        return builder.create();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
