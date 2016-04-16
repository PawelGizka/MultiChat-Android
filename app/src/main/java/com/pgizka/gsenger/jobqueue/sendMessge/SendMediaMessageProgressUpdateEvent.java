package com.pgizka.gsenger.jobqueue.sendMessge;


public class SendMediaMessageProgressUpdateEvent {

    private int messageId;
    private int progress;

    public SendMediaMessageProgressUpdateEvent(int messageId, int progress) {
        this.messageId = messageId;
        this.progress = progress;
    }

    public SendMediaMessageProgressUpdateEvent() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
