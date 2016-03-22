package com.pgizka.gsenger.provider.realm;

import io.realm.RealmObject;

public class TextMessage extends RealmObject{

    private String text;

    public TextMessage() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
