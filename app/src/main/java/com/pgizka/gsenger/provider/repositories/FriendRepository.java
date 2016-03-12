package com.pgizka.gsenger.provider.repositories;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.Friend;

import javax.inject.Inject;

public class FriendRepository {

    @Inject
    Context context;

    @Inject
    ProviderUtils providerUtils;

    public FriendRepository(Context context, ProviderUtils providerUtils) {
        this.context = context;
        this.providerUtils = providerUtils;
    }

    public int saveFriend(Friend friend) {
        Uri uri = providerUtils.insertFriend(friend.getServerId(), friend.getUserName(),
                friend.getAddedDate(), friend.getStatus(), friend.getLastLoggedDate(),
                friend.getPhotoPath(), friend.getPhotoHash());
        return Integer.parseInt(uri.getLastPathSegment());
    }

    public Friend getFriendByServerId(int serverId) {

        String selection = GSengerContract.Friends.FRIEND_SERVER_ID + "=?";
        String [] selectionArgs = new String[]{Integer.toString(serverId)};

        Cursor cursor = context.getContentResolver().query(GSengerContract.Friends.CONTENT_URI, null, selection, selectionArgs, null);

        Friend friend = null;
        if (cursor.moveToFirst()) {
            friend = makeFriend(cursor);
        }

        return friend;
    }

    public static Friend makeFriend(Cursor cursor) {

        Friend contact = new Friend();
        contact.setUserName(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));
        contact.setStatus(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.STATUS)));
        contact.setPhotoPath(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.PHOTO)));
        contact.setAddedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.Friends.ADDED_DATE)));
        contact.setLastLoggedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.Friends.LAST_LOGGED_DATE)));

        return contact;
    }



}
