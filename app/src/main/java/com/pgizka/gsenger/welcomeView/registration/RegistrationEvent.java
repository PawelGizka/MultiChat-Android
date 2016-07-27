package com.pgizka.gsenger.welcomeView.registration;

import android.support.annotation.StringRes;

public class RegistrationEvent {

    private boolean success;

    private int message;

    public RegistrationEvent() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(@StringRes int message) {
        this.message = message;
    }
}
