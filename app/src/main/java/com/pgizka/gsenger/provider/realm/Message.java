package com.pgizka.gsenger.provider.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Message extends RealmObject {

    @PrimaryKey
    private int id;
    private int serverId;
    private int type;
    private boolean outgoing;
    private long sendDate;
    private int state;

    private Friend sender;
    private Chat chat;
    private RealmList<Receiver> receivers;

    private TextMessage textMessage;
    private MediaMessage mediaMessage;

    public enum Type {
        TEXT_MESSAGE(0),
        MEDIA_MESSAGE(1);

        public int code;

        Type(int code) {
            this.code = code;
        }
    }

    public enum State {
        WAITING_TO_SEND(0),
        SENDING(1),
        CANNOT_SEND(2),
        SENT(3);

        public int code;

        State(int code) {
            this.code = code;
        }
    }

    public Message() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Friend getSender() {
        return sender;
    }

    public void setSender(Friend sender) {
        this.sender = sender;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public RealmList<Receiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(RealmList<Receiver> receivers) {
        this.receivers = receivers;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    public MediaMessage getMediaMessage() {
        return mediaMessage;
    }

    public void setMediaMessage(MediaMessage mediaMessage) {
        this.mediaMessage = mediaMessage;
    }
}
