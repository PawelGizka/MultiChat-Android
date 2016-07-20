package com.pgizka.gsenger.api.dtos.messages;

import java.util.List;

public class ReceiverInfoData {
    public static final String UPDATE_RECEIVER_ACTION = "UPDATE_RECEIVER_ACTION";
    private List<Integer> messagesIds;
    private int receiverId;
    private long deliveredDate;
    private long viewedDate;

    public ReceiverInfoData() {
    }

    public List<Integer> getMessagesIds() {
        return messagesIds;
    }

    public void setMessagesIds(List<Integer> messagesIds) {
        this.messagesIds = messagesIds;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public long getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(long deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public long getViewedDate() {
        return viewedDate;
    }

    public void setViewedDate(long viewedDate) {
        this.viewedDate = viewedDate;
    }
}
