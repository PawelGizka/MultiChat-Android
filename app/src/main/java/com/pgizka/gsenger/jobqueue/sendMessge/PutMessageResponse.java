package com.pgizka.gsenger.jobqueue.sendMessge;


import com.pgizka.gsenger.api.BaseResponse;
import com.pgizka.gsenger.api.ResultCode;

public class PutMessageResponse extends BaseResponse {

    private int messageServerId;

    public PutMessageResponse() {
    }

    public PutMessageResponse(ResultCode resultCode, int messageServerId) {
        super(resultCode.code);
        this.messageServerId = messageServerId;
    }

    public int getMessageServerId() {
        return messageServerId;
    }

    public void setMessageServerId(int messageServerId) {
        this.messageServerId = messageServerId;
    }
}
