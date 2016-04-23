package com.pgizka.gsenger.gcm.data;

import com.pgizka.gsenger.provider.User;

import java.util.List;

public class NewMessageData {

    private int messageId;
    private long sendDate;
    private User sender;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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
