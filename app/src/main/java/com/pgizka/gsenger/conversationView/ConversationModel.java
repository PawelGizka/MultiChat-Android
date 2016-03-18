package com.pgizka.gsenger.conversationView;


import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ConversationModel {

    private List<ConversationItem> conversationItems;

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


        return  conversationItem;
    }

    public List<ConversationItem> getConversationItems() {
        return conversationItems;
    }
}
