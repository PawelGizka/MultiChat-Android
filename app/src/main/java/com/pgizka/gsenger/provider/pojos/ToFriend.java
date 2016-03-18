package com.pgizka.gsenger.provider.pojos;


public class ToFriend {

    private int toFriendId;
    private int commonTypeId;
    private long delivered;
    private long viewed;

    public ToFriend() {
    }

    public int getToFriendId() {
        return toFriendId;
    }

    public void setToFriendId(int toFriendId) {
        this.toFriendId = toFriendId;
    }

    public int getCommonTypeId() {
        return commonTypeId;
    }

    public void setCommonTypeId(int commonTypeId) {
        this.commonTypeId = commonTypeId;
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
}
