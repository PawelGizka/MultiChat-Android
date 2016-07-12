package com.pgizka.gsenger.api.dtos.messages;

public class ReceiverData {
    public static final String UPDATE_RECEIVER_ACTION = "UPDATE_RECEIVER_ACTION";

    private int messageId;
    private int receiverId;
    private long deliveredDate;
    private long viewedDate;

    public ReceiverData() {
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

    public long getViewedDate() {
        return viewedDate;
    }

    public void setViewedDate(long viewedDate) {
        this.viewedDate = viewedDate;
    }

    public long getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(long deliveredDate) {
        this.deliveredDate = deliveredDate;
    }
}
