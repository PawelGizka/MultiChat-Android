package com.pgizka.gsenger.welcome.registration;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.api.ResultCode;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.util.UserAccountManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class RegistrationTask extends AsyncTask<Void, Void, Void> {
    static final String TAG = RegistrationTask.class.getSimpleName();

    @Inject
    UserRestService userRestService;

    @Inject
    EventBus eventBus;

    @Inject
    UserAccountManager userAccountManager;

    private UserRegistrationRequest request;

    public RegistrationTask(UserRegistrationRequest request) {
        GSengerApplication.getApplicationComponent().inject(this);
        this.request = request;
    }

    @Override
    protected Void doInBackground(Void... params) {
        UserRegistrationResponse response = null;
        try {
            Call<UserRegistrationResponse> call = userRestService.register(request);
            Response<UserRegistrationResponse> httpResponse = call.execute();
            if (httpResponse.isSuccess()) {
                response = httpResponse.body();
            } else {
                Log.i(TAG, "Connection failed, http code " + httpResponse.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        processResponse(response, request);
        return null;
    }

    private void processResponse(UserRegistrationResponse responseDTO, UserRegistrationRequest registrationRequest) {

        RegistrationEvent registrationEvent = new RegistrationEvent();
        if (responseDTO == null) {
            Log.i(TAG, "connection during registration failed");
            registrationEvent.setSuccess(false);
            registrationEvent.setMessage(R.string.connection_failed);
        } else {
            int resultCode = responseDTO.getResultCode();
            if (resultCode == ResultCode.OK.code) {
                registrationEvent.setSuccess(true);
                userAccountManager.setUserRegistered(registrationRequest, responseDTO);
            } else if (resultCode == ResultCode.USER_ALREADY_EXIST.code) {
                registrationEvent.setSuccess(false);
                registrationEvent.setMessage(R.string.registration_error_user_already_exist);
            } else if(resultCode == ResultCode.UNEXPECTED_ERROR.code) {
                registrationEvent.setSuccess(false);
                registrationEvent.setMessage(R.string.connection_unexpected_error);
            }
        }

        eventBus.post(registrationEvent);
    }


}
