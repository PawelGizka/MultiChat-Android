package com.pgizka.gsenger.provider;

import com.pgizka.gsenger.provider.GSengerContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class GSengerDatabase extends SQLiteOpenHelper {
    private static final String TAG = GSengerDatabase.class.getSimpleName();

    public static final String DATABASE_NAME = "gsenger.db";
    public static final int CUR_DATABASE_VERSION = 2;

    private final Context context;

    public interface Tables {
        String COMMON_TYPES = "common_types";
        String FRIENDS = "friends";
        String TO_FRIENDS = "to_friends";
        String CHATS = "chats";
        String FRIEND_HAS_CHAT = "friend_has_chat";
        String MESSAGES = "messages";
        String MEDIAS = "medias";

        String COMMON_TYPES_JOIN_FRIENDS = COMMON_TYPES +
                " INNER JOIN " + TO_FRIENDS +
                    " ON " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + TO_FRIENDS + "." + ToFriends.COMMON_TYPE_ID +
                " INNER JOIN " + FRIENDS +
                    " ON " + TO_FRIENDS + "." + ToFriends.TO_FRIEND_ID +
                    " = " + FRIENDS + "." + Friends._ID;

        String FRIENDS_JOIN_CHATS = FRIENDS +
                " INNER JOIN " + FRIEND_HAS_CHAT +
                    " ON " + FRIENDS + "." + Friends._ID +
                    " = " + FRIEND_HAS_CHAT + "." + FriendHasChats.FRIEND_ID +
                " INNER JOIN " + CHATS +
                    " ON " + FRIEND_HAS_CHAT + "." + FriendHasChats.CHAT_ID +
                    " = " + CHATS + "." + Chats._ID;

        String CHATS_JOIN_FRIENDS = CHATS +
                " INNER JOIN " + FRIEND_HAS_CHAT +
                    " ON " + CHATS + "." + Chats._ID +
                    " = " + FRIEND_HAS_CHAT + "." + FriendHasChats.CHAT_ID +
                " INNER JOIN " + FRIENDS +
                    " ON " + FRIEND_HAS_CHAT + "." + FriendHasChats.FRIEND_ID +
                    " = " + FRIENDS + "." + Friends._ID;

        //this subquery is correlated, so do not try to use it as standalone
        String SELECT_LAST_TO_FRIEND_SUBQUERY =
                "(SELECT " + TO_FRIENDS + "." + ToFriends.TO_FRIEND_ID +
                " FROM " + TO_FRIENDS +
                " WHERE " + COMMON_TYPES + "." + CommonTypes._ID +
                " = " + TO_FRIENDS + "." + ToFriends.COMMON_TYPE_ID  +
                " ORDER BY " + TO_FRIENDS + "." + ToFriends.VIEWED_DATE + ", " +
                TO_FRIENDS + "." + ToFriends.DELIVERED_DATE +
                " LIMIT 1)";

        //this subquery is correlated, so do not try to use it as standalone
        String SELECT_LAST_COMMON_TYPE_SUBQUERY =
                "(SELECT " + COMMON_TYPES + "." + CommonTypes._ID +
                " FROM " + COMMON_TYPES +
                " WHERE " + CHATS + "." + Chats._ID +
                " = " + COMMON_TYPES + "." + CommonTypes.CHAT_ID  +
                " ORDER BY " + COMMON_TYPES + "." + CommonTypes._ID + " DESC " +
                " LIMIT 1)";

        //this subquery is correlated, so do not try to use it as standalone
        String CHAT_CONVERSATION = COMMON_TYPES +
                " LEFT OUTER JOIN " + MESSAGES +
                    " ON " + COMMON_TYPES + "." + CommonTypes.TYPE +
                    " = " + "'" + CommonTypes.COMMON_TYPE_MESSAGE + "'" +
                    " AND " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + MESSAGES + "." + Messages.COMMON_TYPE_ID +
                " LEFT OUTER JOIN " + MEDIAS +
                    " ON " + COMMON_TYPES + "." + CommonTypes.TYPE +
                    " = " + "'" + CommonTypes.COMMON_TYPE_MEDIA + "'" +
                    " AND " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + MEDIAS + "." + Medias.COMMON_TYPE_ID +
                " LEFT OUTER JOIN " + FRIENDS +
                    " ON " + COMMON_TYPES + "." + CommonTypes.SENDER_ID +
                    " = " + FRIENDS + "." + Friends._ID +
                " LEFT OUTER JOIN " + TO_FRIENDS +
                    " ON " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + TO_FRIENDS + "." + ToFriends.COMMON_TYPE_ID +
                    " AND " + TO_FRIENDS + "." + ToFriends.TO_FRIEND_ID +
                    " = " + SELECT_LAST_TO_FRIEND_SUBQUERY;

        String CHATS_TO_DISPLAY = CHATS +
                " LEFT OUTER JOIN " + FRIEND_HAS_CHAT +
                    " ON " + CHATS + "." + Chats.TYPE +
                    " = " + "'" + Chats.CHAT_TYPE_CONVERSATION + "'" +
                    " AND " + CHATS + "." + Chats._ID +
                    " = " + FRIEND_HAS_CHAT + "." + FriendHasChats.CHAT_ID +
                " LEFT OUTER JOIN " + FRIENDS +
                    " ON " + CHATS + "." + Chats.TYPE +
                    " = " + "'" + Chats.CHAT_TYPE_CONVERSATION + "'" +
                    " AND " + FRIEND_HAS_CHAT + "." + FriendHasChats.FRIEND_ID +
                    " = " + FRIENDS + "." + Friends._ID +
                " INNER JOIN " + COMMON_TYPES +
                    " ON " + CHATS + "." + Chats._ID +
                    " = " + COMMON_TYPES + "." + CommonTypes.CHAT_ID +
                    " AND " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + SELECT_LAST_COMMON_TYPE_SUBQUERY +
                " LEFT OUTER JOIN " + MESSAGES +
                    " ON " + COMMON_TYPES + "." + CommonTypes.TYPE +
                    " = " + "'" + CommonTypes.COMMON_TYPE_MESSAGE + "'" +
                    " AND " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + MESSAGES + "." + Messages.COMMON_TYPE_ID +
                " LEFT OUTER JOIN " + MEDIAS +
                    " ON " + COMMON_TYPES + "." + CommonTypes.TYPE +
                    " = " + "'" + CommonTypes.COMMON_TYPE_MEDIA + "'" +
                    " AND " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + MEDIAS + "." + Medias.COMMON_TYPE_ID +
                " LEFT OUTER JOIN " + TO_FRIENDS +
                    " ON " + COMMON_TYPES + "." + CommonTypes._ID +
                    " = " + TO_FRIENDS + "." + ToFriends.COMMON_TYPE_ID +
                    " AND " + TO_FRIENDS + "." + ToFriends.TO_FRIEND_ID +
                    " = " + SELECT_LAST_TO_FRIEND_SUBQUERY;


    }

    /** {@code REFERENCES} clauses. */
    private interface References {
        String SENDER_ID = "REFERENCES " + Tables.FRIENDS + "(" + Friends._ID + ")";
        String CHAT_ID = "REFERENCES " + Tables.CHATS + "(" + Chats._ID + ")";
        String TO_FRIEND_ID = "REFERENCES " + Tables.FRIENDS + "(" + Friends._ID + ")";
        String COMMON_TYPE_ID = "REFERENCES " + Tables.COMMON_TYPES + "(" + CommonTypes._ID + ")";
        String FRIEND_ID = "REFERENCES " + Tables.FRIENDS + "(" + Friends._ID + ")";
    }

    public GSengerDatabase(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.COMMON_TYPES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CommonTypesColumns.COMMON_TYPE_SERVER_ID + " TEXT,"
                + CommonTypesColumns.TYPE + " TEXT NOT NULL,"
                + CommonTypesColumns.SEND_DATE + " INTEGER NOT NULL,"
                + CommonTypesColumns.SENT + " INTEGER NOT NULL,"
                + CommonTypesColumns.SENDER_ID + " INTEGER " + References.SENDER_ID + ","
                + CommonTypesColumns.CHAT_ID + " INTEGER " + References.CHAT_ID + ","
                + "UNIQUE (" + CommonTypesColumns.COMMON_TYPE_SERVER_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.FRIENDS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FriendsColumns.FRIEND_SERVER_ID + " TEXT,"
                + FriendsColumns.USER_NAME + " TEXT NOT NULL,"
                + FriendsColumns.ADDED_DATE + " INTEGER NOT NULL,"
                + FriendsColumns.LAST_LOGGED_DATE + " INTEGER,"
                + FriendsColumns.STATUS + " TEXT NOT NULL,"
                + FriendsColumns.PHOTO + " TEXT,"
                + FriendsColumns.PHOTO_HASH + " TEXT,"
                + "UNIQUE (" + FriendsColumns.FRIEND_SERVER_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.TO_FRIENDS + " ("
                + ToFriendsColumns.TO_FRIEND_ID + " INTEGER " + References.TO_FRIEND_ID + ","
                + ToFriendsColumns.COMMON_TYPE_ID + " INTEGER " + References.COMMON_TYPE_ID + ","
                + ToFriendsColumns.DELIVERED_DATE + " INTEGER,"
                + ToFriendsColumns.VIEWED_DATE + " INTEGER" + ")");

        db.execSQL("CREATE TABLE " + Tables.CHATS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ChatsColumns.CHAT_SERVER_ID + " TEXT,"
                + ChatsColumns.STARTED_DATE + " INTEGER,"
                + ChatsColumns.CHAT_NAME + " TEXT,"
                + ChatsColumns.TYPE + " TEXT NOT NULL,"
                + "UNIQUE (" + ChatsColumns.CHAT_SERVER_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.FRIEND_HAS_CHAT + " ("
                + FriendHasChatColumns.FRIEND_ID + " INTEGER,"
                + FriendHasChatColumns.CHAT_ID + " INTEGER NOT NULL" + ")");

        db.execSQL("CREATE TABLE " + Tables.MESSAGES + " ("
                + MessagesColumns.COMMON_TYPE_ID + " INTEGER NOT NULL " + References.COMMON_TYPE_ID + ","
                + MessagesColumns.TEXT + " TEXT NOT NULL " + ")");

        db.execSQL("CREATE TABLE " + Tables.MEDIAS + " ("
                + MediasColumns.COMMON_TYPE_ID + " INTEGER NOT NULL " + References.COMMON_TYPE_ID + ","
                + MediasColumns.TYPE + " INTEGER NOT NULL,"
                + MediasColumns.DESCRIPTION + " TEXT NOT NULL,"
                + MediasColumns.FILE_NAME + " TEXT NOT NULL,"
                + MediasColumns.PATH + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Tables.COMMON_TYPES);
        db.execSQL("DROP TABLE " + Tables.FRIENDS);
        db.execSQL("DROP TABLE " + Tables.TO_FRIENDS);
        db.execSQL("DROP TABLE " + Tables.CHATS);
        db.execSQL("DROP TABLE " + Tables.FRIEND_HAS_CHAT);
        db.execSQL("DROP TABLE " + Tables.MESSAGES);
        db.execSQL("DROP TABLE " + Tables.MEDIAS);

        onCreate(db);
    }

}
