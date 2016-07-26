package com.pgizka.gsenger.conversationView;

import android.support.annotation.VisibleForTesting;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.jobqueue.sendMessge.SendMessageJob;
import com.pgizka.gsenger.jobqueue.setMessageState.UpdateMessageStateJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ConversationPresenter implements ConversationContract.Presenter {
    static final String TAG = ConversationPresenter.class.getSimpleName();

    private ConversationContract.View conversationView;
    private Realm realm;

    private Chat chat;

    private boolean groupChat;

    private RealmResults<Message> messages;
    private boolean paused;

    @Inject
    JobManager jobManager;

    @Inject
    UserAccountManager userAccountManager;

    @Inject
    MessageRepository messageRepository;

    @Inject
    ChatRepository chatRepository;

    @Override
    public void onCreate(ConversationContract.View view, int chatId) {
        GSengerApplication.getApplicationComponent().inject(this);
        conversationView = view;
        realm = Realm.getDefaultInstance();
        chat = realm.where(Chat.class).equalTo("id", chatId).findFirst();
        groupChat = chat.getType() == Chat.Type.SINGLE_CONVERSATION.code;
    }

    @Override
    public void onDestroy() {
        conversationView = null;
    }

    @Override
    public void onResume() {
        paused = false;

        conversationView.setGroupChat(groupChat);
        fetchMessages();

        messages.addChangeListener(element -> {
            messages = element;
            conversationView.displayConversationItems(messages);
            setAllMessagesViewed();
        });

        conversationView.displayConversationItems(messages);

        setAllMessagesViewed();
        displayChatInfo();
    }

    private void displayChatInfo() {
        if (groupChat) {
            conversationView.displayChatName(chat.getChatName());
        } else {
            User friend = realm.where(User.class).equalTo("chats.id", chat.getId()).notEqualTo("id", 0).findFirst();
            if (friend.isInContacts()) {
                conversationView.displayChatName(friend.getUserName());
            } else {
                conversationView.displayChatName(String.valueOf(friend.getPhoneNumber()));
            }

            if (friend.getPhotoHash() != null) {
                conversationView.displayChatImage(friend);
            }
        }
    }

    @Override
    public void onPause() {
        paused = true;
        if (messages != null) {
            messages.removeChangeListeners();
        }
        realm.removeAllChangeListeners();
    }

    private void fetchMessages() {
        messages = realm.where(Message.class)
                .equalTo("chat.id", chat.getId())
                .findAllSorted("sendDate", Sort.ASCENDING);
    }

    @VisibleForTesting
    public void setAllMessagesViewed() {
        if (paused) {
            return;
        }

        realm.beginTransaction();
        List<Message> viewedMessages = messageRepository.setMessagesViewed(chat.getId());
        realm.commitTransaction();

        List<Integer> messagesIds = new ArrayList<>(viewedMessages.size());
        for (Message message : viewedMessages) {
            messagesIds.add(message.getId());
        }

        if (!messagesIds.isEmpty()) {
            jobManager.addJobInBackground(new UpdateMessageStateJob(messagesIds));
        }
    }

    @Override
    public void sendMessage(String text) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Message message = messageRepository.createOutgoingTextMessageWithReceivers(chat, text);
        realm.commitTransaction();

        jobManager.addJob(new SendMessageJob(message.getId()));
    }

    @Override
    public void messageClicked(Message message) {
        if (message.getType() == Message.Type.MEDIA_MESSAGE.code) {
            MediaMessage mediaMessage = message.getMediaMessage();
            int type = mediaMessage.getMediaType();
            if (type == MediaMessage.Type.PHOTO.code || type == MediaMessage.Type.VIDEO.code) {
                conversationView.navigateToMediaDetailView(message.getId());
            }

        }
    }

    @VisibleForTesting
    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
