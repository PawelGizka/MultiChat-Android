package com.pgizka.gsenger.io;

public class Response {

    private int resultCode;

    public Response() {

    }

    public Response(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
