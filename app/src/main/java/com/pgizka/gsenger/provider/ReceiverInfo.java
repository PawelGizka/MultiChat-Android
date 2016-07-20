package com.pgizka.gsenger.provider;


import io.realm.RealmObject;

public class ReceiverInfo extends RealmObject {

    private User user;
    private Message message;

    private long delivered;
    private long viewed;

    public ReceiverInfo() {
    }

    public long getDelivered() {
        return delivered;
    }

    public void setDelivered(long delivered) {
        this.delivered = delivered;
    }

    public long getViewed() {
        return viewed;
    }

    public void setViewed(long viewed) {
        this.viewed = viewed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
