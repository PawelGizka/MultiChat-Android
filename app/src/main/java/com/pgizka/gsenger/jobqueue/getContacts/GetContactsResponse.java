package com.pgizka.gsenger.jobqueue.getContacts;


import com.pgizka.gsenger.api.BaseResponse;
import com.pgizka.gsenger.provider.User;

import java.util.List;

public class GetContactsResponse extends BaseResponse {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
