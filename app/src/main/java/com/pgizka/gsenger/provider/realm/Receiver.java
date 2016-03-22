package com.pgizka.gsenger.provider.realm;


import io.realm.RealmObject;

public class Receiver extends RealmObject {

    private Friend friend;
    private Message message;

    private long delivered;
    private long viewed;

    public Receiver() {
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

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
