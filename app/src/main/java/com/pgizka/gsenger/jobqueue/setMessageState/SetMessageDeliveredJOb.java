package com.pgizka.gsenger.jobqueue.setMessageState;


import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.pgizka.gsenger.api.BaseResponse;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.Receiver;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

public class SetMessageDeliveredJOb extends BaseJob {

    private transient Realm realm;

    private int messageId;

    @Inject
    transient UserAccountManager userAccountManager;

    @Inject
    transient MessageRestService messageRestService;

    public SetMessageDeliveredJOb(int messageId) {
        super(new Params(5).requireNetwork().persist().groupBy("message_state"));
        this.messageId = messageId;
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        realm = Realm.getDefaultInstance();
        realm.refresh();

        User owner = userAccountManager.getOwner();
        Message message = realm.where(Message.class)
                .equalTo("id", messageId)
                .findFirst();

        Receiver receiver = realm.where(Receiver.class)
                .equalTo("user.id", owner.getId())
                .equalTo("message.id", message.getId())
                .findFirst();

        MessageStateChangedRequest messageStateChangedRequest = new MessageStateChangedRequest();
        messageStateChangedRequest.setMessageId(message.getServerId());
        messageStateChangedRequest.setReceiverId(owner.getServerId());
        messageStateChangedRequest.setDate(receiver.getDelivered());

        Call<BaseResponse> call = messageRestService.setMessageDelivered(messageStateChangedRequest);
        Response<BaseResponse> response = call.execute();

        if (response.isSuccess()) {
            //do nothing
            response.body();
        } else {
            throw new Exception();
        }

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        if (runCount <= 3) {
            return RetryConstraint.RETRY;
        } else {
            return RetryConstraint.createExponentialBackoff(runCount, 2000);
        }
    }

    @Override
    protected void onCancel() {

    }
}

