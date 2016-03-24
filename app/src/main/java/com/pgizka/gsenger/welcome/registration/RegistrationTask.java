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

    private String email;
    private String userName;
    private String password;
    private int phoneNumber;
    private String gcmToken;

    public RegistrationTask(String email, String userName, String password, int phoneNumber, String gcmToken) {
        GSengerApplication.getApplicationComponent().inject(this);

        this.email = email;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gcmToken = gcmToken;
    }

    @Override
    protected Void doInBackground(Void... params) {
        UserRegistrationRequest requestDTO = createRequest();

        UserRegistrationResponse responseDTO = null;
        try {
            Call<UserRegistrationResponse> call = userRestService.register(requestDTO);
            Response<UserRegistrationResponse> response = call.execute();
            if (response.isSuccess()) {
                responseDTO = response.body();
            } else {
                Log.i(TAG, "Connection failed, http code " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        processResponse(responseDTO, requestDTO);
        return null;
    }

    private UserRegistrationRequest createRequest() {
        UserRegistrationRequest requestDTO = new UserRegistrationRequest();
        requestDTO.setEmail(email);
        requestDTO.setPassword(password);
        requestDTO.setUserName(userName);
        requestDTO.setPhoneNumber(phoneNumber);
        requestDTO.setGcmToken(gcmToken);

        return requestDTO;
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
