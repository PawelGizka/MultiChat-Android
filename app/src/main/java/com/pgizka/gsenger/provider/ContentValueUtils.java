package com.pgizka.gsenger.provider;

import android.content.ContentValues;

public class ContentValueUtils {

    public static ContentValues createCommonType(
            String type, long sendDate, int sent, String senderId, String chatId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.CommonTypes.TYPE, type);
        contentValues.put(GSengerContract.CommonTypes.SEND_DATE, sendDate);
        contentValues.put(GSengerContract.CommonTypes.SENT, sent);
        contentValues.put(GSengerContract.CommonTypes.SENDER_ID, senderId);
        contentValues.put(GSengerContract.CommonTypes.CHAT_ID, chatId);
        return contentValues;
    }

    public static ContentValues createFriend(
            int serverId, String userName, long addedDate, String status, long lastLoggedDate, String photoPath, String photoHash) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Friends.FRIEND_SERVER_ID, serverId);
        contentValues.put(GSengerContract.Friends.USER_NAME, userName);
        contentValues.put(GSengerContract.Friends.ADDED_DATE, addedDate);
        contentValues.put(GSengerContract.Friends.STATUS, status);
        contentValues.put(GSengerContract.Friends.LAST_LOGGED_DATE, lastLoggedDate);
        contentValues.put(GSengerContract.Friends.PHOTO, photoPath);
        contentValues.put(GSengerContract.Friends.PHOTO_HASH, photoHash);
        return contentValues;
    }

    public static ContentValues createToFriend(String friendId, String commonTypeId, long deliverDate, long viewedDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.ToFriends.TO_FRIEND_ID, friendId);
        contentValues.put(GSengerContract.ToFriends.COMMON_TYPE_ID, commonTypeId);
        contentValues.put(GSengerContract.ToFriends.DELIVERED_DATE, deliverDate);
        contentValues.put(GSengerContract.ToFriends.VIEWED_DATE, viewedDate);
        return contentValues;
    }

    public static ContentValues createChat(String type, String name, long startedDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Chats.TYPE, type);
        contentValues.put(GSengerContract.Chats.CHAT_NAME, name);
        contentValues.put(GSengerContract.Chats.STARTED_DATE, startedDate);
        return contentValues;
    }

    public static ContentValues createFriendHasChat(String friendId, String chatId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.FriendHasChats.FRIEND_ID, friendId);
        contentValues.put(GSengerContract.FriendHasChats.CHAT_ID, chatId);
        return contentValues;
    }

    public static ContentValues createMessage(String commonTypeId, String text) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Messages.COMMON_TYPE_ID, commonTypeId);
        contentValues.put(GSengerContract.Messages.TEXT, text);
        return contentValues;
    }

    public static ContentValues createMedia(
            String commonTypeId, String type, String fileName, String description, String path) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Medias.COMMON_TYPE_ID, commonTypeId);
        contentValues.put(GSengerContract.Medias.TYPE, type);
        contentValues.put(GSengerContract.Medias.FILE_NAME, fileName);
        contentValues.put(GSengerContract.Medias.DESCRIPTION, description);
        contentValues.put(GSengerContract.Medias.PATH, path);
        return contentValues;
    }

}
