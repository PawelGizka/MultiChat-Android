package com.pgizka.gsenger.api.dtos.messages;


import java.util.List;

public class MessagesStateChangedRequest {

    private List<Integer> messagesIds;
    private int receiverId;
    private long deliveredDate;
    private long viewedDate;

    public MessagesStateChangedRequest() {
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
