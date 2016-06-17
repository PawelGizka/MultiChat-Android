package com.pgizka.gsenger.addUsersToChatView;

import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.api.ChatRestService;
import com.pgizka.gsenger.api.dtos.chats.AddUsersToChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatResponse;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.createChatsView.CreateChatPresenter;
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
import okhttp3.ResponseBody;

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.createGroupChat;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class AddUsersToChatPresenterTest {

    @Mock
    AddUsersToChatContract.View view;

    @Mock
    ResponseBody responseBody;

    @Inject
    ChatRestService chatRestService;

    private AddUsersToChatPresenter addUsersToChatPresenter;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        TestApplicationComponent applicationComponent = getTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);

        addUsersToChatPresenter = new AddUsersToChatPresenter();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatingChat() throws Exception {
        getOrCreateOwner();

        List<User> participants = new ArrayList<>();
        participants.add(createUser());
        participants.add(createUser());

        Chat chat = createGroupChat(participants);

        when(chatRestService.addUsersToChat(Mockito.<AddUsersToChatRequest>any())).thenReturn(createCall(responseBody));

        addUsersToChatPresenter.onCreate(view, chat.getId());

        List<User> additionalUsers = new ArrayList<>();
        additionalUsers.add(createUser());
        additionalUsers.add(createUser());

        addUsersToChatPresenter.addUsers(additionalUsers);

        verify(chatRestService, timeout(2000)).addUsersToChat(Mockito.<AddUsersToChatRequest>any());

        int numberOfChatParticipants = 4;
        assertEquals(numberOfChatParticipants, chat.getUsers().size());
    }


}
