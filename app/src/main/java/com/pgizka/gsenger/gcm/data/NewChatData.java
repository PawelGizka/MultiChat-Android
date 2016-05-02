package com.pgizka.gsenger.gcm.data;

import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.User;

import java.util.ArrayList;
import java.util.List;


public class NewChatData {

    public static final transient String ACTION = "NEW_GROUP_CHAT_ACTION";

    private int chatId;
    private String name;
    private long startedDate;

    private List<User> participants;

    public NewChatData() {
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
