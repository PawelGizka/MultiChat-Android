package com.pgizka.gsenger.gcm;


import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.api.dtos.messages.MediaMessageData;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.gcm.commands.NewMediaMessageCommand;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;
import com.pgizka.gsenger.jobqueue.setMessageState.UpdateMessageStateJob;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import javax.inject.Inject;

import static android.support.test.espresso.core.deps.guava.base.Verify.verify;
import static com.pgizka.gsenger.TestUtils.getApplication;
import static com.pgizka.gsenger.TestUtils.getDefaultTestApplicationComponent;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class NewMessageCommandTest {

    @Inject
    Gson gson;

    @Inject
    JobManager jobManager;

    @Inject
    MessageRepository messageRepository;

    @Mock
    TextMessageData textMessageData;

    @Mock
    MediaMessageData mediaMessageData;

    private NewTextMessageCommand textMessageCommand;
    private NewMediaMessageCommand mediaMessageCommand;
    private GSengerApplication gSengerApplication;

    @Before
    public void setUp() throws IOException {
        gSengerApplication = getApplication();
        TestApplicationComponent applicationComponent = getDefaultTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);

        textMessageCommand = new NewTextMessageCommand();
        mediaMessageCommand = new NewMediaMessageCommand();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReceivingTextMessage() throws Exception {
        when(messageRepository.handleIncomingTextMessage(any())).thenReturn(new Message());

        String data = gson.toJson(textMessageData);
        textMessageCommand.execute(gSengerApplication, TextMessageData.ACTION, data);

        verify(messageRepository).handleIncomingTextMessage(any());
        verify(jobManager).addJob(new UpdateMessageStateJob(any()));
    }

    @Test
    public void testReceivingMediaMessage() throws Exception {
        when(messageRepository.handleIncomingMediaMessage(any())).thenReturn(new Message());

        String data = gson.toJson(mediaMessageData);
        mediaMessageCommand.execute(gSengerApplication, TextMessageData.ACTION, data);

        Mockito.verify(messageRepository).handleIncomingMediaMessage(any());
        verify(jobManager, times(2)).addJob(new UpdateMessageStateJob(any()));
    }

}
