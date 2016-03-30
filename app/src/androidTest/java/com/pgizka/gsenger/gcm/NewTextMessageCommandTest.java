package com.pgizka.gsenger.gcm;


import com.google.gson.Gson;
import com.pgizka.gsenger.api.BaseResponse;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.conversationView.ConversationPresenter;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.gcm.data.NewMessageData;
import com.pgizka.gsenger.gcm.data.NewTextMessageData;
import com.pgizka.gsenger.jobqueue.setMessageState.MessageStateChangedRequest;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.Receiver;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.createChatBetweenUsers;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getApplication;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class NewTextMessageCommandTest {

    @Inject
    MessageRestService messageRestService;

    private NewTextMessageCommand textMessageCommand;
    private GSengerApplication gSengerApplication;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        gSengerApplication = getApplication();
        TestApplicationComponent applicationComponent = getTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);

        textMessageCommand = new NewTextMessageCommand();
    }

    @Test
    public void testReceivingTextMessage_whenChatAndUsersExists() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();
        createChatBetweenUsers(owner, sender);

        int messageServerId = 15;
        String data = new Gson().getAdapter(NewTextMessageData.class).toJson(prepareTextMessageData(sender, messageServerId));

        when(messageRestService.setMessageDelivered(Mockito.<MessageStateChangedRequest>any())).thenReturn(createCall(new BaseResponse()));
        textMessageCommand.execute(gSengerApplication, NewTextMessageData.ACTION, data);

        verifyNewTextMessageHandledCorrectly(messageServerId);
    }

    @Test
    public void testReceivingTextMessage_whenUsersExistsAndChatNotExist() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();

        int messageServerId = 15;
        String data = new Gson().getAdapter(NewTextMessageData.class).toJson(prepareTextMessageData(sender, messageServerId));

        when(messageRestService.setMessageDelivered(Mockito.<MessageStateChangedRequest>any())).thenReturn(createCall(new BaseResponse()));
        textMessageCommand.execute(gSengerApplication, NewTextMessageData.ACTION, data);

        verifyNewTextMessageHandledCorrectly(messageServerId);
    }

    private NewTextMessageData prepareTextMessageData(User sender, int messageServerId) {
        NewTextMessageData textMessageData = new NewTextMessageData();
        textMessageData.setText("MessageText");
        prepareMessageData(textMessageData, sender, messageServerId);
        return textMessageData;
    }

    private void prepareMessageData(NewMessageData messageData, User sender, int messageServerId) {
        messageData.setSenderId(sender.getServerId());
        messageData.setSendDate(System.currentTimeMillis());
        messageData.setMessageId(messageServerId);
        messageData.setChatId(-1);
    }

    private void verifyNewTextMessageHandledCorrectly(int messageServerId) throws Exception {
        verify(messageRestService, timeout(2000)).setMessageDelivered(Mockito.<MessageStateChangedRequest>any());

        Realm realm = Realm.getDefaultInstance();
        realm.refresh();
        Message message = realm.where(Message.class)
                .equalTo("serverId", messageServerId)
                .findFirst();

        assertNotNull(message);
        TextMessage textMessage = message.getTextMessage();
        assertNotNull(textMessage);

        Receiver receiver = realm.where(Receiver.class)
                .equalTo("user.id", getOrCreateOwner().getId())
                .equalTo("message.id", message.getId())
                .findFirst();

        assertNotNull(receiver);
    }

}