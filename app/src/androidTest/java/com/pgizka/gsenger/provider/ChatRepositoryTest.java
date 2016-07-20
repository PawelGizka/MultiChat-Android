package com.pgizka.gsenger.provider;

import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.api.dtos.chats.ChatData;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.dagger.TestApplicationComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.TestUtils.*;
import static junit.framework.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ChatRepositoryTest {

    @Inject
    ChatRepository chatRepository;

    private Realm realm;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        realm = Realm.getDefaultInstance();
        TestApplicationComponent applicationComponent = getTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);
    }

    @Test
    public void testAddingOtherUsersToChat() throws Exception {
        //initial data
        User owner = getOrCreateOwner();
        User user1 = createUser();
        User user2 = createUser();

        List<User> participants = new ArrayList<>(2);
        participants.add(user1);
        participants.add(user2);
        participants.add(owner);
        Chat chat = createGroupChat(participants);

        createMessageWithReceiverInfo(chat, user2, owner);
        createMessageWithReceiverInfo(chat, user1, owner);

        //data after adding user
        User addedUser = createUser();
        participants.add(addedUser);

        ChatData chatData = new ChatData();
        chatData.setChatId(chat.getServerId());
        chatData.setName(chat.getChatName());
        chatData.setStartedDate(chat.getStartedDate());
        chatData.setParticipants(participants);

        realm.beginTransaction();
        chatRepository.addUsersToChat(chatData);
        realm.commitTransaction();

        assertEquals(participants.size(), chat.getUsers().size());
    }

}
