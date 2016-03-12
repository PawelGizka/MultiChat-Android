package com.pgizka.gsenger.welcome.registration;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.io.ResultCode;
import com.pgizka.gsenger.io.UserRegistrationI;
import com.pgizka.gsenger.util.UserAccountManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Response;

public class RegistrationTask extends AsyncTask<Void, Void, Void> {
    static final String TAG = RegistrationTask.class.getSimpleName();

    //@Inject
    private UserRegistrationI userRegistrationI;

    private Context context;

    private String email;
    private String userName;
    private String password;
    private int phoneNumber;
    private String gcmToken;

    public RegistrationTask(Context context, String email, String userName, String password, int phoneNumber, String gcmToken) {
        this.context = context;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gcmToken = gcmToken;
    }

    @Override
    protected Void doInBackground(Void... params) {
        UserRegistrationRequestDTO requestDTO = createRequest();

//        Retrofit retrofit = RetrofitFactory.getInstance();
//        UserRegistrationI userRegistrationI = retrofit.create(UserRegistrationI.class);

        UserRegistrationResponseDTO responseDTO = null;
        try {
            Response<UserRegistrationResponseDTO> response = userRegistrationI.register(requestDTO).execute();
            if (response.isSuccess()) {
                responseDTO = response.body();
            } else {
                Log.i(TAG, "Connection failed, http code " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        processResponse(responseDTO);
        return null;
    }

    private UserRegistrationRequestDTO createRequest() {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword(password);
        requestDTO.setUserName(userName);
        requestDTO.setPhoneNumber(phoneNumber);
        requestDTO.setGcmToken(gcmToken);

        return requestDTO;
    }

    private void processResponse(UserRegistrationResponseDTO responseDTO) {

        RegistrationEvent registrationEvent = new RegistrationEvent();
        if (responseDTO == null) {
            Log.i(TAG, "connection during registration failed");
            registrationEvent.setSuccess(false);
            registrationEvent.setMessage(R.string.connection_failed);
        } else {
            int resultCode = responseDTO.getResultCode();
            if (resultCode == ResultCode.OK.code) {
                registrationEvent.setSuccess(true);
                UserAccountManager.setUserRegistered(context, responseDTO.getUserId());
            } else if (resultCode == ResultCode.USER_ALREADY_EXIST.code) {
                registrationEvent.setSuccess(false);
                registrationEvent.setMessage(R.string.registration_error_user_already_exist);
            } else if(resultCode == ResultCode.UNEXPECTED_ERROR.code) {
                registrationEvent.setSuccess(false);
                registrationEvent.setMessage(R.string.connection_unexpected_error);
            }
        }

        EventBus.getDefault().post(registrationEvent);
    }


}
