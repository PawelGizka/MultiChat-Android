package com.pgizka.gsenger.mainView.contacts;

import android.database.Cursor;

import com.pgizka.gsenger.provider.GSengerContract;

import java.util.ArrayList;
import java.util.List;

public class ContactsModel {

    private List<Contact> contacts;

    

    public boolean readDataFromCursor(Cursor cursor) {
        contacts = new ArrayList<>();

        if(!cursor.moveToFirst()){
            return true;
        }

        do {
            contacts.add(makeContact(cursor));
        } while (cursor.moveToNext());


        return true;
    }

    private Contact makeContact(Cursor cursor) {

        Contact contact = new Contact();
        contact.setUserName(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));
        contact.setStatus(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.STATUS)));
        contact.setPhoto(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.PHOTO)));
        contact.setAddedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.Friends.ADDED_DATE)));
        contact.setLastLoggedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.Friends.LAST_LOGGED_DATE)));

        return contact;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
