package com.pgizka.gsenger.jobqueue.refreshFriends;

import java.util.List;

public class RefreshFriendsRequest {

    List<String> phoneNumbers;

    public RefreshFriendsRequest() {
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefreshFriendsRequest that = (RefreshFriendsRequest) o;

        return that.getPhoneNumbers().containsAll(phoneNumbers);
    }
}
