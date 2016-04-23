package com.pgizka.gsenger.jobqueue.getContacts;

import java.util.List;

public class GetContactsRequest {

    private List<String> phoneNumbers;
    private List<Integer> userIds;

    public GetContactsRequest() {
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
