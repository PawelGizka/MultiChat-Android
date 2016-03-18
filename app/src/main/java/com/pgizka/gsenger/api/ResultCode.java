package com.pgizka.gsenger.api;

public enum ResultCode {

    OK(0),
    USER_ALREADY_EXIST(1),


    UNEXPECTED_ERROR(1001);

    public int code;

    private ResultCode(int code) {
        this.code = code;
    }

}
