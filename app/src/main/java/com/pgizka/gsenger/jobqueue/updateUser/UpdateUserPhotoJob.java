package com.pgizka.gsenger.jobqueue.updateUser;


import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.config.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateUserPhotoJob extends BaseJob {

    @Inject
    transient UserAccountManager userAccountManager;

    @Inject
    transient UserRestService userRestService;

    public UpdateUserPhotoJob() {
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

        RequestBody requestBody = getRequestBody();
        Call<ResponseBody> call = userRestService.updatePhoto(owner.getServerId(), requestBody);
        Response<ResponseBody> response = call.execute();

        if (response.isSuccess()) {
            //do nothing
        } else {

            throw new Exception();
        }

    }

    private RequestBody getRequestBody() {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("image");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                FileInputStream fileInputStream = new FileInputStream(userAccountManager.getOwnerImage());

                int cnt = 0;
                byte[] buffer = new byte[8192];
                while((cnt = fileInputStream.read(buffer)) > 0) {
                    sink.write(buffer, 0, cnt);
                }
                sink.flush();
                sink.close();
                fileInputStream.close();
            }
        };
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
