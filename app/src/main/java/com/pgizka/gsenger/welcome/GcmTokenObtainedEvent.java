package com.pgizka.gsenger.welcome;

public class GcmTokenObtainedEvent {

    private String gcmToken;

    public GcmTokenObtainedEvent() {}

    public GcmTokenObtainedEvent(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }
}
