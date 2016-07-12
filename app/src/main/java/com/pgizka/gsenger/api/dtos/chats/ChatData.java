package com.pgizka.gsenger.api.dtos.chats;

import com.pgizka.gsenger.provider.User;

import java.util.List;


public class ChatData {
    public static final transient String NEW_GROUP_CHAT_ACTION = "NEW_GROUP_CHAT_ACTION";
    public static final transient String USERS_ADDED_TO_CHAT_ACTION = "USERS_ADDED_TO_CHAT_ACTION";
    public static final transient String ADDED_TO_CHAT_ACTION = "ADDED_TO_CHAT_ACTION";

    private int chatId;
    private String name;
    private long startedDate;

    private List<User> participants;

    public ChatData() {
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(long startedDate) {
        this.startedDate = startedDate;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

}
