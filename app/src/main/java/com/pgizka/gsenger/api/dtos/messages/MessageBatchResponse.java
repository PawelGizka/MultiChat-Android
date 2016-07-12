package com.pgizka.gsenger.api.dtos.messages;

import java.util.List;

public class MessageBatchResponse {

    List<TextMessageData> textMessages;
    List<MediaMessageData> mediaMessages;

    public MessageBatchResponse(List<TextMessageData> textMessages, List<MediaMessageData> mediaMessages) {
        this.textMessages = textMessages;
        this.mediaMessages = mediaMessages;
    }

    public MessageBatchResponse() {
    }

    public List<TextMessageData> getTextMessages() {
        return textMessages;
    }

    public void setTextMessages(List<TextMessageData> textMessages) {
        this.textMessages = textMessages;
    }

    public List<MediaMessageData> getMediaMessages() {
        return mediaMessages;
    }

    public void setMediaMessages(List<MediaMessageData> mediaMessages) {
        this.mediaMessages = mediaMessages;
    }
}

