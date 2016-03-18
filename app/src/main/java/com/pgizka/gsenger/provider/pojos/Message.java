package com.pgizka.gsenger.provider.pojos;

import com.pgizka.gsenger.provider.GSengerContract;

public class Message extends CommonType {

    private String text;

    public Message() {
        super(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
