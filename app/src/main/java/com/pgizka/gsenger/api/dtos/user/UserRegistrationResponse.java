package com.pgizka.gsenger.api.dtos.user;

public class UserRegistrationResponse {

    private int userId;
    private int resultCode;

    public enum ResultCode {

        OK(0),
        USER_ALREADY_EXIST(1),

        UNEXPECTED_ERROR(1001);

        public int code;

        private ResultCode(int code) {
            this.code = code;
        }

    }

    public UserRegistrationResponse() {}

    public UserRegistrationResponse(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
