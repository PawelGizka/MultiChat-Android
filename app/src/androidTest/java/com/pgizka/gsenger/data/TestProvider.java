package com.pgizka.gsenger.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.pgizka.gsenger.dagger2.DaggerSimpleComponent;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.ProviderUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestProvider {
    static final String TAG = TestProvider.class.getSimpleName();

    Context context;
    ProviderUtils providerUtils;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getContext();
        cleanDB();
        if(providerUtils == null) {
            providerUtils = new ProviderUtils(context);
        }
    }

    private void cleanDB(){
        context.getContentResolver().delete(GSengerContract.CommonTypes.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Friends.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.ToFriends.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Chats.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.FriendHasChats.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Messages.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Medias.CONTENT_URI, null, null);
    }

    @Test
    public void testCommonType() throws Exception{
        Uri uri = providerUtils.insertCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE, 123, 0, "0", "0");

        uri = GSengerContract.CommonTypes.buildCommonTypeUri(uri.getLastPathSegment());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE, cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE)));

        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.CommonTypes.TYPE, GSengerContract.CommonTypes.COMMON_TYPE_MEDIA);
        int numberOfRowsUpdate = context.getContentResolver().update(uri, contentValues, null, null);
        assertEquals(1, numberOfRowsUpdate);

        cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(GSengerContract.CommonTypes.COMMON_TYPE_MEDIA, cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE)));

        int numberOfRowsDeleted = context.getContentResolver().delete(uri, null, null);
        assertEquals(1, numberOfRowsDeleted);
    }

    @Test
    public void testFriends() throws Exception {
        Uri uri = providerUtils.insertFriend("pawel", 123, "status", 0, "photoPath", "photoHash");

        uri = GSengerContract.Friends.buildFriendUri(uri.getLastPathSegment());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("pawel", cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));

        //test update
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Friends.USER_NAME, "asia");
        int numberOfRowsUpdate = context.getContentResolver().update(uri, contentValues, null, null);
        assertEquals(1, numberOfRowsUpdate);

        cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("asia", cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));

        //test delete
        int numberOfRowsDeleted = context.getContentResolver().delete(uri, null, null);
        assertEquals(1, numberOfRowsDeleted);
    }

    @Test
    public void testToFriends() throws Exception {
        Uri commonTypeUri = providerUtils.insertCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE, 123, 0, "0", "0");
        Uri friendUri = providerUtils.insertFriend("pawel", 123, "status", 0, "photoPath", "photoHash");

        String friendId = friendUri.getLastPathSegment();
        String commonTypeId = commonTypeUri.getLastPathSegment();

        Uri toFriendUri = providerUtils.insertToFriend(friendId, commonTypeId, 123, 0);

        String newFriendId = toFriendUri.getPathSegments().get(1);
        String newCommonTypeId = toFriendUri.getPathSegments().get(2);
        assertEquals(friendId, newFriendId);
        assertEquals(commonTypeId, newCommonTypeId);

        toFriendUri = GSengerContract.ToFriends.buildToFriendUri(friendId, commonTypeId);
        Cursor cursor = context.getContentResolver().query(toFriendUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(123, cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.DELIVERED_DATE)));

        //test update
        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.ToFriends.DELIVERED_DATE, 12345);
        int numberOfUpdatedRecords = context.getContentResolver().update(toFriendUri, contentValues, null, null);
        assertEquals(1, numberOfUpdatedRecords);

        cursor = context.getContentResolver().query(toFriendUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(12345, cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.DELIVERED_DATE)));

        //test delete
        int numberOfDeletedRecords = context.getContentResolver().delete(toFriendUri, null, null);
        assertEquals(1, numberOfDeletedRecords);
    }

    @Test
    public void testChats() throws Exception {
        Uri chatUri = providerUtils.insertChat(GSengerContract.Chats.CHAT_TYPE_CONVERSATION, "someChat", 123);

        Cursor cursor = context.getContentResolver().query(chatUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(123, cursor.getLong(cursor.getColumnIndex(GSengerContract.Chats.STARTED_DATE)));

        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Chats.STARTED_DATE, 12345);
        int numberOfUpdateRecords = context.getContentResolver().update(chatUri, contentValues, null, null);
        assertEquals(1, numberOfUpdateRecords);

        cursor = context.getContentResolver().query(chatUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(12345, cursor.getLong(cursor.getColumnIndex(GSengerContract.Chats.STARTED_DATE)));

        int numberOfDelatedRecords = context.getContentResolver().delete(chatUri, null, null);
        assertEquals(1, numberOfDelatedRecords);
    }

    @Test
    public void testFriendHasChat() throws Exception {
        Uri friendUri = providerUtils.insertFriend("pawel", 123, "status", 0, "photoPath", "photoHash");
        Uri chatUri = providerUtils.insertChat(GSengerContract.Chats.CHAT_TYPE_CONVERSATION, "someChat", 123);

        String friendId = friendUri.getLastPathSegment();
        String chatId = chatUri.getLastPathSegment();

        Uri friendHasChatUri = providerUtils.insertFriendHasChat(friendId, chatId);
        assertEquals(friendId, friendHasChatUri.getPathSegments().get(1));
        assertEquals(chatId, friendHasChatUri.getPathSegments().get(2));

        Cursor cursor = context.getContentResolver().query(friendHasChatUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getCount());

        int numberOfDeletedRecords = context.getContentResolver().delete(friendHasChatUri, null, null);
        assertEquals(1, numberOfDeletedRecords);
    }

    @Test
    public void testMessages() throws Exception {
        Uri commonTypeUri = providerUtils.insertCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE, 123, 0, "0", "0");

        String commTypeId = commonTypeUri.getLastPathSegment();
        Uri messageUri = providerUtils.insertMessage(commTypeId, "message");

        Cursor cursor = context.getContentResolver().query(messageUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("message", cursor.getString(cursor.getColumnIndex(GSengerContract.Messages.TEXT)));

        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Messages.TEXT, "anotherMessage");
        int numberOfUpdatedRecords = context.getContentResolver().update(messageUri, contentValues, null, null);
        assertEquals(1, numberOfUpdatedRecords);

        cursor = context.getContentResolver().query(messageUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("anotherMessage", cursor.getString(cursor.getColumnIndex(GSengerContract.Messages.TEXT)));

        int numberOfDeletedRecords = context.getContentResolver().delete(messageUri, null, null);
        assertEquals(1, numberOfDeletedRecords);
    }

    @Test
    public void testMedias() throws Exception {
        Uri commonTypeUri = providerUtils.insertCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MEDIA, 123, 0, "0", "0");

        String commTypeId = commonTypeUri.getLastPathSegment();
        Uri mediasUri = providerUtils.insertMedia(commTypeId, GSengerContract.Medias.MEDIA_TYPE_FILE, "fileName", "description", "path");

        Cursor cursor = context.getContentResolver().query(mediasUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("fileName", cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.FILE_NAME)));

        ContentValues contentValues = new ContentValues();
        contentValues.put(GSengerContract.Medias.FILE_NAME, "anotherFileName");
        int numberOfUpdatedRecords = context.getContentResolver().update(mediasUri, contentValues, null, null);
        assertEquals(1, numberOfUpdatedRecords);

        cursor = context.getContentResolver().query(mediasUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("anotherFileName", cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.FILE_NAME)));

        int numberOfDeletedRecords = context.getContentResolver().delete(mediasUri, null, null);
        assertEquals(1, numberOfDeletedRecords);
    }

    @Test
    public void testCommonTypeJoinToFriends() throws Exception {
        Uri commonTypeUri = providerUtils.insertCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE, 123, 0, "0", "0");
        Uri friendUri = providerUtils.insertFriend("pawel", 123, "status", 0, "photoPath", "photoHash");
        Uri toFriendsUri = providerUtils.insertToFriend(friendUri.getLastPathSegment(), commonTypeUri.getLastPathSegment(), 12345, 0);

        Cursor cursor = context.getContentResolver().query(toFriendsUri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(12345, cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.DELIVERED_DATE)));

        //finally test join
        Uri uri = GSengerContract.CommonTypes.buildCommonTypeWithFriendsUri(commonTypeUri.getLastPathSegment());
        cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE,
                cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE)));
        assertEquals(12345, cursor.getLong(cursor.getColumnIndex(GSengerContract.ToFriends.DELIVERED_DATE)));
        assertEquals("pawel", cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));
    }

    @Test
    public void testFriendsJoinChats() throws Exception {
        Uri friendUri = providerUtils.insertFriend("pawel", 123, "status", 0, "photoPath", "photoHash");
        Uri chatUri = providerUtils.insertChat(GSengerContract.Chats.CHAT_TYPE_CONVERSATION, "someChat", 123);

        String friendId = friendUri.getLastPathSegment();
        String chatId = chatUri.getLastPathSegment();

        Uri friendHasChatUri = providerUtils.insertFriendHasChat(friendId, chatId);
        assertEquals(friendId, friendHasChatUri.getPathSegments().get(1));
        assertEquals(chatId, friendHasChatUri.getPathSegments().get(2));

        //test join
        Uri uri = GSengerContract.Friends.buildFriendWithChatsUri(friendId);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());

        assertEquals("pawel", cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));
        assertEquals("someChat", cursor.getString(cursor.getColumnIndex(GSengerContract.Chats.CHAT_NAME)));

        chatUri = providerUtils.insertChat(GSengerContract.Chats.CHAT_TYPE_CONVERSATION, "chat2", 123);

        friendId = friendUri.getLastPathSegment();
        chatId = chatUri.getLastPathSegment();
        friendHasChatUri = providerUtils.insertFriendHasChat(friendId, chatId);

        //test join
        uri = GSengerContract.Friends.buildFriendWithChatsUri(friendId);
        cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(2, cursor.getCount());
    }

    @Test
    public void testChatsJoinFriends() throws Exception {
        Uri friendUri = providerUtils.insertFriend("pawel", 123, "status", 0, "photoPath", "photoHash");
        Uri chatUri = providerUtils.insertChat(GSengerContract.Chats.CHAT_TYPE_CONVERSATION, "someChat", 123);

        String friendId = friendUri.getLastPathSegment();
        String chatId = chatUri.getLastPathSegment();

        Uri friendHasChatUri = providerUtils.insertFriendHasChat(friendId, chatId);
        assertEquals(friendId, friendHasChatUri.getPathSegments().get(1));
        assertEquals(chatId, friendHasChatUri.getPathSegments().get(2));

        //test join
        Uri uri = GSengerContract.Chats.buildChatWithFriendsUri(chatId);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("pawel", cursor.getString(cursor.getColumnIndex(GSengerContract.Friends.USER_NAME)));
        assertEquals("someChat", cursor.getString(cursor.getColumnIndex(GSengerContract.Chats.CHAT_NAME)));

        //insert another friend
        friendUri = providerUtils.insertFriend("pawe2l", 12345, "status2", 0, "photoPath", "photoHash");

        friendId = friendUri.getLastPathSegment();
        chatId = chatUri.getLastPathSegment();

        friendHasChatUri = providerUtils.insertFriendHasChat(friendId, chatId);
        assertEquals(friendId, friendHasChatUri.getPathSegments().get(1));
        assertEquals(chatId, friendHasChatUri.getPathSegments().get(2));

        //test join
        uri = GSengerContract.Chats.buildChatWithFriendsUri(chatId);
        cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(2, cursor.getCount());
    }

    @Test
    public void testChatConversation() throws Exception {
        String chatId = prepareCompleteChatsToTest();

        Uri uri = GSengerContract.Chats.buildChatConversationUri(chatId);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        assertTrue(cursor.moveToFirst());
        assertEquals(2, cursor.getCount());
    }

    @Test
    public void testChatsToDisplay() throws Exception {
        //prepare two chats
        prepareCompleteChatsToTest();
        prepareCompleteChatsToTest();

        Uri uri = GSengerContract.Chats.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals(2, cursor.getCount());

        uri = GSengerContract.Chats.buildChatsToDisplayUri();
        cursor = context.getContentResolver().query(uri, null, null, null, null);

        assertTrue(cursor.moveToFirst());
        assertEquals(2, cursor.getCount());
        assertEquals(GSengerContract.CommonTypes.COMMON_TYPE_MEDIA, cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE)));
    }

    private String prepareCompleteChatsToTest() {
        Uri chatUri = providerUtils.insertChat(GSengerContract.Chats.CHAT_TYPE_GROUP, "someChat", 123);
        String chatId = chatUri.getLastPathSegment();

        Uri friendUri = providerUtils.insertFriend("pawel", 0, "status", 0, "photoPath", "photoHash");
        String friendId = friendUri.getLastPathSegment();

        Uri friendUri2 = providerUtils.insertFriend("asia", 0, "status", 0, "photoPath", "photoHash");
        String friendId2 = friendUri2.getLastPathSegment();

        Uri friendUri3 = providerUtils.insertFriend("marcin", 0, "status", 0, "photoPath", "photoHash");
        String friendId3 = friendUri3.getLastPathSegment();

        Uri friendUri4 = providerUtils.insertFriend("sochaj", 0, "status", 0, "photoPath", "photoHash");
        String friendId4 = friendUri4.getLastPathSegment();

        providerUtils.insertFriendHasChat(friendId, chatId);
        providerUtils.insertFriendHasChat(friendId2, chatId);
        providerUtils.insertFriendHasChat(friendId3, chatId);
        providerUtils.insertFriendHasChat(friendId4, chatId);

        Uri commonTypeUri = providerUtils.insertCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE, 123, 0, friendId, chatId);
        String commonTypeId = commonTypeUri.getLastPathSegment();

        Uri commonTypeUri2 = providerUtils.insertCommonType(GSengerContract.CommonTypes.COMMON_TYPE_MEDIA, 123, 0, friendId, chatId);
        String commonTypeId2 = commonTypeUri2.getLastPathSegment();

        providerUtils.insertToFriend(friendId2, commonTypeId, 123, 0);
        providerUtils.insertToFriend(friendId3, commonTypeId, 0, 0);
        providerUtils.insertToFriend(friendId4, commonTypeId, 12345, 0);

        providerUtils.insertToFriend(friendId2, commonTypeId2, 123, 0);
        providerUtils.insertToFriend(friendId3, commonTypeId2, 0, 0);
        providerUtils.insertToFriend(friendId4, commonTypeId2, 12345, 0);

        providerUtils.insertMessage(commonTypeId, "messageText");
        providerUtils.insertMedia(commonTypeId2, GSengerContract.Medias.MEDIA_TYPE_FILE, "fileName", "description", "path");

        return chatId;

    }

}
