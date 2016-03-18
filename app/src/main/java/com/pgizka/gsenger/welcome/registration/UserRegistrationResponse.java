package com.pgizka.gsenger.welcome.registration;

import com.pgizka.gsenger.api.BaseResponse;

public class UserRegistrationResponse extends BaseResponse {

    private int userId;

    public UserRegistrationResponse() {}

    public UserRegistrationResponse(int resultCode) {
        super(resultCode);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
