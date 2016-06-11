package com.pgizka.gsenger.jobqueue.updateUser;


import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.api.dtos.user.UpdateUserStatusRequest;
import com.pgizka.gsenger.config.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateUserStatusJob extends BaseJob {

    @Inject
    transient UserAccountManager userAccountManager;

    @Inject
    transient UserRestService userRestService;

    public UpdateUserStatusJob() {
        super(new Params(5).requireNetwork());
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
        User owner = userAccountManager.getOwner();

        UpdateUserStatusRequest updateUserStatusRequest = prepareRequest(owner);

        Call<ResponseBody> call =  userRestService.updateStatus(owner.getServerId(), updateUserStatusRequest);
        Response<ResponseBody> response = call.execute();

        if (response.isSuccess()) {
            //do nothing
        } else {
            throw new Exception();
        }
    }

    private UpdateUserStatusRequest prepareRequest(User owner) {
        UpdateUserStatusRequest updateUserStatusRequest = new UpdateUserStatusRequest();
        updateUserStatusRequest.setStatus(owner.getStatus());
        updateUserStatusRequest.setUserName(owner.getUserName());
        updateUserStatusRequest.setLastLoggedDate(owner.getLastLoggedDate());
        return updateUserStatusRequest;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        if (runCount <= 3) {
            return RetryConstraint.RETRY;
        } else {
            return RetryConstraint.createExponentialBackoff(runCount, 10_000);
        }
    }

    @Override
    protected void onCancel() {

    }
}
