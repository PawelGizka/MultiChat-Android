package com.pgizka.gsenger.addUsersToChatView;

import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.api.ChatRestService;
import com.pgizka.gsenger.api.dtos.chats.AddUsersToChatRequest;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.dagger.TestApiModule;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.dagger.TestApplicationModule;
import com.pgizka.gsenger.dagger.TestRepositoryModule;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
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

import okhttp3.ResponseBody;

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.createGroupChat;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getApplication;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getDefaultTestApplicationComponent;
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

    @Inject
    ChatRepository chatRepository;

    private AddUsersToChatPresenter addUsersToChatPresenter;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        GSengerApplication application = getApplication();
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
        addUsersToChatPresenter.onCreate(view, chat.getId());

        List<User> additionalUsers = new ArrayList<>();
        additionalUsers.add(createUser());
        additionalUsers.add(createUser());

        when(chatRestService.addUsersToChat(Mockito.<AddUsersToChatRequest>any())).thenReturn(createCall(responseBody));

        addUsersToChatPresenter.addUsers(additionalUsers);

        verify(chatRestService, timeout(2000)).addUsersToChat(Mockito.<AddUsersToChatRequest>any());
        verify(chatRepository).addUsersToChat(chat, additionalUsers);
    }


}
