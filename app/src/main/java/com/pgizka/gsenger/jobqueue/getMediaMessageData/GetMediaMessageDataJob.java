package com.pgizka.gsenger.jobqueue.getMediaMessageData;


import android.util.Log;

import com.path.android.jobqueue.Params;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.config.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.util.StorageResolver;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class GetMediaMessageDataJob extends BaseJob {
    static final String TAG = GetMediaMessageDataJob.class.getSimpleName();

    private int messageId;

    private transient Realm realm;
    private transient Message message;

    @Inject
    transient MessageRestService messageRestService;

    @Inject
    transient EventBus eventBus;

    public GetMediaMessageDataJob(int messageId) {
        super(new Params(5).requireNetwork().persist().groupBy("messageData"));
        this.messageId = messageId;
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        super.inject(applicationComponent);
        applicationComponent.inject(this);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        message = realm.where(Message.class).equalTo("id", messageId).findFirst();

        if (message == null) {
            //message is not available, maybe it was deleted, skip it
            Log.i(TAG, "message with id " + messageId + " does not exist, skipping it");
            realm.commitTransaction();
            return;
        }

        message.setState(Message.State.DOWNLOADING.code);
        realm.commitTransaction();

        Call<ResponseBody> call = messageRestService.getMediaMessageData(message.getServerId());
        Response<ResponseBody> response = call.execute();

        if (response.isSuccess()) {
            Log.i(TAG, "Downloading message: " + messageId);
            MediaMessage mediaMessage = message.getMediaMessage();
            InputStream inputStream = response.body().byteStream();

            File file = new File(
                    StorageResolver.getPathForIncomingMediaData(mediaMessage),
                    mediaMessage.getFileName());
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int cnt = 0;
            while ((cnt = inputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, cnt);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

            realm.beginTransaction();
            mediaMessage.setPath(file.getPath());
            message.setState(Message.State.RECEIVED.code);
            realm.commitTransaction();
        } else {
            throw new Exception();
        }
    }

    @Override
    protected void onCancel() {

    }
}
