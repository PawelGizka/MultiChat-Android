package com.pgizka.gsenger.api.dtos.messages;

public class TextMessageData extends MessageData {
    public static final String ACTION = "NEW_TEXT_MESSAGE_ACTION";

    private String text;

    public TextMessageData() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
