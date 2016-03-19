package com.pgizka.gsenger.provider.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.Chat;
import com.pgizka.gsenger.provider.pojos.Media;

public class ChatRepository {

    private Context context;
    private ProviderUtils providerUtils;

    public ChatRepository(Context context, ProviderUtils providerUtils) {
        this.context = context;
        this.providerUtils = providerUtils;
    }

    public int insertChat(Chat chat) {
        Uri uri = providerUtils.insertChat(chat);
        chat.setId(Integer.parseInt(uri.getLastPathSegment()));
        return chat.getId();
    }

    public void updateChat(Chat chat) {
        String id = String.valueOf(chat.getId());
        Uri uri = GSengerContract.Chats.buildChatUri(id);
        ContentValues contentValues = ContentValueUtils.createChat(chat);
        context.getContentResolver().update(uri, contentValues, null, null);
    }

    public Chat getChatById(int id) {
        Uri uri = GSengerContract.Chats.buildChatUri(String.valueOf(id));
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        Chat chat = null;
        if (cursor.moveToFirst()) {
            chat = buildChat(cursor);
        }

        return chat;
    }

    public Chat getConversationChatByFriendId(int friendId) {
        Uri uri = GSengerContract.Friends.buildFriendWithChatsUri(String.valueOf(friendId));
        String selection = GSengerContract.Chats.TYPE + "=?";
        String[] args = new String[]{GSengerContract.Chats.CHAT_TYPE_CONVERSATION};

        Cursor cursor = context.getContentResolver().query(uri, null, selection, args, null);

        Chat chat = null;
        if (cursor.moveToFirst()) {
            chat = buildChat(cursor);
        }

        return chat;
    }

    public Chat buildChat(Cursor cursor) {
        Chat chat = new Chat();

        chat.setId(cursor.getInt(cursor.getColumnIndex(GSengerContract.Chats._ID)));
        chat.setServerId(cursor.getInt(cursor.getColumnIndex(GSengerContract.Chats.CHAT_SERVER_ID)));
        chat.setStartedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.Chats.STARTED_DATE)));
        chat.setChatName(cursor.getString(cursor.getColumnIndex(GSengerContract.Chats.CHAT_NAME)));
        chat.setType(cursor.getString(cursor.getColumnIndex(GSengerContract.Chats.TYPE)));

        return chat;
    }

}
