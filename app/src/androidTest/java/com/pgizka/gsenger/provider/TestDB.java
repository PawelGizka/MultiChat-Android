package com.pgizka.gsenger.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.test.AndroidTestCase;

import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.GSengerDatabase;
import com.pgizka.gsenger.provider.ContentValueUtils;

public class TestDB extends AndroidTestCase{
    GSengerDatabase gSengerDatabase;
    SQLiteDatabase database;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cleanDB();
        createDB();
    }

    private void createDB(){
        gSengerDatabase = new GSengerDatabase(getContext());
        database = gSengerDatabase.getWritableDatabase();
    }

    private void cleanDB(){
        mContext.deleteDatabase(GSengerDatabase.DATABASE_NAME);
    }

    public void testCreateDB() throws Throwable{
        assertTrue(database.isOpen());
    }

    public void testCommonType() throws Throwable{
        long id = insertNewCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE);

        Cursor cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.COMMON_TYPES, null);
        assertTrue(cursor.moveToFirst());
        String type = cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE));
        assertEquals(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE, type);

        updateCommonType(id);
        cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.COMMON_TYPES, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(GSengerContract.CommonTypes.COMMON_TYPE_MEDIA, cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE)));
    }

    private long insertNewCommonType(String type){
        ContentValues contentValues = ContentValueUtils.createCommonType(type, true, 123, 0, "0", "0");

        long id = database.insert(GSengerDatabase.Tables.COMMON_TYPES, null, contentValues);
        assertNotSame(-1, id);
        return id;
    }

    private void updateCommonType(long id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.CommonTypes.TYPE, GSengerContract.CommonTypes.COMMON_TYPE_MEDIA);
        int numberOfRows = database.update(
                GSengerDatabase.Tables.COMMON_TYPES, contentValues,
                BaseColumns._ID + "=?", new String[]{Long.toString(id)});
        assertEquals(1, numberOfRows);
    }

    public void testFriends() throws Throwable{
        long id = insertNewFriend();

        Cursor cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.FRIENDS, null);
        assertTrue(cursor.moveToFirst());

        String userName = cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME));
        assertEquals("pawel", userName);

        updateFriend(id);

        cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.FRIENDS, null);
        assertTrue(cursor.moveToFirst());

        userName = cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME));
        assertEquals("maciek", userName);
    }

    private long insertNewFriend(){
        ContentValues contentValues = ContentValueUtils.createFriend(0, "pawel", 123, "status", 12345, "photoPath", "photoHash");

        long id = database.insert(GSengerDatabase.Tables.FRIENDS, null, contentValues);
        assertNotSame(-1, id);
        return id;
    }

    private void updateFriend(long id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Friends.USER_NAME, "maciek");
        int rowsAffected = database.update(GSengerDatabase.Tables.FRIENDS, contentValues,
                BaseColumns._ID + "=?", new String[]{Long.toString(id)});

        assertEquals(1, rowsAffected);
    }

    public void testToFriends() throws Throwable {
        long commonTypeId = insertNewCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE);
        long friendId = insertNewFriend();

        long deliveredDate = 123;
        ContentValues contentValues = ContentValueUtils.createToFriend(String.valueOf(friendId), String.valueOf(commonTypeId), deliveredDate, 0);

        long toFriendId = database.insert(GSengerDatabase.Tables.TO_FRIENDS, null, contentValues);
        assertNotSame(-1, toFriendId);

        Cursor cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.TO_FRIENDS, null);
        assertTrue(cursor.moveToFirst());

        long returnedDeliveredDate = cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.DELIVERED_DATE));
        assertEquals(deliveredDate, returnedDeliveredDate);
    }

    public void testChats() throws Throwable {
        insertChat(GSengerContract.Chats.CHAT_TYPE_CONVERSATION);

        Cursor cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.CHATS, null);
        assertTrue(cursor.moveToFirst());

        String returnedType = cursor.getString(cursor.getColumnIndex(GSengerContract.Chats.TYPE));
        assertEquals(GSengerContract.Chats.CHAT_TYPE_CONVERSATION, returnedType);
    }

    private long insertChat(String chatType) throws Throwable {
        ContentValues contentValues = ContentValueUtils.createChat(chatType, "chat", 123);

        long chatId = database.insert(GSengerDatabase.Tables.CHATS, null, contentValues);
        assertNotSame(-1, chatId);
        return chatId;
    }

    public void testFriendHasChat() throws Throwable {
        long friendId = insertNewFriend();
        long chatId = insertChat(GSengerContract.Chats.CHAT_TYPE_CONVERSATION);

        ContentValues contentValues = ContentValueUtils.createFriendHasChat(String.valueOf(friendId), String.valueOf(chatId));

        long id = database.insert(GSengerDatabase.Tables.FRIEND_HAS_CHAT, null, contentValues);
        assertNotSame(-1, id);
    }

    public void testMessages() throws Throwable {
        long commonTypeId = insertNewCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE);

        String messageText = "jakistext";
        ContentValues contentValues = ContentValueUtils.createMessage(String.valueOf(commonTypeId), messageText);

        long id = database.insert(GSengerDatabase.Tables.MESSAGES, null, contentValues);
        assertNotSame(-1, id);

        Cursor cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.MESSAGES, null);
        assertTrue(cursor.moveToFirst());

        String returnedMessage = cursor.getString(cursor.getColumnIndex(GSengerContract.Messages.TEXT));
        assertEquals(messageText, returnedMessage);
    }

    public void testMedias() throws Throwable {
        long commonTypeId = insertNewCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MEDIA);

        int type = GSengerContract.Medias.MEDIA_TYPE_PHOTO;
        String description = "some description";
        String fileName = "some file name";
        String path = "path";
        ContentValues contentValues = ContentValueUtils.createMedia(String.valueOf(commonTypeId), type, fileName, description, path);

        long id = database.insert(GSengerDatabase.Tables.MEDIAS, null, contentValues);
        assertNotSame(-1, id);

        Cursor cursor = database.rawQuery("SELECT * FROM " + GSengerDatabase.Tables.MEDIAS, null);
        assertTrue(cursor.moveToFirst());

        int returnedType = cursor.getInt(cursor.getColumnIndex(GSengerContract.Medias.TYPE));
        String returnedDescription = cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.DESCRIPTION));
        String returnedFileName = cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.FILE_NAME));

        assertEquals(type, returnedType);
        assertEquals(description, returnedDescription);
        assertEquals(fileName, returnedFileName);
    }

}
