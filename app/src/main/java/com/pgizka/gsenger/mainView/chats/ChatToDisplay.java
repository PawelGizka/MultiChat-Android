package com.pgizka.gsenger.mainView.chats;

import com.pgizka.gsenger.provider.pojos.Chat;
import com.pgizka.gsenger.provider.pojos.CommonType;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.pojos.Media;
import com.pgizka.gsenger.provider.pojos.Message;
import com.pgizka.gsenger.provider.pojos.ToFriend;

public class ChatToDisplay {

    private Chat chat;
    private Friend friend;
    private CommonType commonType;
    private ToFriend toFriend;

    public ChatToDisplay() {

    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public CommonType getCommonType() {
        return commonType;
    }

    public void setCommonType(CommonType commonType) {
        this.commonType = commonType;
    }

    public ToFriend getToFriend() {
        return toFriend;
    }

    public void setToFriend(ToFriend toFriend) {
        this.toFriend = toFriend;
    }
}

