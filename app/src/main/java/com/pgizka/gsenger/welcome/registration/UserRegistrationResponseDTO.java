package com.pgizka.gsenger.welcome.registration;

import com.pgizka.gsenger.io.Response;

public class UserRegistrationResponseDTO extends Response {

    private int userId;

    public UserRegistrationResponseDTO() {}

    public UserRegistrationResponseDTO(int resultCode) {
        super(resultCode);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
