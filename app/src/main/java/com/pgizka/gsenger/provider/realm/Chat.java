package com.pgizka.gsenger.provider.realm;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Chat extends RealmObject {

    @PrimaryKey
    private int id;
    private int serverId;
    private long startedDate;
    private String chatName;
    private int type;

    private RealmList<Message> messages;
    private RealmList<Friend> friends;

    public Chat() {
    }

    public enum Type {
        SINGLE_CONVERSATION(0),
        GROUP(1);

        public int code;

        Type(int code) {
            this.code = code;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public long getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(long startedDate) {
        this.startedDate = startedDate;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RealmList<Message> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<Message> messages) {
        this.messages = messages;
    }

    public RealmList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(RealmList<Friend> friends) {
        this.friends = friends;
    }
}
