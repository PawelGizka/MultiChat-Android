package com.pgizka.gsenger.mainView.chats;

import android.database.Cursor;

import com.pgizka.gsenger.provider.GSengerContract;

import java.util.ArrayList;
import java.util.List;

public class ChatsToDisplayModel {

    List<ChatToDisplay> chatToDisplays;

    public void readDataFromCursor(Cursor cursor){
        chatToDisplays = new ArrayList<>();

        if(!cursor.moveToFirst()){
            return;
        }

        do {
            chatToDisplays.add(makeChat(cursor));
        } while (cursor.moveToNext());
    }

    private ChatToDisplay makeChat(Cursor cursor) {
        ChatToDisplay chatToDisplay = new ChatToDisplay();

        chatToDisplay.setChatId(cursor.getInt(cursor.getColumnIndex(GSengerContract.Chats._ID)));
        chatToDisplay.setChatStartedData(cursor.getLong(cursor.getColumnIndex(GSengerContract.Chats.STARTED_DATE)));
        chatToDisplay.setChatName(cursor.getString(cursor.getColumnIndex(GSengerContract.Chats.CHAT_NAME)));
        chatToDisplay.setChatType(cursor.getString(cursor.getColumnIndex(GSengerContract.Chats.TYPE)));
        chatToDisplay.setFriendId(cursor.getLong(cursor.getColumnIndex(GSengerContract.Friends._ID)));
        chatToDisplay.setFriendUserName(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));
        chatToDisplay.setFriendPhotoPath(cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.PHOTO)));
        chatToDisplay.setCommonTypeType(cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE)));
        chatToDisplay.setCommonTypeSendDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.CommonTypes.SEND_DATE)));
        chatToDisplay.setMessageText(cursor.getString(cursor.getColumnIndex(GSengerContract.Messages.TEXT)));
        chatToDisplay.setMediaType(cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.TYPE)));
        chatToDisplay.setMediaFileName(cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.FILE_NAME)));
        chatToDisplay.setMediaDescription(cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.DESCRIPTION)));
        chatToDisplay.setToFriendDeliveredDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.DELIVERED_DATE)));
        chatToDisplay.setToFriendViewedDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.VIEWED_DATE)));

        return chatToDisplay;
    }


    public List<ChatToDisplay> getChatToDisplays() {
        return chatToDisplays;
    }

    public void setChatToDisplays(List<ChatToDisplay> chatToDisplays) {
        this.chatToDisplays = chatToDisplays;
    }
}
