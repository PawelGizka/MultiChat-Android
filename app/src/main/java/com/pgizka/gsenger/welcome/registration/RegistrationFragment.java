package com.pgizka.gsenger.welcome.registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.api.dtos.user.UserRegistrationRequest;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMUTil;
import com.pgizka.gsenger.mainView.MainActivity;
import com.pgizka.gsenger.util.UserAccountManager;
import com.pgizka.gsenger.welcome.GcmTokenObtainedEvent;
import com.pgizka.gsenger.welcome.WelcomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RegistrationFragment extends Fragment implements WelcomeActivity.WelcomeActivityContent {
    static final String TAG = RegistrationFragment.class.getSimpleName();

    @Bind(R.id.registration_user_name) EditText userNameEditText;
    @Bind(R.id.registration_phone_number) EditText phoneNumberEditText;
    @Bind(R.id.registration_sign_in_button) Button loginButton;
    @Bind(R.id.facbook_login_button) LoginButton facebookLoginButton;

    @Inject
    UserAccountManager userAccountManager;

    private ProgressDialog progressDialog;

    private CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        setupFacebookLogin();
        return view;
    }

    private void setupFacebookLogin() {
        facebookLoginButton.setFragment(this);
        List<String> permissions = new ArrayList<>();
        permissions.add("user_friends");
        permissions.add("email");
        permissions.add("public_profile");
        facebookLoginButton.setReadPermissions(permissions);
        callbackManager = CallbackManager.Factory.create();

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "On facebook success");
                onLoginButtonClicked();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "On facebook cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "On facebook error", error);
            }
        });
    }

    @OnTextChanged(value = {R.id.registration_user_name, R.id.registration_phone_number},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void validateInput() {
        String username = userNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();

        boolean enabled = username.length() >= 3 && phoneNumber.length() == 9;

        facebookLoginButton.setEnabled(enabled);
        loginButton.setEnabled(enabled);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean shouldDisplay(Context context) {
        if (userAccountManager == null) {
            userAccountManager = GSengerApplication.getApplicationComponent().userAccountManager();
        }
        return !userAccountManager.isUserRegistered();
    }

    @OnClick(R.id.registration_sign_in_button)
    public void onLoginButtonClicked() {
        String token = GCMUTil.getRegistrationToken(getContext());
        boolean tokenObtained = token != null;
        if (!tokenObtained) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(R.string.waitingForGoogleCloudMessagingToken);
            progressDialog.show();
            return;
        }

        String userName = userNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        long parsedPhoneNumber = 0;

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, "PL").getNationalNumber();
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUserName(userName);
        request.setPhoneNumber((int) parsedPhoneNumber);
        request.setGcmToken(token);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            String facebookToken = accessToken.getToken();
            if (facebookToken != null) {
                request.setFacebookToken(facebookToken);
            }
        }

        new RegistrationTask(request).execute();

        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(R.string.registering);
        progressDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GcmTokenObtainedEvent tokenObtainedEvent) {
        progressDialog.dismiss();
        onLoginButtonClicked();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RegistrationEvent registrationEvent) {
        progressDialog.dismiss();
        loginButton.setEnabled(true);
        if(registrationEvent.isSuccess()) {
            showResultDialog(R.string.success, R.string.registration_successfully_registered, (dialog, which) -> {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
        } else {
            showResultDialog(R.string.failure, registrationEvent.getMessage(), null);
        }
    }

    private void showResultDialog(@StringRes int title, @StringRes int message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, onClickListener)
                .create().show();
    }


}
