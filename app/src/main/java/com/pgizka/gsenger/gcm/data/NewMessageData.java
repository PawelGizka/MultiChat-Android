package com.pgizka.gsenger.gcm.data;

import java.util.List;

public class NewMessageData {

    private int messageId;
    private long sendDate;
    private int senderId;
    private long chatId;
    private List<Integer> receiversIds;

    public NewMessageData() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public List<Integer> getReceiversIds() {
        return receiversIds;
    }

    public void setReceiversIds(List<Integer> receiversIds) {
        this.receiversIds = receiversIds;
    }
}
