package com.pgizka.gsenger.jobqueue.refreshFriends;


import com.pgizka.gsenger.api.BaseResponse;
import com.pgizka.gsenger.provider.realm.Friend;

import java.util.List;

public class RefreshFriendsResponse extends BaseResponse {

    private List<Friend> friends;

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
