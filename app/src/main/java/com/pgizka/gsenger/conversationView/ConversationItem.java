package com.pgizka.gsenger.conversationView;

import com.pgizka.gsenger.provider.pojos.CommonType;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.pojos.ToFriend;

public class ConversationItem {

    private Friend senderFriend;
    private CommonType commonType;
    private ToFriend toFriend;

    public ConversationItem() {
    }

    public Friend getSenderFriend() {
        return senderFriend;
    }

    public void setSenderFriend(Friend senderFriend) {
        this.senderFriend = senderFriend;
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
