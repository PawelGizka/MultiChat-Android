package com.pgizka.gsenger.jobqueue.getContacts;


import com.pgizka.gsenger.api.BaseResponse;
import com.pgizka.gsenger.provider.User;

import java.util.List;

public class GetContactsResponse extends BaseResponse {

    private List<User> contacts;

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }
}
