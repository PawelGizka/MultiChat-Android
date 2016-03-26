package com.pgizka.gsenger.jobqueue.sendMessge;


import com.pgizka.gsenger.api.BaseResponse;

public class PutMessageResponse extends BaseResponse {

    private int messageServerId;

    public PutMessageResponse() {
    }

    public int getMessageServerId() {
        return messageServerId;
    }

    public void setMessageServerId(int messageServerId) {
        this.messageServerId = messageServerId;
    }
}
