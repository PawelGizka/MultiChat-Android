package com.pgizka.gsenger.createChatsView;

import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.api.ChatRestService;
import com.pgizka.gsenger.api.dtos.chats.PutChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatResponse;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getDefaultTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CreateChatPresenterTest {

    @Mock
    CreateChatContract.View view;

    @Inject
    ChatRestService chatRestService;

    private CreateChatPresenter createChatPresenter;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        TestApplicationComponent applicationComponent = getDefaultTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);

        createChatPresenter = new CreateChatPresenter();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCreatingChat() throws Exception {
        getOrCreateOwner();
        User user1 = createUser();
        User user2 = createUser();

        int chatId = 15;
        when(chatRestService.createChat(Mockito.<PutChatRequest>any())).thenReturn(createCall(new PutChatResponse(chatId)));

        createChatPresenter.onCreate(view);

        String chatName = "Sample Chat";
        List<User> participants = new ArrayList<>();
        participants.add(user1);
        participants.add(user2);
        createChatPresenter.createChat(chatName, participants);

        verify(chatRestService, timeout(2000)).createChat(Mockito.<PutChatRequest>any());

        Realm realm = Realm.getDefaultInstance();
        Chat chat = realm.where(Chat.class)
                .equalTo("serverId", chatId)
                .findFirst();
        assertNotNull(chat);
        assertEquals(chat.getChatName(), chatName);
        int chatNumberOfParticipants = 3;
        assertEquals(chatNumberOfParticipants, chat.getUsers().size());
    }

}
