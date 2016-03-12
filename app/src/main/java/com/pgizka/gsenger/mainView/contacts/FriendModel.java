package com.pgizka.gsenger.mainView.contacts;


import android.content.Context;
import android.database.Cursor;

import com.pgizka.gsenger.provider.GSengerContract;

public class FriendModel {



    public Friend getFriendByServerId(Context context, int serverId) {

        String selection = GSengerContract.Friends.FRIEND_SERVER_ID + "=?";
        String [] selectionArgs = new String[]{Integer.toString(serverId)};

        Cursor cursor = context.getContentResolver().query(GSengerContract.Friends.CONTENT_URI, null, selection, selectionArgs, null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
        }

        return new Friend();
    }



}
