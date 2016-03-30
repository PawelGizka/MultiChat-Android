package com.pgizka.gsenger.gcm.data;

public class NewTextMessageData extends NewMessageData {
    public static final String ACTION = "NEW_TEXT_MESSAGE_ACTION";

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
