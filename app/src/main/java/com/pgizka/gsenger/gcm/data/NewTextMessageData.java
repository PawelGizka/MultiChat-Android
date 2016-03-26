package com.pgizka.gsenger.gcm.data;

public class NewTextMessageData extends NewMessageData {

    private String text;

    public NewTextMessageData() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
