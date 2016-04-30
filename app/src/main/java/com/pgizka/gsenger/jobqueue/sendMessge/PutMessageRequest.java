package com.pgizka.gsenger.jobqueue.sendMessge;


import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Message;

public abstract class PutMessageRequest {

    private long sendDate;
    private int senderId;
    private int receiverId;
    private int chatId;

    public PutMessageRequest() {
    }

    public PutMessageRequest(Message message) {
        this.sendDate = message.getSendDate();
        this.senderId = message.getSender().getServerId();

        Chat chat = message.getChat();
        if (chat.getType() == Chat.Type.GROUP.code) {
            this.chatId = chat.getServerId();
            this.receiverId = -1;
        } else {
            this.receiverId = message.getReceivers().get(0).getUser().getServerId();
            this.chatId = -1;
        }
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

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
