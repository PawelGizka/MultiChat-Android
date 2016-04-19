package com.pgizka.gsenger.gcm.data;


public class NewMediaMessageData extends NewMessageData {
    public static final transient String ACTION = "NEW_MEDIA_MESSAGE_ACTION";

    private String description;
    private String fileName;
    private int type;

    public NewMediaMessageData() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
