package com.pgizka.gsenger.provider;


import android.support.test.runner.AndroidJUnit4;

import com.pgizka.gsenger.TestUtils;
import com.pgizka.gsenger.api.dtos.messages.MediaMessageData;
import com.pgizka.gsenger.api.dtos.messages.MessageData;
import com.pgizka.gsenger.api.dtos.messages.MessagesStateChangedRequest;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.dagger.TestApplicationComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

import static com.pgizka.gsenger.TestUtils.createCall;
import static com.pgizka.gsenger.TestUtils.createChatBetweenUsers;
import static com.pgizka.gsenger.TestUtils.createGroupChat;
import static com.pgizka.gsenger.TestUtils.createMessageWithReceiverInfo;
import static com.pgizka.gsenger.TestUtils.createNotPersistedUser;
import static com.pgizka.gsenger.TestUtils.createUser;
import static com.pgizka.gsenger.TestUtils.getOrCreateOwner;
import static com.pgizka.gsenger.TestUtils.getDefaultTestApplicationComponent;
import static com.pgizka.gsenger.TestUtils.prepareMessageData;
import static com.pgizka.gsenger.TestUtils.prepareReceiversInfoData;
import static com.pgizka.gsenger.TestUtils.setupRealm;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MessageRepositoryTest {

    @Inject
    MessageRepository messageRepository;

    private Realm realm;

    @Before
    public void setUp() throws IOException {
        setupRealm();
        realm = Realm.getDefaultInstance();
        TestApplicationComponent applicationComponent = getDefaultTestApplicationComponent();
        applicationComponent.inject(this);
        GSengerApplication.setApplicationComponent(applicationComponent);
    }

    @Test
    public void testReceivingTextMessage() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();

        int messageServerId = 15;

        realm.beginTransaction();
        messageRepository.handleIncomingTextMessage(prepareTextMessageData(sender, messageServerId));
        realm.commitTransaction();

        verifyNewTextMessageHandledCorrectly(messageServerId);
    }

    private TextMessageData prepareTextMessageData(User sender, int messageServerId) {
        TextMessageData textMessageData = new TextMessageData();
        textMessageData.setText("MessageText");
        prepareMessageData(textMessageData, sender, messageServerId);
        return textMessageData;
    }

    private void verifyNewTextMessageHandledCorrectly(int messageServerId) throws Exception {
        Message message = realm.where(Message.class).equalTo("serverId", messageServerId).findFirst();

        assertNotNull(message);
        TextMessage textMessage = message.getTextMessage();
        assertNotNull(textMessage);
        assertNotNull(textMessage.getText());
    }

    @Test
    public void testReceivingMediaMessage() throws Exception {
        User owner = getOrCreateOwner();
        User sender = TestUtils.createUser();

        int messageServerId = 15;

        realm.beginTransaction();
        messageRepository.handleIncomingMediaMessage(prepareMediaMessageData(sender, messageServerId));
        realm.commitTransaction();

        verifyNewMediaMessageHandledCorrectly(messageServerId);
    }

    private MediaMessageData prepareMediaMessageData(User sender, int messageServerId) {
        MediaMessageData mediaMessageData = new MediaMessageData();
        mediaMessageData.setFileName("fileName");
        mediaMessageData.setType(MediaMessage.Type.PHOTO.code);
        mediaMessageData.setDescription("description");
        prepareMessageData(mediaMessageData, sender, messageServerId);
        return mediaMessageData;
    }

    private void verifyNewMediaMessageHandledCorrectly(int messageServerId) throws Exception {
        Message message = realm.where(Message.class).equalTo("serverId", messageServerId).findFirst();

        assertNotNull(message);
        MediaMessage mediaMessage = message.getMediaMessage();
        assertNotNull(mediaMessage);
        assertNotNull(mediaMessage.getFileName());
        assertNotNull(mediaMessage.getDescription());
    }

    @Test
    public void testReceivingMessage_whenChatAndUsersExists() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();
        createChatBetweenUsers(owner, sender);

        int messageServerId = 15;

        realm.beginTransaction();
        messageRepository.handleIncomingMessage(prepareMessageData(sender, messageServerId));
        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId, sender, owner);
    }

    @Test
    public void testReceivingMessage_whenUsersExistsAndChatNotExist() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();

        int messageServerId = 15;

        realm.beginTransaction();
        messageRepository.handleIncomingMessage(prepareMessageData(sender, messageServerId));
        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId, sender, owner);
    }

    @Test
    public void testReceivingMessage_whenSenderAndChatNotExist() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createNotPersistedUser();

        int messageServerId = 15;

        realm.beginTransaction();
        messageRepository.handleIncomingMessage(prepareMessageData(sender, messageServerId));
        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId, sender, owner);

        User persistedSender = realm.where(User.class).equalTo("serverId", sender.getServerId()).findFirst();
        assertNotNull(persistedSender);
    }

    @Test
    public void testReceivingMessage_inGroupChatWithoutReceiversData() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();
        User user1 = createUser();

        List<User> participants = new ArrayList<>();
        participants.add(owner);
        participants.add(sender);
        participants.add(user1);

        Chat chat = createGroupChat(participants);

        int messageServerId = 15;

        realm.beginTransaction();
        messageRepository.handleIncomingMessage(prepareMessageData(chat, sender, messageServerId));
        realm.commitTransaction();

        List<User> receivers = new ArrayList<>();
        receivers.add(owner);
        receivers.add(user1);

        verifyNewMessageHandledCorrectly(messageServerId, sender, receivers);
    }

    @Test
    public void testReceivingMessage_inGroupChatWithReceiversData() throws Exception {
        User owner = getOrCreateOwner();
        User sender = createUser();
        User user1 = createUser();

        List<User> participants = new ArrayList<>();
        participants.add(owner);
        participants.add(sender);
        participants.add(user1);

        Chat chat = createGroupChat(participants);

        List<User> receivers = new ArrayList<>();
        receivers.add(owner);
        receivers.add(user1);

        int messageServerId = 15;
        long deliveredDate = 1234;

        MessageData messageData = prepareMessageData(chat, sender, messageServerId);
        messageData.setReceiversInfoData(prepareReceiversInfoData(messageServerId, receivers, deliveredDate));

        realm.beginTransaction();
        messageRepository.handleIncomingMessage(messageData);
        realm.commitTransaction();

        verifyNewMessageHandledCorrectly(messageServerId, sender, receivers);
    }

    private void verifyNewMessageHandledCorrectly(int messageServerId, User sender, User receiver) throws Exception {
        List<User> receivers = new ArrayList<>(1);
        receivers.add(receiver);
        verifyNewMessageHandledCorrectly(messageServerId, sender, receivers);
    }

    private void verifyNewMessageHandledCorrectly(int messageServerId, User sender, List<User> receivers) throws Exception {
        Realm realm = Realm.getDefaultInstance();

        Message message = realm.where(Message.class).equalTo("serverId", messageServerId).findFirst();
        assertNotNull(message);

        Chat chat = message.getChat();
        assertNotNull(chat);

        assertEquals(receivers.size(), message.getReceiverInfos().size());

        for (User expectedReceiver : receivers) {
            ReceiverInfo actualReceiverInfo = realm.where(ReceiverInfo.class)
                    .equalTo("user.id", expectedReceiver.getId())
                    .equalTo("message.id", message.getId())
                    .findFirst();

            assertNotNull(actualReceiverInfo);
            assertEquals(message, actualReceiverInfo.getMessage());
            assertEquals(expectedReceiver, actualReceiverInfo.getUser());
        }
    }

    @Test
    public void testSettingAllMessagesToViewed() throws Exception {
        User user = createUser();
        User owner = getOrCreateOwner();
        Chat chat = createChatBetweenUsers(owner, user);
        Message message1 = createMessageWithReceiverInfo(chat, user, owner);
        Message message2 = createMessageWithReceiverInfo(chat, user, owner);

        realm.beginTransaction();
        List<Message> messages = messageRepository.setMessagesViewed(chat.getId());
        realm.commitTransaction();
        assertEquals(2, messages.size());

        realm.beginTransaction();
        ReceiverInfo receiverInfo = realm.where(ReceiverInfo.class)
                .equalTo("message.id", message1.getId())
                .equalTo("user.id", owner.getId())
                .findFirst();

        assertNotSame(0, receiverInfo.getViewed());
        realm.commitTransaction();
    }

}
