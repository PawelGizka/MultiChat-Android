package com.pgizka.gsenger.api.dtos.chats;


public class PutChatResponse {

    private int chatId;

    public PutChatResponse() {
    }

    public PutChatResponse(int chatId) {
        this.chatId = chatId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

}
