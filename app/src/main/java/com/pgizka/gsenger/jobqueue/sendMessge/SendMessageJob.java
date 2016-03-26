package com.pgizka.gsenger.jobqueue.sendMessge;

import android.util.Log;

import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.Message;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

public class SendMessageJob extends BaseJob {
    static final String TAG = SendMessageJob.class.getSimpleName();

    private int messageId;

    private transient Realm realm;
    private transient Message message;

    @Inject
    transient MessageRestService messageRestService;

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
        realm.refresh();

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

        PutTextMessageRequest messageRequest = new PutTextMessageRequest(message);

        Call<PutMessageResponse> call = messageRestService.sendTextMessage(messageRequest);
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
