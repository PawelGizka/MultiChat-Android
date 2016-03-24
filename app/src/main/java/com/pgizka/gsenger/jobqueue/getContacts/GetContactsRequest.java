package com.pgizka.gsenger.jobqueue.getContacts;

import java.util.List;

public class GetContactsRequest {

    List<String> phoneNumbers;

    public GetContactsRequest() {
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

        GetContactsRequest that = (GetContactsRequest) o;

        return that.getPhoneNumbers().containsAll(phoneNumbers);
    }
}
