package com.pgizka.gsenger.gcm.data;


import com.pgizka.gsenger.jobqueue.setMessageState.MessageStateChangedRequest;

public class MessageStateChangedData {
    public static final String MESSAGE_DELIVERED_ACTION = "MESSAGE_DELIVERED_ACTION";
    public static final String MESSAGE_VIEWED_ACTION = "MESSAGE_VIEWED_ACTION";

    private int messageId;
    private int receiverId;
    private long date;

    public MessageStateChangedData() {
    }

    public MessageStateChangedData(MessageStateChangedRequest request) {
        this.messageId = request.getMessageId();
        this.receiverId = request.getReceiverId();
        this.date = request.getDate();
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
