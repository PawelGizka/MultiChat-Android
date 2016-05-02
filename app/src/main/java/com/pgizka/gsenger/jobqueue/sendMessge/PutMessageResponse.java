package com.pgizka.gsenger.jobqueue.sendMessge;


public class PutMessageResponse{

    private int messageServerId;

    public PutMessageResponse() {
    }

    public PutMessageResponse(int messageServerId) {
        this.messageServerId = messageServerId;
    }

    public int getMessageServerId() {
        return messageServerId;
    }

    public void setMessageServerId(int messageServerId) {
        this.messageServerId = messageServerId;
    }
}
