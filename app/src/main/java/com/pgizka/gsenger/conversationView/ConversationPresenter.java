package com.pgizka.gsenger.conversationView;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.jobqueue.sendMessge.SendMessageJob;
import com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.Receiver;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.util.UserAccountManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob.Type.SET_VIEWED;

public class ConversationPresenter implements ConversationContract.Presenter {
    static final String TAG = ConversationPresenter.class.getSimpleName();

    private ConversationContract.View conversationView;
    private Realm realm;

    private int friendId;
    private int chatId;

    private User friend;
    private User owner;
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
    public void onCreate(ConversationContract.View view, int friendId, int chatId) {
        GSengerApplication.getApplicationComponent().inject(this);
        conversationView = view;
        realm = Realm.getDefaultInstance();
        groupChat = friendId == -1;
        this.friendId = friendId;
        this.chatId = chatId;
    }

    @Override
    public void onResume() {
        paused = false;
        owner = userAccountManager.getOwner();

        if (groupChat) {
            chat = realm.where(Chat.class)
                    .equalTo("id", chatId)
                    .findFirst();
        } else {
            friend = realm.where(User.class)
                    .equalTo("id", friendId)
                    .findFirst();

            chat = chatRepository.getSingleConversationChatWith(friend);
        }

        if (chat != null) {
            getMessages();
            conversationView.displayConversationItems(messages);
        } else {
            realm.where(Chat.class).findAll().addChangeListener(element -> {
                chat = chatRepository.getSingleConversationChatWith(friend);
                if (chat != null) {
                    getMessages();
                }
            });
        }

        if (chat != null && messages != null) {
            setAllMessagesViewed();
        }

        if (groupChat) {
            conversationView.displayChatName(chat.getChatName());
        } else {
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

    private void getMessages() {
        messages = realm.where(Message.class)
                .equalTo("chat.id", chat.getId())
                .findAll();

        messages.addChangeListener(element -> {
            Log.i(TAG, "on messages change called");
            messages = element;
            conversationView.displayConversationItems(messages);
            setAllMessagesViewed();
        });
    }

    @VisibleForTesting
    public void setAllMessagesViewed() {
        if (paused) {
            return;
        }
        List<SetMessageStateJob> setMessageStateJobs = new ArrayList<>();

        realm.beginTransaction();
        while (true) {
            Receiver receiver = realm.where(Receiver.class)
                    .equalTo("message.chat.id", chat.getId())
                    .equalTo("user.id", owner.getId())
                    .equalTo("viewed", 0)
                    .findFirst();

            if (receiver == null) {
                break;
            }

            receiver.setViewed(System.currentTimeMillis());
            setMessageStateJobs.add(new SetMessageStateJob(receiver.getMessage().getId(), SET_VIEWED));
        }
        realm.commitTransaction();

        for (SetMessageStateJob job : setMessageStateJobs) {
            jobManager.addJobInBackground(job);
        }
    }

    @Override
    public void sendMessage(String text) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (!groupChat) {
            chat = chatRepository.getOrCreateSingleConversationChatWith(friend);
        }

        Message message = messageRepository.createOutgoingMessageWithReceivers(chat);

        TextMessage textMessage = new TextMessage();
        textMessage.setText(text);
        textMessage = realm.copyToRealm(textMessage);

        message.setType(Message.Type.TEXT_MESSAGE.code);
        message.setTextMessage(textMessage);

        realm.commitTransaction();

        jobManager.addJob(new SendMessageJob(message.getId()));
    }

    @VisibleForTesting
    public void setFriend(User friend) {
        this.friend = friend;
    }

    @VisibleForTesting
    public void setOwner(User owner) {
        this.owner = owner;
    }

    @VisibleForTesting
    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
