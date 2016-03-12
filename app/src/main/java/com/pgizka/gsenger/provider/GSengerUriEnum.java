package com.pgizka.gsenger.provider;

import static com.pgizka.gsenger.provider.GSengerContract.*;

public enum GSengerUriEnum {
    COMMON_TYPES(100, PATH_COMMON_TYPES, CommonTypes.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.COMMON_TYPES),
    COMMON_TYPE(101, PATH_COMMON_TYPE + "/*", CommonTypes.CONTENT_TYPE_ID, true, GSengerDatabase.Tables.COMMON_TYPES),
    COMMON_TYPE_WITH_FRIENDS(102, PATH_COMMON_TYPE_WITH_FRIENDS + "/*", CommonTypes.CONTENT_TYPE_ID, false, null),
    FRIENDS(200, PATH_FRIENDS, Friends.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.FRIENDS),
    FRIEND(201, PATH_FRIEND + "/*", Friends.CONTENT_TYPE_ID, true, GSengerDatabase.Tables.FRIENDS),
    FRIEND_WITH_CHATS(202, PATH_FRIEND_WITH_CHATS + "/*", Friends.CONTENT_TYPE_ID, false, null),
    FRIEND_WITH_CHAT(203, PATH_FRIEND_WITH_CHAT + "/*/*", Friends.CONTENT_TYPE_ID, true, null),
    TO_FRIENDS(300, PATH_TO_FRIENDS, ToFriends.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.TO_FRIENDS),
    TO_FRIEND(301, PATH_TO_FRIEND + "/*/*", ToFriends.CONTENT_TYPE_ID, true, GSengerDatabase.Tables.TO_FRIENDS),
    CHATS(400, PATH_CHATS, Chats.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.CHATS),
    CHAT(401, PATH_CHAT + "/*", Chats.CONTENT_TYPE_ID, true, GSengerDatabase.Tables.CHATS),
    CHAT_WITH_FRIENDS(402, PATH_CHAT_WITH_FRIENDS + "/*", Chats.CONTENT_TYPE_ID, false, null),
    CHAT_CONVERSATION(403, PATH_CHAT_CONVERSATION + "/*", Chats.CONTENT_TYPE_ID, false, null),
    CHAT_GROUP(404, PATH_CHAT_GROUP + "/*", Chats.CONTENT_TYPE_ID, false, null),
    CHATS_TO_DISPLAY(405, PATH_CHATS_TO_DISPLAY, Chats.CONTENT_TYPE_ID, false, null),
    FRIENDS_HAVE_CHATS(500, PATH_FRIENDS_HAVE_CHATS, FriendHasChats.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.FRIEND_HAS_CHAT),
    FRIEND_HAS_CHATS(501, PATH_FRIEND_HAS_CHATS + "/*", FriendHasChats.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.FRIEND_HAS_CHAT),
    FRIEND_HAS_CHAT(502, PATH_FRIEND_HAS_CHAT + "/*/*", FriendHasChats.CONTENT_TYPE_ID, true, GSengerDatabase.Tables.FRIEND_HAS_CHAT),
    MESSAGES(600, PATH_MESSAGES, Messages.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.MESSAGES),
    MESSAGE(601, PATH_MESSAGE + "/*", Messages.CONTENT_TYPE_ID, true, GSengerDatabase.Tables.MESSAGES),
    MEDIAS(700, PATH_MEDIAS, Medias.CONTENT_TYPE_ID, false, GSengerDatabase.Tables.MEDIAS),
    MEDIA(701, PATH_MEDIA + "/*", Medias.CONTENT_TYPE_ID, true, GSengerDatabase.Tables.MEDIAS);

    public int code;

    /**
     * The path to the {@link android.content.UriMatcher} will use to match. * may be used as a
     * wild card for any text, and # may be used as a wild card for numbers.
     */
    public String path;

    public String contentType;

    public String table;

    GSengerUriEnum(int code, String path, String contentTypeId, boolean item, String table) {
        this.code = code;
        this.path = path;
        this.contentType = item ? GSengerContract.makeContentItemType(contentTypeId)
                : GSengerContract.makeContentType(contentTypeId);
        this.table = table;
    }

}
