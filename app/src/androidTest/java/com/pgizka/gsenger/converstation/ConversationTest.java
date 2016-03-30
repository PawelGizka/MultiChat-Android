package com.pgizka.gsenger.converstation;


import android.test.AndroidTestCase;

import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.ResultCode;
import com.pgizka.gsenger.conversationView.ConversationContract;
import com.pgizka.gsenger.conversationView.ConversationPresenter;
import com.pgizka.gsenger.dagger.DaggerTestApplicationComponent;
import com.pgizka.gsenger.dagger.TestApiModule;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.dagger2.ApplicationModule;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.jobqueue.sendMessge.PutMessageResponse;
import com.pgizka.gsenger.jobqueue.sendMessge.PutTextMessageRequest;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.TestUtils.*;
import static org.mockito.Mockito.*;

public class ConversationTest extends AndroidTestCase {

    @Mock
    ConversationContract.View view;

    @Inject
    MessageRestService messageRestService;

    private ConversationPresenter conversationPresenter;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        TestApplicationComponent applicationComponent = getTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);

        conversationPresenter = new ConversationPresenter();
    }

    @Test
    public void testSendingTextMessage_whenChatBetweenUsersNotExist() throws Exception {
        Realm realm = Realm.getDefaultInstance();
        User user = createUser();
        User owner = getOrCreateOwner();

        PutMessageResponse putMessageResponse = new PutMessageResponse();
        putMessageResponse.setResultCode(ResultCode.OK.code);
        putMessageResponse.setMessageServerId(12);

        conversationPresenter.onCreate(view, user.getId());
        conversationPresenter.setFriend(user);
        conversationPresenter.setOwner(owner);

        when(messageRestService.sendTextMessage(Matchers.<PutTextMessageRequest>any()))
                .thenReturn(createCall(putMessageResponse));

        conversationPresenter.sendMessage("message");

        verify(messageRestService, timeout(2000)).sendTextMessage(Matchers.<PutTextMessageRequest>any());

        realm.refresh();
        Message message = realm.where(Message.class)
                .equalTo("serverId", 12)
                .findFirst();

        assertNotNull(message);
        assertEquals(Message.State.SENT.code, message.getState());
    }

}
