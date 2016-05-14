package com.pgizka.gsenger.api.dtos.messages;

import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.TextMessage;

public class PutTextMessageRequest extends PutMessageRequest {

    private String text;

    public PutTextMessageRequest() {
    }

    public PutTextMessageRequest(Message message) {
        super(message);
        TextMessage textMessage = message.getTextMessage();
        this.text = textMessage.getText();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
