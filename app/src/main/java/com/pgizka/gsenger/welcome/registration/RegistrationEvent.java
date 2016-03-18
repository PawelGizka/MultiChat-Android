package com.pgizka.gsenger.welcome.registration;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrationEvent that = (RegistrationEvent) o;

        if (success != that.success) return false;
        return message == that.message;

    }
}
