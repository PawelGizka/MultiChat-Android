package com.pgizka.gsenger.api.dtos.chats;

import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.User;

import java.util.ArrayList;
import java.util.List;

public class AddUsersToChatRequest {

    private int chatId;
    private List<Integer> usersToAdd;

    public AddUsersToChatRequest(Chat chat, List<User> users) {
        this.chatId = chat.getServerId();
        usersToAdd = new ArrayList<>(users.size());
        for (User user : users) {
            usersToAdd.add(user.getServerId());
        }
    }

    public AddUsersToChatRequest() {
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public List<Integer> getUsersToAdd() {
        return usersToAdd;
    }

    public void setUsersToAdd(List<Integer> usersToAdd) {
        this.usersToAdd = usersToAdd;
    }
}
