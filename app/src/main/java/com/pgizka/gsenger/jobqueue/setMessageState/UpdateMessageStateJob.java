package com.pgizka.gsenger.jobqueue.setMessageState;

import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.dtos.messages.MessagesStateChangedRequest;
import com.pgizka.gsenger.config.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.ReceiverInfo;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateMessageStateJob extends BaseJob {
    static final String TAG = UpdateMessageStateJob.class.getSimpleName();

    private List<Integer> messagesIds;

    private transient Realm realm;

    @Inject
    transient UserAccountManager userAccountManager;

    @Inject
    transient MessageRestService messageRestService;

    public UpdateMessageStateJob(int messageId) {
        super(new Params(5).requireNetwork().persist().groupBy("message_state"));
        this.messagesIds = new ArrayList<>(1);
        this.messagesIds.add(messageId);
    }

    public UpdateMessageStateJob(List<Integer> messagesIds) {
        super(new Params(5).requireNetwork().persist().groupBy("message_state"));
        this.messagesIds = messagesIds;
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

        if (messagesIds.isEmpty()) {
            return;
        }

        realm.beginTransaction();
        User owner = userAccountManager.getOwner();

        Message message = realm.where(Message.class)
                .equalTo("id", messagesIds.get(0))
                .findFirst();

        ReceiverInfo receiverInfo = realm.where(ReceiverInfo.class)
                .equalTo("user.id", owner.getId())
                .equalTo("message.id", message.getId())
                .findFirst();

        List<Integer> messagesServerIds = new ArrayList<>(messagesIds.size());
        for (int messageId : messagesIds) {
            messagesServerIds.add(realm.where(Message.class).equalTo("id", messageId).findFirst().getServerId());
        }
        realm.commitTransaction();

        MessagesStateChangedRequest messagesStateChangedRequest = new MessagesStateChangedRequest();
        messagesStateChangedRequest.setMessagesIds(messagesServerIds);
        messagesStateChangedRequest.setReceiverId(owner.getServerId());
        messagesStateChangedRequest.setDeliveredDate(receiverInfo.getDelivered());
        messagesStateChangedRequest.setViewedDate(receiverInfo.getViewed());

        Call<ResponseBody> call = messageRestService.updateMessageState(messagesStateChangedRequest);

        Response<ResponseBody> response = call.execute();

        if (response.isSuccess()) {
            //do nothing
        } else {
            throw new Exception();
        }

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        throwable.printStackTrace();
        if (realm.isInTransaction()) {
            realm.cancelTransaction();
        }
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

