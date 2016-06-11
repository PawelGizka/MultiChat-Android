package com.pgizka.gsenger.gcm;


import com.google.gson.Gson;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.gcm.commands.NewGroupChatCommand;
import com.pgizka.gsenger.gcm.data.NewChatData;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.TestUtils.createNotPersistedUser;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getApplication;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NewGroupChatCommandTest {

    private NewGroupChatCommand newGroupChatCommand;

    private GSengerApplication gSengerApplication;

    @Inject
    Gson gson;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        gSengerApplication = getApplication();
        TestApplicationComponent applicationComponent = getTestApplicationComponent();
        GSengerApplication.setApplicationComponent(applicationComponent);
        applicationComponent.inject(this);

        newGroupChatCommand = new NewGroupChatCommand();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testHandlingNewChat() throws Exception {
        User owner = getOrCreateOwner();
        User participant1 = createUser();
        User participant2 = createNotPersistedUser();

        int chatId = 15;
        String chatName = "Sample Chat";
        NewChatData chatData = new NewChatData();
        chatData.setName(chatName);
        chatData.setStartedDate(System.currentTimeMillis());
        chatData.setChatId(chatId);
        List<User> participants = new ArrayList<>();
        participants.add(new User(owner));
        participants.add(new User(participant1));
        participants.add(new User(participant2));
        chatData.setParticipants(participants);

        String data = gson.toJson(chatData);
        newGroupChatCommand.execute(gSengerApplication, NewChatData.ACTION, data);

        Realm realm = Realm.getDefaultInstance();

        Chat chat = realm.where(Chat.class)
                .equalTo("serverId", chatId)
                .findFirst();

        assertNotNull(chat);
        int numberOfParticipants = 3;
        assertEquals(numberOfParticipants, chat.getUsers().size());
        assertEquals(chatName, chat.getChatName());
    }

}
