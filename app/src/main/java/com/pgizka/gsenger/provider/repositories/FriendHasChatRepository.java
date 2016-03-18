package com.pgizka.gsenger.provider.repositories;

import android.content.Context;
import android.net.Uri;

import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ProviderUtils;

public class FriendHasChatRepository {

    private Context context;
    private ProviderUtils providerUtils;

    public FriendHasChatRepository(Context context, ProviderUtils providerUtils) {
        this.context = context;
        this.providerUtils = providerUtils;
    }

    public void insertFriendHasChat(int friendId, int chatId) {
        providerUtils.insertFriendHasChat(String.valueOf(friendId), String.valueOf(chatId));
    }

    public void deleteFriendHasChat(int friendId, int chatId) {
        Uri uri = GSengerContract.FriendHasChats.buildFriendHasChatUri(String.valueOf(friendId), String.valueOf(chatId));

        context.getContentResolver().delete(uri, null, null);
    }

}
