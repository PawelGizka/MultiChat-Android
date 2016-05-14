package com.pgizka.gsenger.api.dtos.contacts;


import com.pgizka.gsenger.provider.User;

import java.util.List;

public class GetContactsResponse{

    private List<User> contacts;

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }
}
