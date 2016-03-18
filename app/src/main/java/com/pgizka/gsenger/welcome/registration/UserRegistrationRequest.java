package com.pgizka.gsenger.welcome.registration;


public class UserRegistrationRequest {

    private String userName;
    private String email;
    private String password;

    private int phoneNumber;
    private String gcmToken;

    public UserRegistrationRequest() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    //required for testing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRegistrationRequest that = (UserRegistrationRequest) o;

        if (phoneNumber != that.phoneNumber) return false;
        if (!userName.equals(that.userName)) return false;
        if (!email.equals(that.email)) return false;
        if (!password.equals(that.password)) return false;
        return gcmToken.equals(that.gcmToken);

    }
}

