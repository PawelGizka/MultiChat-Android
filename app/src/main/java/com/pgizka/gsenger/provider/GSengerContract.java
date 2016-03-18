package com.pgizka.gsenger.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.DateUtils;

public class GSengerContract {

    public static final String CONTENT_TYPE_APP_BASE = "gsenger.";

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd."
            + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd."
            + CONTENT_TYPE_APP_BASE;

    interface CommonTypesColumns{

        String _ID = "common_type_id";

        String COMMON_TYPE_SERVER_ID = "common_type_server_id";

        String TYPE = "message_type";

        String SEND_DATE = "send_date";

        String STATE = "state";
        /** Indicates who sent this item. If null it means that this is from owner of phone **/
        String SENDER_ID = "sender_id";

        String CHAT_ID = "chat_id";

    }

    interface FriendsColumns{

        String _ID = "friend_id";

        String FRIEND_SERVER_ID = "friend_server_id";

        String USER_NAME = "user_name";

        String ADDED_DATE = "added_date";

        String LAST_LOGGED_DATE = "last_logged_date";

        String STATUS = "status";

        String PHOTO = "photo";

        String PHOTO_HASH = "photo_hash";

    }

    interface ToFriendsColumns{

        String TO_FRIEND_ID = "to_friend_id";

        String COMMON_TYPE_ID = "common_type_id";

        String DELIVERED_DATE = "delivered_date";

        String VIEWED_DATE = "viewed_date";

    }

    interface ChatsColumns{

        String _ID = "chat_id";

        String CHAT_SERVER_ID = "chat_server_id";

        String STARTED_DATE = "started_date";

        String CHAT_NAME = "chat_name";

        String TYPE = "chat_type";

    }

    interface FriendHasChatColumns{
        /**Foreign key mapping to Friends table, if null it means
         * that this is the owner of phone
         */
        String FRIEND_ID = "friend_id";

        String CHAT_ID = "chat_id";
    }

    interface MessagesColumns{

        String COMMON_TYPE_ID = "common_type_id";

        String TEXT = "text";

    }

    interface MediasColumns{

        String COMMON_TYPE_ID = "common_type_id";

        String TYPE = "type";

        String DESCRIPTION = "description";

        String FILE_NAME = "file_name";

        String PATH = "path";

    }

    public static final String CONTENT_AUTHORITY = "com.pgizka.gsenger";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_COMMON_TYPES = "common_types";

    public static final String PATH_COMMON_TYPE = "common_type";

    public static final String PATH_COMMON_TYPE_WITH_FRIENDS = "common_type_with_friends";

    public static final String PATH_FRIENDS = "friends";

    public static final String PATH_FRIEND = "friend";

    public static final String PATH_FRIEND_WITH_CHATS = "friend_with_chat";

    public static final String PATH_FRIEND_WITH_CHAT = "friend_with_chat";

    public static final String PATH_TO_FRIENDS = "to_friends";

    public static final String PATH_TO_FRIEND = "to_friend";

    public static final String PATH_CHATS = "chats";

    public static final String PATH_CHAT = "chat";

    public static final String PATH_CHAT_WITH_FRIENDS = "chat_with_friends";

    public static final String PATH_CHAT_CONVERSATION = "chat_conversation";

    public static final String PATH_CHAT_GROUP = "chat_group";

    public static final String PATH_CHATS_TO_DISPLAY = "chats_to_display";

    public static final String PATH_FRIENDS_HAVE_CHATS = "friends_have_chats";

    public static final String PATH_FRIEND_HAS_CHATS = "friend_has_chats";

    public static final String PATH_FRIEND_HAS_CHAT = "friend_has_chat";

    public static final String PATH_MESSAGES = "messages";

    public static final String PATH_MESSAGE = "message";

    public static final String PATH_MEDIAS = "medias";

    public static final String PATH_MEDIA = "media";

    public static String makeContentType(String id) {
        if (id != null) {
            return CONTENT_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    public static String makeContentItemType(String id) {
        if (id != null) {
            return CONTENT_ITEM_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    public static class CommonTypes implements CommonTypesColumns {

        public static final String COMMON_TYPE_MESSAGE = "message";
        public static final String COMMON_TYPE_MEDIA = "media";

        public enum State {
            WAITING_TO_SEND(0),
            SENDING(1),
            CANNOT_SEND(2),
            SENT(3);

            public int code;

            State(int code) {
                this.code = code;
            }
        }

        public static final boolean isValidCommonType(String type) {
            return COMMON_TYPE_MESSAGE.equals(type) || COMMON_TYPE_MEDIA.equals(type);
        }

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMON_TYPES).build();

        public static final String CONTENT_TYPE_ID = "common_type";

        public static Uri buildCommonTypeUri(String commonTypeId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMON_TYPE)
                    .appendPath(commonTypeId).build();
        }

        public static Uri buildCommonTypeWithFriendsUri(String commonTypeId){
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMON_TYPE_WITH_FRIENDS)
                    .appendPath(commonTypeId).build();
        }

    }

    public static class Friends implements FriendsColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIENDS).build();

        public static final String CONTENT_TYPE_ID = "friends";

        public static Uri buildFriendUri(String friendId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIEND)
                    .appendPath(friendId).build();
        }

        public static Uri buildFriendWithChatsUri(String friendId){
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIEND_WITH_CHATS)
                    .appendPath(friendId).build();
        }

        public static Uri buildFriendWithChatUri(String friendId, String chatId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIEND_WITH_CHAT)
                    .appendPath(friendId).appendPath(chatId).build();
        }

    }

    public static class ToFriends implements ToFriendsColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TO_FRIENDS).build();

        public static final String CONTENT_TYPE_ID = "to_friends";

        public static Uri buildToFriendUri(String toFriendId, String commonTypeId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_TO_FRIEND)
                    .appendPath(toFriendId).appendPath(commonTypeId).build();
        }

    }

    public static class Chats implements ChatsColumns {

        public static final String CHAT_TYPE_CONVERSATION = "conversation";
        public static final String CHAT_TYPE_GROUP = "group";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHATS).build();

        public static final String CONTENT_TYPE_ID = "chats";

        public static Uri buildChatUri(String chatId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHAT)
                    .appendPath(chatId).build();
        }

        public static Uri buildChatWithFriendsUri(String chatId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHAT_WITH_FRIENDS)
                    .appendPath(chatId).build();
        }

        public static Uri buildChatConversationUri(String chatId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHAT_CONVERSATION)
                    .appendPath(chatId).build();
        }

        public static Uri buildChatsToDisplayUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHATS_TO_DISPLAY).build();
        }

    }

    public static class FriendHasChats implements FriendHasChatColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIENDS_HAVE_CHATS).build();

        public static final String CONTENT_TYPE_ID = "friend_has_chat";

        public static Uri buildFriendHasChatUri(String friendId, String chatId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIEND_HAS_CHAT)
                    .appendPath(friendId).appendPath(chatId).build();
        }
    }

    public static class Messages implements MessagesColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGES).build();

        public static final String CONTENT_TYPE_ID = "messages";

        public static Uri buildMessageUri(String messageId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGE)
                    .appendPath(messageId).build();
        }

    }

    public static class Medias implements MediasColumns {

        public static final String MEDIA_TYPE_PHOTO = "photo";
        public static final String MEDIA_TYPE_VIDEO = "video";
        public static final String MEDIA_TYPE_FILE = "file";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDIAS).build();

        public static final String CONTENT_TYPE_ID = "medias";

        public static Uri buildMediaUri(String mediaId) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDIA)
                    .appendPath(mediaId).build();
        }
    }



}
