package com.pgizka.gsenger.api;

public class BaseResponse {

    private int resultCode;

    public BaseResponse() {

    }

    public BaseResponse(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
