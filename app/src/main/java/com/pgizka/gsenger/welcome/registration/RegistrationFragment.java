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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMUTil;
import com.pgizka.gsenger.mainView.MainActivity;
import com.pgizka.gsenger.util.UserAccountManager;
import com.pgizka.gsenger.welcome.WelcomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RegistrationFragment extends Fragment implements WelcomeActivity.WelcomeActivityContent {
    static final String TAG = RegistrationFragment.class.getSimpleName();

    private EditText userNameEditText;
    private EditText phoneNumberEditText;
    private Button loginButton;
    private LoginButton facebookLoginButton;

    private ProgressDialog progressDialog;

    private UserAccountManager userAccountManager;

    private CallbackManager callbackManager;

    @Inject
    UserRestService userRestService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        GSengerApplication.getApplicationComponent().inject(this);
        userAccountManager = new UserAccountManager(getActivity());
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

        userNameEditText = (EditText) view.findViewById(R.id.registration_user_name);
        phoneNumberEditText = (EditText) view.findViewById(R.id.registration_phone_number);
        loginButton = (Button) view.findViewById(R.id.registration_sign_in_button);
        facebookLoginButton = (LoginButton) view.findViewById(R.id.facbook_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked(null);
            }
        });

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
                AccessToken accessToken = loginResult.getAccessToken();
                onLoginButtonClicked(accessToken.getToken());
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

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateInput();
            }
        };

        userNameEditText.addTextChangedListener(textWatcher);
        phoneNumberEditText.addTextChangedListener(textWatcher);

        return view;
    }

    private void validateInput() {
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

    private void onLoginButtonClicked(String facebookToken) {
        String userName = userNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String token = GCMUTil.getRegistrationToken(getContext());
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
        if (facebookToken != null) {
            request.setFacebookToken(facebookToken);
        }

        new RegistrationTask(request).execute();

        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Registering");
        progressDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RegistrationEvent registrationEvent) {
        progressDialog.dismiss();
        loginButton.setEnabled(true);
        if(registrationEvent.isSuccess()) {
            showResultDialog(R.string.success, R.string.registration_successfully_registered, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
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
