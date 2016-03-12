package com.pgizka.gsenger.mainView.contacts;

public class Contact {

    private String userName;
    private String status;
    private long addedDate;
    private long lastLoggedDate;
    private String photo;

    public Contact() {

    }

    public Contact(String userName, String status, long addedDate, long lastLoggedDate, String photo) {
        this.userName = userName;
        this.status = status;
        this.addedDate = addedDate;
        this.lastLoggedDate = lastLoggedDate;
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    public long getLastLoggedDate() {
        return lastLoggedDate;
    }

    public void setLastLoggedDate(long lastLoggedDate) {
        this.lastLoggedDate = lastLoggedDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
