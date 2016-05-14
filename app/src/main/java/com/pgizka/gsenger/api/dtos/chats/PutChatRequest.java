package com.pgizka.gsenger.api.dtos.chats;


import com.pgizka.gsenger.provider.User;

import java.util.ArrayList;
import java.util.List;

public class PutChatRequest {

    private String chatName;
    private long startedDate;
    private List<Integer> participants;

    public PutChatRequest() {
    }

    public PutChatRequest(String chatName, List<User> participants) {
        this.chatName = chatName;
        this.startedDate = System.currentTimeMillis();

        this.participants = new ArrayList<>(participants.size());
        for (User participant : participants) {
            this.participants.add(participant.getServerId());
        }
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public long getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(long startedDate) {
        this.startedDate = startedDate;
    }

    public List<Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Integer> participants) {
        this.participants = participants;
    }

}
