package com.pgizka.gsenger.provider;


import com.google.gson.Gson;
import com.pgizka.gsenger.api.BaseResponse;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.data.NewMessageData;
import com.pgizka.gsenger.gcm.data.NewTextMessageData;
import com.pgizka.gsenger.jobqueue.setMessageState.MessageStateChangedRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.TestUtils.*;
import static com.pgizka.gsenger.TestUtils.createChatBetweenUsers;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getApplication;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        User sender = createUser();
        createChatBetweenUsers(owner, sender);

        int messageServerId = 15;
        String data = new Gson().getAdapter(NewMessageData.class).toJson(prepareMessageData(sender, messageServerId));

        realm.refresh();
        realm.beginTransaction();

        messageRepository.handleIncomingMessage(data);

        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId);
    }

    @Test
    public void testReceivingTextMessage_whenUsersExistsAndChatNotExist() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();

        int messageServerId = 15;
        String data = new Gson().getAdapter(NewMessageData.class).toJson(prepareMessageData(sender, messageServerId));

        realm.refresh();
        realm.beginTransaction();

        messageRepository.handleIncomingMessage(data);

        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId);
    }

    private void verifyNewMessageHandledCorrectly(int messageServerId) throws Exception {
        Realm realm = Realm.getDefaultInstance();
        realm.refresh();
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
