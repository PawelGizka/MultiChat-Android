package com.pgizka.gsenger.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.pgizka.gsenger.provider.GSengerDatabase.*;

import java.util.Arrays;

public class GSengerProvider extends ContentProvider {
    private static final String TAG = GSengerProvider.class.getSimpleName();

    private GSengerDatabase database;

    private GSengerProviderUriMatcher uriMatcher;

    public GSengerProvider() {
    }

    @Override
    public boolean onCreate() {
        database = new GSengerDatabase(getContext());
        uriMatcher = new GSengerProviderUriMatcher();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        GSengerUriEnum matchingUriEnum = uriMatcher.matchUri(uri);
        return matchingUriEnum.contentType;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        GSengerUriEnum matchingUriEnum = uriMatcher.matchUri(uri);

        Log.d(TAG, "uri = " + uri + " code = " + matchingUriEnum.code + " proj = " +
                Arrays.toString(projection) + " selection = " + selection + " args = "
                + Arrays.toString(selectionArgs) + " order = " + sortOrder);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (matchingUriEnum){

            case COMMON_TYPES: {
                queryBuilder.setTables(Tables.COMMON_TYPES);
                break;
            }
            case COMMON_TYPE: {
                queryBuilder.setTables(Tables.COMMON_TYPES);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.COMMON_TYPES + "." + GSengerContract.CommonTypes._ID + " = " + id);
                break;
            }
            case COMMON_TYPE_WITH_FRIENDS: {
                queryBuilder.setTables(Tables.COMMON_TYPES_JOIN_FRIENDS);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.COMMON_TYPES + "." + GSengerContract.CommonTypes._ID + " = " + id);
                break;
            }
            case FRIENDS: {
                queryBuilder.setTables(Tables.FRIENDS);
                break;
            }
            case FRIEND: {
                queryBuilder.setTables(Tables.FRIENDS);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.FRIENDS + "." + GSengerContract.Friends._ID + " = " + id);
                break;
            }
            case FRIEND_WITH_CHATS: {
                queryBuilder.setTables(Tables.FRIENDS_JOIN_CHATS);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.FRIENDS + "." + GSengerContract.Friends._ID + " = " + id);
                break;
            }
            case FRIEND_WITH_CHAT: {
                queryBuilder.setTables(Tables.FRIENDS_JOIN_CHATS);
                String friendId = uri.getPathSegments().get(1);
                String chatId = uri.getPathSegments().get(2);
                queryBuilder.appendWhere(
                        Tables.FRIEND_HAS_CHAT + "." + GSengerContract.FriendHasChatColumns.FRIEND_ID + " = " + friendId);
                queryBuilder.appendWhere(" AND ");
                queryBuilder.appendWhere(
                        Tables.FRIEND_HAS_CHAT + "." + GSengerContract.FriendHasChatColumns.CHAT_ID + " = " + chatId);
                break;
            }
            case TO_FRIENDS: {
                queryBuilder.setTables(Tables.TO_FRIENDS);
                break;
            }
            case TO_FRIEND: {
                queryBuilder.setTables(Tables.TO_FRIENDS);
                String friendId = uri.getPathSegments().get(1);
                String commonTypeId = uri.getPathSegments().get(2);
                queryBuilder.appendWhere(
                        Tables.TO_FRIENDS + "." + GSengerContract.ToFriends.TO_FRIEND_ID + " = " + friendId);
                queryBuilder.appendWhere(" AND ");
                queryBuilder.appendWhere(
                        Tables.TO_FRIENDS + "." + GSengerContract.ToFriends.COMMON_TYPE_ID + " = " + commonTypeId);
                break;
            }
            case CHATS: {
                queryBuilder.setTables(Tables.CHATS);
                break;
            }
            case CHAT: {
                queryBuilder.setTables(Tables.CHATS);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.CHATS + "." + GSengerContract.Chats._ID + " = " + id);
                break;
            }
            case CHAT_WITH_FRIENDS: {
                queryBuilder.setTables(Tables.CHATS_JOIN_FRIENDS);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.CHATS + "." + GSengerContract.Chats._ID + " = " + id);
                break;
            }
            case CHAT_CONVERSATION: {
                queryBuilder.setTables(Tables.CHAT_CONVERSATION);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.COMMON_TYPES + "." + GSengerContract.CommonTypes.CHAT_ID + " = " + id);
                break;
            }
            case CHAT_GROUP: {
                queryBuilder.setTables(Tables.CHAT_CONVERSATION);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.COMMON_TYPES + "." + GSengerContract.CommonTypes.CHAT_ID + " = " + id);
                break;
            }
            case CHATS_TO_DISPLAY: {
                queryBuilder.setTables(Tables.CHATS_TO_DISPLAY);
                break;
            }
            case FRIENDS_HAVE_CHATS: {
                queryBuilder.setTables(Tables.FRIEND_HAS_CHAT);
                break;
            }
            case FRIEND_HAS_CHATS: {
                queryBuilder.setTables(Tables.FRIEND_HAS_CHAT);
                String friendId = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.FRIEND_HAS_CHAT + "." + GSengerContract.FriendHasChats.FRIEND_ID + " = " + friendId);
                break;
            }
            case FRIEND_HAS_CHAT: {
                queryBuilder.setTables(Tables.FRIEND_HAS_CHAT);
                String friendId = uri.getPathSegments().get(1);
                String chatId = uri.getPathSegments().get(2);
                queryBuilder.appendWhere(
                        Tables.FRIEND_HAS_CHAT + "." + GSengerContract.FriendHasChats.FRIEND_ID + " = " + friendId);
                queryBuilder.appendWhere(" AND ");
                queryBuilder.appendWhere(
                        Tables.FRIEND_HAS_CHAT + "." + GSengerContract.FriendHasChats.CHAT_ID + " = " + chatId);
                break;
            }
            case MESSAGES: {
                queryBuilder.setTables(Tables.MESSAGES);
                break;
            }
            case MESSAGE: {
                queryBuilder.setTables(Tables.MESSAGES);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.MESSAGES + "." + GSengerContract.Messages.COMMON_TYPE_ID + " = " + id);
                break;
            }
            case MEDIAS: {
                queryBuilder.setTables(Tables.MEDIAS);
                break;
            }
            case MEDIA: {
                queryBuilder.setTables(Tables.MEDIAS);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(
                        Tables.MEDIAS + "." + GSengerContract.Medias.COMMON_TYPE_ID + " = " + id);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        Cursor cursor =  queryBuilder.query(database.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);

        Log.d(TAG, "queried: " + cursor.getCount() + " records");

        Context context = getContext();
        if (null != context) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert (uri=" + uri + ", values=" + values.toString());
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        GSengerUriEnum matchingUriEnum = uriMatcher.matchUri(uri);
        long insertedId = 0;
        if(matchingUriEnum.table != null) {
            insertedId = sqLiteDatabase.insertWithOnConflict(matchingUriEnum.table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        switch (matchingUriEnum) {
            case COMMON_TYPES: {
                return GSengerContract.CommonTypes.buildCommonTypeUri(String.valueOf(insertedId));
            }
            case FRIENDS: {
                return GSengerContract.Friends.buildFriendUri(String.valueOf(insertedId));
            }
            case TO_FRIENDS: {
                String friendId= values.getAsString(GSengerContract.ToFriends.TO_FRIEND_ID);
                String commonTypeId= values.getAsString(GSengerContract.ToFriends.COMMON_TYPE_ID);
                return GSengerContract.ToFriends.buildToFriendUri(friendId, commonTypeId);
            }
            case CHATS: {
                return GSengerContract.Chats.buildChatUri(String.valueOf(insertedId));
            }
            case FRIENDS_HAVE_CHATS: {
                String friendId= values.getAsString(GSengerContract.FriendHasChats.FRIEND_ID);
                String chatId = values.getAsString(GSengerContract.FriendHasChats.CHAT_ID);
                return GSengerContract.FriendHasChats.buildFriendHasChatUri(friendId, chatId);
            }
            case MESSAGES: {
                String commonTypeId = values.getAsString(GSengerContract.Messages.COMMON_TYPE_ID);
                return GSengerContract.Messages.buildMessageUri(String.valueOf(commonTypeId));
            }
            case MEDIAS: {
                String commonTypeId = values.getAsString(GSengerContract.Medias.COMMON_TYPE_ID);
                return GSengerContract.Medias.buildMediaUri(commonTypeId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update (uri = " + uri + ", values = " + values.toString() +
                " selection = " + selection + " selectionArgs = " + selectionArgs);

        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        GSengerUriEnum matchingUriEnum = uriMatcher.matchUri(uri);

        String simpleSelection = buildSimpleSelection(uri, selection);

        int updatedRecords = sqLiteDatabase.update(matchingUriEnum.table, values, simpleSelection, selectionArgs);

        Log.d(TAG, "updated: " + updatedRecords + " records");
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRecords;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete (uri = " + uri + ", values = " +
                " selection = " + selection + " selectionArgs = " + selectionArgs);

        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        GSengerUriEnum matchingUriEnum = uriMatcher.matchUri(uri);

        String simpleSelection = buildSimpleSelection(uri, selection);

        int deletedRecords = sqLiteDatabase.delete(matchingUriEnum.table, simpleSelection, selectionArgs);

        Log.d(TAG, "deleted: " + deletedRecords + " records");
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRecords;
    }

    public String buildSimpleSelection(Uri uri, String selection){
        GSengerUriEnum matchingUriEnum = uriMatcher.matchUri(uri);
        String whereClause = null;

        switch (matchingUriEnum) {
            case COMMON_TYPES:
            case FRIENDS:
            case TO_FRIENDS:
            case CHATS:
            case FRIENDS_HAVE_CHATS:
            case MESSAGES:
            case MEDIAS:{
                //do nothing
                break;
            }
            case COMMON_TYPE:
            case FRIEND:
            case CHAT: {
                String commonTypeId = uri.getLastPathSegment();
                whereClause = BaseColumns._ID + " = " + commonTypeId;
                break;
            }
            case TO_FRIEND: {
                String toFriendId = uri.getPathSegments().get(1);
                String commonTypeId = uri.getPathSegments().get(2);
                whereClause = GSengerContract.ToFriends.TO_FRIEND_ID + " = " + toFriendId +
                        " AND " + GSengerContract.ToFriends.COMMON_TYPE_ID + " = " + commonTypeId;
                break;
            }
            case FRIEND_HAS_CHAT: {
                String friendId = uri.getPathSegments().get(1);
                String chatId = uri.getPathSegments().get(2);
                whereClause = GSengerContract.FriendHasChats.FRIEND_ID + " = " + friendId +
                        " AND " + GSengerContract.FriendHasChats.CHAT_ID + " = " + chatId;
                break;
            }
            case MESSAGE: {
                String commonTypeId = uri.getLastPathSegment();
                whereClause = GSengerContract.Messages.COMMON_TYPE_ID + " = " + commonTypeId;
                break;
            }
            case MEDIA: {
                String commonTypeId = uri.getLastPathSegment();
                whereClause = GSengerContract.Medias.COMMON_TYPE_ID + " = " + commonTypeId;
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
            }
        }

        if(TextUtils.isEmpty(selection)){
            if(!TextUtils.isEmpty(whereClause)){
                selection = whereClause;
            }
        } else {
            if(!TextUtils.isEmpty(whereClause)) {
                selection = selection + " AND " + whereClause;
            }
        }

        return selection;
    }

}
