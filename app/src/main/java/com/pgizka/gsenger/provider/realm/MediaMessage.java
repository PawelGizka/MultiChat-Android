package com.pgizka.gsenger.provider.realm;

import io.realm.RealmObject;

public class MediaMessage extends RealmObject {

    private int mediaType;
    private String description;
    private String fileName;
    private String path;

    public MediaMessage() {}

    public enum Type {
        PHOTO(0),
        VIDEO(1),
        FILE(2);

        public int code;

        Type(int code) {
            this.code = code;
        }
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
