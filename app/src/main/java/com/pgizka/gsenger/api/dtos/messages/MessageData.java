package com.pgizka.gsenger.api.dtos.messages;

import com.pgizka.gsenger.provider.User;

import java.util.List;

public class MessageData {

    private int messageId;
    private long sendDate;
    private User sender;
    private long chatId;
    private List<ReceiverData> receiversData;

    public MessageData() {
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

    public List<ReceiverData> getReceiversData() {
        return receiversData;
    }

    public void setReceiversData(List<ReceiverData> receiversData) {
        this.receiversData = receiversData;
    }
}
