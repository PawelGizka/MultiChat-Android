package com.pgizka.gsenger.jobqueue.sendMessge;


import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.TextMessage;

public class PutMediaMessageRequest extends PutMessageRequest {

    private String description;
    private int type;
    private String fileName;

    public PutMediaMessageRequest(Message message) {
        super(message);
        MediaMessage mediaMessage = message.getMediaMessage();
        this.description = mediaMessage.getDescription();
        this.type = mediaMessage.getMediaType();
        this.fileName = mediaMessage.getFileName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
