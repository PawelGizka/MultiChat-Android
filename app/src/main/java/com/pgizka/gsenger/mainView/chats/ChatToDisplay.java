package com.pgizka.gsenger.mainView.chats;

public class ChatToDisplay {

    private long chatId;
    private long chatStartedData;
    private String chatName;
    private String chatType;

    private long friendId;
    private String friendUserName;
    private String friendPhotoPath;

    private String commonTypeType;
    private long commonTypeSendDate;

    private String messageText;

    private String mediaType;
    private String mediaFileName;
    private String mediaDescription;

    private long toFriendDeliveredDate;
    private long toFriendViewedDate;

    public ChatToDisplay() {

    }

    public ChatToDisplay(long chatId, long chatStartedData, String chatName, String chatType,
                         long friendId, String friendUserName, String friendPhotoPath,
                         String commonTypeType, long commonTypeSendDate, String messageText,
                         String mediaType, String mediaFileName, String mediaDescription,
                         long toFriendDeliveredDate, long toFriendViewedDate) {
        this.chatId = chatId;
        this.chatStartedData = chatStartedData;
        this.chatName = chatName;
        this.chatType = chatType;
        this.friendId = friendId;
        this.friendUserName = friendUserName;
        this.friendPhotoPath = friendPhotoPath;
        this.commonTypeType = commonTypeType;
        this.commonTypeSendDate = commonTypeSendDate;
        this.messageText = messageText;
        this.mediaType = mediaType;
        this.mediaFileName = mediaFileName;
        this.mediaDescription = mediaDescription;
        this.toFriendDeliveredDate = toFriendDeliveredDate;
        this.toFriendViewedDate = toFriendViewedDate;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getChatStartedData() {
        return chatStartedData;
    }

    public void setChatStartedData(long chatStartedData) {
        this.chatStartedData = chatStartedData;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getFriendUserName() {
        return friendUserName;
    }

    public void setFriendUserName(String friendUserName) {
        this.friendUserName = friendUserName;
    }

    public String getFriendPhotoPath() {
        return friendPhotoPath;
    }

    public void setFriendPhotoPath(String friendPhotoPath) {
        this.friendPhotoPath = friendPhotoPath;
    }

    public String getCommonTypeType() {
        return commonTypeType;
    }

    public void setCommonTypeType(String commonTypeType) {
        this.commonTypeType = commonTypeType;
    }

    public long getCommonTypeSendDate() {
        return commonTypeSendDate;
    }

    public void setCommonTypeSendDate(long commonTypeSendDate) {
        this.commonTypeSendDate = commonTypeSendDate;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaFileName() {
        return mediaFileName;
    }

    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
    }

    public long getToFriendDeliveredDate() {
        return toFriendDeliveredDate;
    }

    public void setToFriendDeliveredDate(long toFriendDeliveredDate) {
        this.toFriendDeliveredDate = toFriendDeliveredDate;
    }

    public long getToFriendViewedDate() {
        return toFriendViewedDate;
    }

    public void setToFriendViewedDate(long toFriendViewedDate) {
        this.toFriendViewedDate = toFriendViewedDate;
    }
}

