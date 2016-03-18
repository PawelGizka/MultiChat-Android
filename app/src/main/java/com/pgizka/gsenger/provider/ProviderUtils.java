package com.pgizka.gsenger.provider;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.pgizka.gsenger.provider.pojos.CommonType;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.pojos.Media;
import com.pgizka.gsenger.provider.pojos.Message;

public class ProviderUtils {
    Context context;

    public ProviderUtils(Context context) {
        this.context = context;
    }

    public Uri insertCommonType(String type, long sendDate, int state, String senderId, String chatId) {
        ContentValues contentValues = ContentValueUtils.createCommonType(type, sendDate, state, senderId, chatId);
        Uri uri = GSengerContract.CommonTypes.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertCommonType(CommonType commonType) {
        ContentValues contentValues = ContentValueUtils.createCommonType(commonType);
        Uri uri = GSengerContract.CommonTypes.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertFriend(int serverId, String userName, long addedDate, String status,
                             long lastLoggedDate, String photoPath, String photoHash) {
        ContentValues contentValues = ContentValueUtils.createFriend(serverId, userName, addedDate, status, lastLoggedDate, photoPath, photoHash);
        Uri uri = GSengerContract.Friends.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertFriend(Friend friend) {
        ContentValues contentValues = ContentValueUtils.createFriend(friend);
        Uri uri = GSengerContract.Friends.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertToFriend(String friendId, String commonTypeId, long deliverDate, long viewedDate) {
        ContentValues contentValues = ContentValueUtils.createToFriend(friendId, commonTypeId, deliverDate, viewedDate);
        Uri uri = GSengerContract.ToFriends.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertChat(String type, String name, long startedDate) {
        ContentValues contentValues = ContentValueUtils.createChat(type, name, startedDate);
        Uri uri = GSengerContract.Chats.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertFriendHasChat(String friendId, String chatId) {
        ContentValues contentValues = ContentValueUtils.createFriendHasChat(friendId, chatId);
        Uri uri = GSengerContract.FriendHasChats.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertMessage(String commonTypeId, String text) {
        ContentValues contentValues = ContentValueUtils.createMessage(commonTypeId, text);
        Uri uri = GSengerContract.Messages.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertMessage(Message message) {
        ContentValues contentValues = ContentValueUtils.createMessage(message);
        Uri uri = GSengerContract.Messages.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertMedia(String commonTypeId, String type, String fileName,
                            String description, String path) {
        ContentValues contentValues = ContentValueUtils.createMedia(commonTypeId, type, fileName, description, path);
        Uri uri = GSengerContract.Medias.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

    public Uri insertMedia(Media media) {
        ContentValues contentValues = ContentValueUtils.createMedia(media);
        Uri uri = GSengerContract.Medias.CONTENT_URI;
        return context.getContentResolver().insert(uri, contentValues);
    }

}
