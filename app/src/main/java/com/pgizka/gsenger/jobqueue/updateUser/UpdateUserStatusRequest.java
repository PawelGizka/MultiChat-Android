package com.pgizka.gsenger.jobqueue.updateUser;


public class UpdateUserStatusRequest {

    private String userName;
    private long lastLoggedDate;
    private String status;

    public UpdateUserStatusRequest() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getLastLoggedDate() {
        return lastLoggedDate;
    }

    public void setLastLoggedDate(long lastLoggedDate) {
        this.lastLoggedDate = lastLoggedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

