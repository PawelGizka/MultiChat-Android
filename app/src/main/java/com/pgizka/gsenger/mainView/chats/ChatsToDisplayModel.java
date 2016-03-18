package com.pgizka.gsenger.mainView.chats;

import android.database.Cursor;

import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.pojos.Media;
import com.pgizka.gsenger.provider.pojos.Message;
import com.pgizka.gsenger.provider.repositories.ChatRepository;
import com.pgizka.gsenger.provider.repositories.FriendRepository;
import com.pgizka.gsenger.provider.repositories.MediaRepository;
import com.pgizka.gsenger.provider.repositories.MessageRepository;
import com.pgizka.gsenger.provider.repositories.ToFriendRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChatsToDisplayModel {

    private List<ChatToDisplay> chatToDisplays;

    @Inject
    ChatRepository chatRepository;

    @Inject
    FriendRepository friendRepository;

    @Inject
    MessageRepository messageRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    ToFriendRepository toFriendRepository;

    public ChatsToDisplayModel() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    public void readDataFromCursor(Cursor cursor){
        chatToDisplays = new ArrayList<>();

        if(!cursor.moveToFirst()){
            return;
        }

        do {
            chatToDisplays.add(makeChat(cursor));
        } while (cursor.moveToNext());
    }

    private ChatToDisplay makeChat(Cursor cursor) {
        ChatToDisplay chatToDisplay = new ChatToDisplay();

        chatToDisplay.setChat(chatRepository.buildChat(cursor));
        chatToDisplay.setFriend(friendRepository.buildFriend(cursor));

        Message message = messageRepository.buildMessage(cursor);
        Media media = mediaRepository.buildMedia(cursor);

        if (message != null) {
            chatToDisplay.setCommonType(message);
        } else {
            chatToDisplay.setCommonType(media);
        }

        chatToDisplay.setToFriend(toFriendRepository.buildToFriend(cursor));

        return chatToDisplay;
    }


    public List<ChatToDisplay> getChatToDisplays() {
        return chatToDisplays;
    }

    public void setChatToDisplays(List<ChatToDisplay> chatToDisplays) {
        this.chatToDisplays = chatToDisplays;
    }
}
