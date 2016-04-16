package com.pgizka.gsenger.conversationView.sendMediaView;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.jobqueue.sendMessge.SendMessageJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.CopyFileTask;
import com.pgizka.gsenger.util.StorageResolver;

import javax.inject.Inject;

import io.realm.Realm;

public class SendMediaPresenter implements SendMediaContract.Presenter {

    @Inject
    MessageRepository messageRepository;

    @Inject
    ChatRepository chatRepository;

    @Inject
    JobManager jobManager;

    private int chatId;
    private int friendId;

    private SendMediaContract.View view;

    private Context context;

    @Override
    public void onCreate(SendMediaContract.View view, Context context, int friendId, int chatId) {
        GSengerApplication.getApplicationComponent().inject(this);
        this.view = view;
        this.friendId = friendId;
        this.chatId = chatId;
        this.context = context;
    }

    @Override
    public void onResume() {
        Realm realm = Realm.getDefaultInstance();
        User friend = realm.where(User.class)
                .equalTo("id", friendId)
                .findFirst();
        view.setToolbarSubtitle(friend.getUserName());
    }

    @Override
    public void sendPhoto(Uri photoUri, String description) {
        view.showProgressDialog("Copying File...");
        new CopyFileTask(context)
                .from(photoUri)
                .to(StorageResolver.IMAGES_SENT_PATH)
                .onCopyingFinished((newFileName, path) -> {
                    view.dismissProgressDialog();
                    sendMediaMessage(MediaMessage.Type.PHOTO.code, newFileName, path, description);
                    view.finish();
                })
                .execute();
    }

    public void sendMediaMessage(int type, String fileName, String path, String description) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        MediaMessage mediaMessage = new MediaMessage();
        mediaMessage.setMediaType(type);
        mediaMessage.setFileName(fileName);
        mediaMessage.setPath(path);
        mediaMessage.setDescription(description);

        mediaMessage = realm.copyToRealm(mediaMessage);

        User friend = realm.where(User.class)
                .equalTo("id", friendId)
                .findFirst();
        Chat chat = chatRepository.getOrCreateSingleConversationChatWith(friend);

        Message message = messageRepository.createOutgoingMessageWithReceiver(chat, friend);
        message.setType(Message.Type.MEDIA_MESSAGE.code);
        message.setMediaMessage(mediaMessage);
        chat.getMessages().add(message);

        realm.commitTransaction();

//        jobManager.addJob(new SendMessageJob(message.getId()));
    }

}
