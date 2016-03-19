package com.pgizka.gsenger.util;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class ContactsUtil {

    private Context context;

    public ContactsUtil(Context context) {
        this.context = context;
    }

    public List<String> listAllContactsPhoneNumbers() {
        List<String> phoneNumbers = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor contacts = context.getContentResolver().query(uri, projection, null, null, null);

        int indexName = contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if (contacts.moveToFirst()) {
            do {
                phoneNumbers.add(contacts.getString(indexName));
            } while (contacts.moveToNext());
        }

        return phoneNumbers;
    }

}
