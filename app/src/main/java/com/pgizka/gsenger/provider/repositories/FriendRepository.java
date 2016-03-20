package com.pgizka.gsenger.provider.repositories;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.pgizka.gsenger.gcm.GCMUTil;
import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.GSengerDatabase;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.Friend;

public class FriendRepository {

    private Context context;
    private ProviderUtils providerUtils;

    public FriendRepository(Context context, ProviderUtils providerUtils) {
        this.context = context;
        this.providerUtils = providerUtils;
    }

    public int insertFriend(Friend friend) {
        Uri uri = providerUtils.insertFriend(friend);
        int id = Integer.parseInt(uri.getLastPathSegment());
        friend.setId(id);
        return id;
    }

    public void updateFriend(Friend friend) {
        String friendId = String.valueOf(friend.getId());
        Uri friendUri = GSengerContract.Friends.buildFriendUri(friendId);
        ContentValues contentValues = ContentValueUtils.createFriend(friend);
        context.getContentResolver().update(friendUri, contentValues, null, null);
    }

    public Friend getFriendById(int id) {
        String selection = GSengerDatabase.Tables.FRIENDS + "." + BaseColumns._ID + "=?";
        String [] selectionArgs = new String[]{Integer.toString(id)};

        return getFriendBy(selection, selectionArgs);
    }

    public Friend getFriendByServerId(int serverId) {
        String selection = GSengerDatabase.Tables.FRIENDS + "." + GSengerContract.Friends.FRIEND_SERVER_ID + "=?";
        String [] selectionArgs = new String[]{Integer.toString(serverId)};

        return getFriendBy(selection, selectionArgs);
    }

    public Friend getFriendBy(String selection, String [] selectionArgs) {
        Cursor cursor = context.getContentResolver().query(GSengerContract.Friends.CONTENT_URI, null, selection, selectionArgs, null);

        Friend friend = null;
        if (cursor.moveToFirst()) {
            friend = buildFriend(cursor);
        }

        return friend;
    }

    public void deleteFriend(Friend friend) {
        int id = friend.getId();
        Uri uri = GSengerContract.Friends.buildFriendUri(String.valueOf(id));
        context.getContentResolver().delete(uri, null, null);
    }

    public Friend buildFriend(Cursor cursor) {

        Friend contact = new Friend();
        contact.setId(cursor.getInt(cursor.getColumnIndex(GSengerContract.Friends._ID)));
        contact.setUserName(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));
        contact.setStatus(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.STATUS)));
        contact.setPhotoPath(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.PHOTO)));
        contact.setAddedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.Friends.ADDED_DATE)));
        contact.setLastLoggedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.Friends.LAST_LOGGED_DATE)));

        return contact;
    }



}
