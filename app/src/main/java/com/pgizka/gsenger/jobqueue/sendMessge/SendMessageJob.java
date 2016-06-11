package com.pgizka.gsenger.jobqueue.sendMessge;

import android.util.Log;

import com.google.gson.Gson;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.dtos.messages.PutMediaMessageRequest;
import com.pgizka.gsenger.api.dtos.messages.PutMessageResponse;
import com.pgizka.gsenger.api.dtos.messages.PutTextMessageRequest;
import com.pgizka.gsenger.config.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Response;

public class SendMessageJob extends BaseJob {
    static final String TAG = SendMessageJob.class.getSimpleName();

    private int messageId;

    private transient Realm realm;
    private transient Message message;

    @Inject
    transient MessageRestService messageRestService;

    @Inject
    transient EventBus eventBus;

    @Inject
    transient Gson gson;

    public SendMessageJob(int messageId) {
        super(new Params(10).requireNetwork().persist().groupBy("message"));
        this.messageId = messageId;
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRun() throws Throwable {
        realm = Realm.getDefaultInstance();

        message = realm.where(Message.class)
                .equalTo("id", messageId)
                .findFirst();

        if (message == null) {
            //message is not available, maybe it was deleted, skip it
            Log.i(TAG, "message with id " + messageId + " does not exist, skipping it");
            return;
        }

        realm.beginTransaction();
        message.setState(Message.State.SENDING.code);
        realm.commitTransaction();

        Call<PutMessageResponse> call = processMessage();
        Response<PutMessageResponse> response = call.execute();

        if (response.isSuccess()) {
            PutMessageResponse putMessageResponse = response.body();
            realm.beginTransaction();
            message.setServerId(putMessageResponse.getMessageServerId());
            message.setState(Message.State.SENT.code);
            realm.commitTransaction();
        } else {
            throw new Exception();
        }

    }

    private Call<PutMessageResponse> processMessage() {
        int type = message.getType();
        if (type == Message.Type.TEXT_MESSAGE.code) {
            return processTextMessage();
        } else if (type == Message.Type.MEDIA_MESSAGE.code) {
            return processMediaMessage();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private Call<PutMessageResponse> processTextMessage() {
        PutTextMessageRequest messageRequest = new PutTextMessageRequest(message);
        Call<PutMessageResponse> call = messageRestService.sendTextMessage(messageRequest);
        return call;
    }

    private Call<PutMessageResponse> processMediaMessage() {
        MediaMessage mediaMessage = message.getMediaMessage();

        RequestBody dataRequestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("octet-stream");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                File file = new File(mediaMessage.getPath());
                InputStream inputStream = new FileInputStream(file);

                byte[] buffer = new byte[8192];
                int cnt = 0;
                int sent = 0;
                while ((cnt = inputStream.read(buffer)) > 0) {
                    sink.write(buffer, 0, cnt);
                    sent += cnt;
                    int progress = (int) (sent / file.length());
                    eventBus.post(new SendMediaMessageProgressUpdateEvent(message.getId(), progress));
                }
            }
        };

        RequestBody metaDataRequestBody =new RequestBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                PutMediaMessageRequest putMediaMessageRequest = new PutMediaMessageRequest(message);
                String metadata = gson.toJson(putMediaMessageRequest);
                sink.writeUtf8(metadata);
            }
        };

        Call<PutMessageResponse> call = messageRestService.sendMediaMessage(dataRequestBody, metaDataRequestBody);
        return call;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        if (runCount <= 3) {
            return RetryConstraint.RETRY;
        } else {
            realm.beginTransaction();
            message.setState(Message.State.CANNOT_SEND.code);
            realm.commitTransaction();
            return RetryConstraint.CANCEL;
        }
    }

    @Override
    protected void onCancel() {

    }
}
