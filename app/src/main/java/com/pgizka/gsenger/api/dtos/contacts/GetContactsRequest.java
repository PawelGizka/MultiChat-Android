package com.pgizka.gsenger.api.dtos.contacts;

import java.util.List;

public class GetContactsRequest {

    private int userId;
    private List<String> phoneNumbers;
    private List<Integer> userIds;

    public GetContactsRequest() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
