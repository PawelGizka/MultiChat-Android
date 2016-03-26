package com.pgizka.gsenger.jobqueue.setMessageState;


public class MessageStateChangedRequest {

    private int messageId;
    private int receiverId;
    private long date;

    public MessageStateChangedRequest() {
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
