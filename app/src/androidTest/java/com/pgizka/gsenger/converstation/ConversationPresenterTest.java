package com.pgizka.gsenger.converstation;


import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.dtos.messages.MessagesStateChangedRequest;
import com.pgizka.gsenger.api.dtos.messages.PutMessageResponse;
import com.pgizka.gsenger.api.dtos.messages.PutTextMessageRequest;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.conversationView.ConversationContract;
import com.pgizka.gsenger.conversationView.ConversationPresenter;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.ResponseBody;

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.createChatBetweenUsers;
import static com.pgizka.gsenger.TestUtils.createMessage;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ConversationPresenterTest  {

    @Mock
    ConversationContract.View view;

    @Mock
    ResponseBody responseBody;

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

        int messageServerId = 12;
        PutMessageResponse putMessageResponse = new PutMessageResponse(messageServerId);

        int chatId = -1;
        conversationPresenter.onCreate(view, user.getId(), chatId);
        conversationPresenter.setFriend(user);
        conversationPresenter.setOwner(owner);

        when(messageRestService.sendTextMessage(Matchers.<PutTextMessageRequest>any()))
                .thenReturn(createCall(putMessageResponse));

        conversationPresenter.sendMessage("message");

        verify(messageRestService, after(2000)).sendTextMessage(Matchers.<PutTextMessageRequest>any());

        realm.beginTransaction();
        Message message = realm.where(Message.class)
                .equalTo("serverId", messageServerId)
                .findFirst();
        realm.commitTransaction();

        Assert.assertNotNull(message);
        Assert.assertEquals(Message.State.SENT.code, message.getState());
    }

    @Test
    public void testSettingAllMessagesToViewed() throws Exception {
        User user = createUser();
        User owner = getOrCreateOwner();
        Chat chat = createChatBetweenUsers(owner, user);
        createMessage(user, chat);
        createMessage(user, chat);

        conversationPresenter.onCreate(view, user.getId(), chat.getId());
        conversationPresenter.setOwner(owner);
        conversationPresenter.setFriend(user);
        conversationPresenter.setChat(chat);

        when(messageRestService.updateMessageState(Mockito.<MessagesStateChangedRequest>any())).thenReturn(createCall(responseBody));
        conversationPresenter.setAllMessagesViewed();

        verify(messageRestService, timeout(4000).times(1)).updateMessageState(Mockito.<MessagesStateChangedRequest>any());
    }

}
