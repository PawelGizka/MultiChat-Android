package com.pgizka.gsenger.provider.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.Chat;
import com.pgizka.gsenger.provider.pojos.ToFriend;

public class ToFriendRepository {

    private Context context;
    private ProviderUtils providerUtils;

    public ToFriendRepository(Context context, ProviderUtils providerUtils) {
        this.context = context;
        this.providerUtils = providerUtils;
    }

    public void insertToFriend(ToFriend toFriend) {
        providerUtils.insertToFriend(toFriend);
    }

    public void updateToFriend(ToFriend toFriend) {
        String toFriendId = String.valueOf(toFriend.getToFriendId());
        String commonTypeId = String.valueOf(toFriend.getCommonTypeId());

        Uri uri = GSengerContract.ToFriends.buildToFriendUri(toFriendId, commonTypeId);
        ContentValues contentValues = ContentValueUtils.createToFriend(toFriend);
        context.getContentResolver().update(uri, contentValues, null, null);
    }

    public ToFriend getToFriendById(int toFriendId, int commonTypeId) {
        Uri uri = GSengerContract.ToFriends.buildToFriendUri(String.valueOf(toFriendId), String.valueOf(commonTypeId));

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        ToFriend toFriend = null;
        if (cursor.moveToFirst()) {
            toFriend = buildToFriend(cursor);
        }

        return toFriend;
    }

    public ToFriend buildToFriend(Cursor cursor) {
        ToFriend toFriend = new ToFriend();

        toFriend.setToFriendId(cursor.getInt(cursor.getColumnIndex(GSengerContract.ToFriends.TO_FRIEND_ID)));
        toFriend.setCommonTypeId(cursor.getInt(cursor.getColumnIndex(GSengerContract.ToFriends.COMMON_TYPE_ID)));
        toFriend.setDelivered(cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.DELIVERED_DATE)));
        toFriend.setViewed(cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.VIEWED_DATE)));

        return toFriend;
    }

}
