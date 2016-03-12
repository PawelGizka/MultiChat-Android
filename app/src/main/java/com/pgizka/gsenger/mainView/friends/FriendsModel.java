package com.pgizka.gsenger.mainView.friends;

import android.database.Cursor;

import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.repositories.FriendRepository;

import java.util.ArrayList;
import java.util.List;

public class FriendsModel {

    private List<Friend> friends;

    public boolean readDataFromCursor(Cursor cursor) {
        friends = new ArrayList<>();

        if(!cursor.moveToFirst()){
            return true;
        }

        do {
            friends.add(FriendRepository.makeFriend(cursor));
        } while (cursor.moveToNext());


        return true;
    }

    public List<Friend> getFriends() {
        return friends;
    }
}
