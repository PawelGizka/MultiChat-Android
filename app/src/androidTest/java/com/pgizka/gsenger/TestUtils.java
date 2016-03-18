package com.pgizka.gsenger;

import android.content.Context;

import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.pojos.Friend;

public class TestUtils {


    public static void cleanDB(Context context){
        context.getContentResolver().delete(GSengerContract.CommonTypes.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Friends.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.ToFriends.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Chats.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.FriendHasChats.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Messages.CONTENT_URI, null, null);
        context.getContentResolver().delete(GSengerContract.Medias.CONTENT_URI, null, null);
    }

    public static Friend prepareFriendStub() {
        Friend friend = new Friend();
        friend.setUserName("pawel");
        friend.setServerId(34);
        friend.setPhotoPath("photoPath");
        friend.setAddedDate(123);
        friend.setLastLoggedDate(1234);
        friend.setStatus("status");
        friend.setPhotoHash("photoHash");

        return friend;
    }

}
