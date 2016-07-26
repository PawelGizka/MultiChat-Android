package com.pgizka.gsenger.converstation;


import android.support.test.runner.AndroidJUnit4;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.dtos.messages.MessagesStateChangedRequest;
import com.pgizka.gsenger.api.dtos.messages.PutMessageResponse;
import com.pgizka.gsenger.api.dtos.messages.PutTextMessageRequest;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.conversationView.ConversationContract;
import com.pgizka.gsenger.conversationView.ConversationPresenter;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.jobqueue.sendMessge.SendMessageJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;
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
import static com.pgizka.gsenger.TestUtils.createMessageWithReceiverInfo;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getDefaultTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.getTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ConversationPresenterTest  {

    @Mock
    ConversationContract.View view;

    @Inject
    MessageRepository messageRepository;

    @Inject
    ChatRepository chatRepository;

    @Inject
    JobManager jobManager;

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
    public void testSendingTextMessage() throws Exception {
        Realm realm = Realm.getDefaultInstance();
        User user = createUser();
        User owner = getOrCreateOwner();

        int chatId = -1;
        conversationPresenter.onCreate(view, chatId);

        when(messageRepository.createOutgoingTextMessageWithReceivers(any(), "message")).thenReturn(new Message());

        conversationPresenter.sendMessage("message");

        verify(messageRepository).createOutgoingTextMessageWithReceivers(any(), "message");
        verify(jobManager).addJobInBackground(new SendMessageJob(any()));
    }

}
