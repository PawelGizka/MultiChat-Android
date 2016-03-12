package com.pgizka.gsenger.provider.pojos;

public class Friend {

    private transient int id;

    private int serverId;
    private String userName;
    private long addedDate;
    private long lastLoggedDate;
    private String status;
    private String photoPath;
    private String photoHash;

    public Friend() {
    }

    public Friend(int id, int serverId, String userName, long addedDate, long lastLoggedDate, String status, String photoPath, String photoHash) {
        this.id = id;
        this.serverId = serverId;
        this.userName = userName;
        this.addedDate = addedDate;
        this.lastLoggedDate = lastLoggedDate;
        this.status = status;
        this.photoPath = photoPath;
        this.photoHash = photoHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoHash() {
        return photoHash;
    }

    public void setPhotoHash(String photoHash) {
        this.photoHash = photoHash;
    }
}
