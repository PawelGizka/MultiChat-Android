package com.pgizka.gsenger.conversationView;


import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.pojos.Media;
import com.pgizka.gsenger.provider.pojos.Message;
import com.pgizka.gsenger.provider.repositories.FriendRepository;
import com.pgizka.gsenger.provider.repositories.MediaRepository;
import com.pgizka.gsenger.provider.repositories.MessageRepository;
import com.pgizka.gsenger.provider.repositories.ToFriendRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

public class ConversationModel {

    private List<ConversationItem> conversationItems;

    @Inject
    MessageRepository messageRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    FriendRepository friendRepository;

    @Inject
    ToFriendRepository toFriendRepository;

    public ConversationModel() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    public void readDataFromCursor(Cursor cursor){
        conversationItems = new ArrayList<>();

        if(!cursor.moveToFirst()){
            return;
        }

        do {
            conversationItems.add(makeConversationItem(cursor));
        } while (cursor.moveToNext());

        return;
    }

    private ConversationItem makeConversationItem(Cursor cursor) {
        ConversationItem conversationItem = new ConversationItem();

        conversationItem.setSenderFriend(friendRepository.buildFriend(cursor));
        conversationItem.setToFriend(toFriendRepository.buildToFriend(cursor));

        Message message = messageRepository.buildMessage(cursor);
        Media media = mediaRepository.buildMedia(cursor);

        if (message != null) {
            conversationItem.setCommonType(message);
        } else {
            conversationItem.setCommonType(media);
        }

        return  conversationItem;
    }

    public List<ConversationItem> getConversationItems() {
        return conversationItems;
    }
}
