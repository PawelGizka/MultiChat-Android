package com.pgizka.gsenger.jobqueue.getMessages;


import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.dtos.messages.MediaMessageData;
import com.pgizka.gsenger.api.dtos.messages.MessageBatchResponse;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;
import com.pgizka.gsenger.config.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.jobqueue.setMessageState.UpdateMessageStateJob;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.MessageRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;


public class GetMessagesJob extends BaseJob {
    private static final String TAG = GetMessagesJob.class.getSimpleName();

    private int chatId;
    private transient Realm realm;

    @Inject
    transient MessageRestService messageRestService;

    @Inject
    transient MessageRepository messageRepository;

    @Inject
    transient JobManager jobManager;

    public GetMessagesJob(int chatId) {
        super(new Params(5).requireNetwork().persist());
        this.chatId = chatId;
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        super.inject(applicationComponent);
        applicationComponent.inject(this);
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRun() throws Throwable {
        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Chat chat = realm.where(Chat.class).equalTo("id", chatId).findFirst();
        realm.commitTransaction();

        if (chat == null) {
            //chat is not available, maybe it was deleted, skip it
            Log.i(TAG, "Chat with id " + chat + " does not exist, skipping it");
            return;
        }

        Call<MessageBatchResponse> call = messageRestService.getMessagesForChat(String.valueOf(chat.getServerId()));
        Response<MessageBatchResponse> response = call.execute();
        if (response.isSuccess()) {
            realm.beginTransaction();

            MessageBatchResponse messageBatchResponse = response.body();
            int size = messageBatchResponse.getTextMessages().size() + messageBatchResponse.getMediaMessages().size();
            List<Integer> deliveredMessagesIds = new ArrayList<>(size);

            for (TextMessageData textMessageData : messageBatchResponse.getTextMessages()) {
                Message insertedMessage = messageRepository.handleIncomingTextMessage(textMessageData);
                deliveredMessagesIds.add(insertedMessage.getId());
            }

            for (MediaMessageData mediaMessageData : messageBatchResponse.getMediaMessages()) {
                Message insertedMessage = messageRepository.handleIncomingMediaMessage(mediaMessageData);
                deliveredMessagesIds.add(insertedMessage.getId());
            }

            jobManager.addJob(new UpdateMessageStateJob(deliveredMessagesIds));

            realm.commitTransaction();
        } else {
            throw new Exception();
        }
    }

    @Override
    protected void onCancel() {

    }
}
