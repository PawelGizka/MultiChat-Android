package com.pgizka.gsenger.gcm;


import android.support.test.runner.AndroidJUnit4;

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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.ResponseBody;

import static com.pgizka.gsenger.TestUtils.*;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class NewTextMessageCommandTest {

    @Inject
    MessageRestService messageRestService;

    @Mock
    ResponseBody responseBody;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReceivingTextMessage() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();

        int messageServerId = 15;
        String data = new Gson().getAdapter(NewTextMessageData.class).toJson(prepareTextMessageData(sender, messageServerId));

        when(messageRestService.setMessageDelivered(Mockito.<MessageStateChangedRequest>any())).thenReturn(createCall(responseBody));
        textMessageCommand.execute(gSengerApplication, NewTextMessageData.ACTION, data);

        verifyNewTextMessageHandledCorrectly(messageServerId);
    }

    private NewTextMessageData prepareTextMessageData(User sender, int messageServerId) {
        NewTextMessageData textMessageData = new NewTextMessageData();
        textMessageData.setText("MessageText");
        prepareMessageData(textMessageData, sender, messageServerId);
        return textMessageData;
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
        assertNotNull(textMessage.getText());
    }

}
