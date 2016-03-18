package com.pgizka.gsenger.provider.pojos;

public class Message extends CommonType {

    private String text;

    public Message() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
