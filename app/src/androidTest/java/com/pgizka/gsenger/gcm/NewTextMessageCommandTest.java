package com.pgizka.gsenger.gcm;


import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.dtos.messages.MessagesStateChangedRequest;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;

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

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getApplication;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.prepareMessageData;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class NewTextMessageCommandTest {

    @Inject
    MessageRestService messageRestService;

    @Inject
    Gson gson;

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
        String data = gson.toJson(prepareTextMessageData(sender, messageServerId));

        when(messageRestService.updateMessageState(Mockito.<MessagesStateChangedRequest>any())).thenReturn(createCall(responseBody));
        textMessageCommand.execute(gSengerApplication, TextMessageData.ACTION, data);

        verifyNewTextMessageHandledCorrectly(messageServerId);
    }

    private TextMessageData prepareTextMessageData(User sender, int messageServerId) {
        TextMessageData textMessageData = new TextMessageData();
        textMessageData.setText("MessageText");
        prepareMessageData(textMessageData, sender, messageServerId);
        return textMessageData;
    }

    private void verifyNewTextMessageHandledCorrectly(int messageServerId) throws Exception {
        verify(messageRestService, timeout(2000)).updateMessageState(Mockito.<MessagesStateChangedRequest>any());

        Realm realm = Realm.getDefaultInstance();
        Message message = realm.where(Message.class).equalTo("serverId", messageServerId).findFirst();

        assertNotNull(message);
        TextMessage textMessage = message.getTextMessage();
        assertNotNull(textMessage);
        assertNotNull(textMessage.getText());
    }

}
