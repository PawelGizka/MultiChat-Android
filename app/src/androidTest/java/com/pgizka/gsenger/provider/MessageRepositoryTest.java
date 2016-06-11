package com.pgizka.gsenger.provider;


import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.TestUtils;
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

import static com.pgizka.gsenger.TestUtils.createChatBetweenUsers;
import static com.pgizka.gsenger.TestUtils.createGroupChat;
import static com.pgizka.gsenger.TestUtils.createNotPersistedUser;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getApplication;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.prepareMessageData;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MessageRepositoryTest {

    @Inject
    MessageRepository messageRepository;

    private GSengerApplication gSengerApplication;
    private Realm realm;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        realm = Realm.getDefaultInstance();
        gSengerApplication = getApplication();
        TestApplicationComponent applicationComponent = getTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);
    }

    @Test
    public void testReceivingMessage_whenChatAndUsersExists() throws Exception {
        User owner = getOrCreateOwner();
        User sender = TestUtils.createUser();
        createChatBetweenUsers(owner, sender);

        int messageServerId = 15;
        realm.beginTransaction();

        messageRepository.handleIncomingMessage(prepareMessageData(sender, messageServerId));

        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId);
    }

    @Test
    public void testReceivingMessage_whenUsersExistsAndChatNotExist() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();

        int messageServerId = 15;
        realm.beginTransaction();

        messageRepository.handleIncomingMessage(prepareMessageData(sender, messageServerId));

        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId);
    }

    @Test
    public void testReceivingMessage_whenSenderNotExists() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createNotPersistedUser();

        int messageServerId = 15;
        realm.beginTransaction();

        messageRepository.handleIncomingMessage(prepareMessageData(sender, messageServerId));

        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId);
        User persistedSender = realm.where(User.class)
                .equalTo("serverId", sender.getServerId())
                .findFirst();
        assertNotNull(persistedSender);
    }

    @Test
    public void testReceivingMessage_inGroupChat() throws Exception {
        User owner = getOrCreateOwner();
        User user1 = createUser();
        User user2 = createUser();

        List<User> participants = new ArrayList<>();
        participants.add(owner);
        participants.add(user1);
        participants.add(user2);

        Chat chat = createGroupChat(participants);

        int messageServerId = 15;

        realm.beginTransaction();

        messageRepository.handleIncomingMessage(prepareMessageData(chat, user1, messageServerId));

        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId);
    }

    private void verifyNewMessageHandledCorrectly(int messageServerId) throws Exception {
        Realm realm = Realm.getDefaultInstance();
        Message message = realm.where(Message.class)
                .equalTo("serverId", messageServerId)
                .findFirst();

        assertNotNull(message);

        Receiver receiver = realm.where(Receiver.class)
                .equalTo("user.id", getOrCreateOwner().getId())
                .equalTo("message.id", message.getId())
                .findFirst();

        assertNotNull(receiver);
    }

}
