package com.pgizka.gsenger.converstation;


import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.TestUtils;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaContract;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaPresenter;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.jobqueue.sendMessge.PutMessageResponse;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.RequestBody;

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SendMediaPresenterTest {

    @Mock
    SendMediaContract.View view;

    @Mock
    Context context;

    @Inject
    MessageRestService messageRestService;

    private SendMediaPresenter sendMediaPresenter;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        TestApplicationComponent applicationComponent = getTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);

        sendMediaPresenter = new SendMediaPresenter();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendingMediaMessage_whenChatBetweenUsersNotExist() throws Exception {
        Realm realm = Realm.getDefaultInstance();
        User user = TestUtils.createUser();
        User owner = getOrCreateOwner();

        int messageServerId = 12;
        PutMessageResponse putMessageResponse = new PutMessageResponse(ResultCode.OK, messageServerId);

        int chatId = -1;
        sendMediaPresenter.onCreate(view, context, user.getId(), chatId);

        when(messageRestService.sendMediaMessage(Matchers.<RequestBody>any(), Matchers.<RequestBody>any()))
                .thenReturn(createCall(putMessageResponse));

        String fileName = "fileName";
        String path = "path/fileName";
        String description = "description";
        sendMediaPresenter.sendMediaMessage(MediaMessage.Type.PHOTO.code, fileName, path, description);

        verify(messageRestService, after(2000)).sendMediaMessage(Matchers.<RequestBody>any(), Matchers.<RequestBody>any());

        realm.refresh();
        Message message = realm.where(Message.class)
                .equalTo("serverId", 12)
                .findFirst();

        assertNotNull(message);
        assertEquals(Message.State.SENT.code, message.getState());
    }



}
