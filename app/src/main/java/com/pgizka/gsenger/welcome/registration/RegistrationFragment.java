package com.pgizka.gsenger.welcome.registration;

import android.app.Dialog;
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
import android.widget.Toast;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.gcm.GCMUTil;
import com.pgizka.gsenger.mainView.MainActivity;
import com.pgizka.gsenger.util.UserAccountManager;
import com.pgizka.gsenger.welcome.WelcomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegistrationFragment extends Fragment implements WelcomeActivity.WelcomeActivityContent {
    static final String TAG = RegistrationFragment.class.getSimpleName();

    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private Button loginButton;

    private ProgressDialog progressDialog;

    private UserAccountManager userAccountManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        emailEditText = (EditText) view.findViewById(R.id.registration_email);
        passwordEditText = (EditText) view.findViewById(R.id.registration_password);
        phoneNumberEditText = (EditText) view.findViewById(R.id.registration_phone_number);
        loginButton = (Button) view.findViewById(R.id.registration_sign_in_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });

        return view;
    }

    @Override
    public boolean shouldDisplay(Context context) {
        if (userAccountManager == null) {
            userAccountManager = GSengerApplication.getApplicationComponent().userAccountManager();
            userAccountManager.setUserRegistered(12);
        }
        return !userAccountManager.isUserRegistered();
    }

    private void onLoginButtonClicked() {
        String userName = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        int phoneNumber = Integer.parseInt(phoneNumberEditText.getText().toString());
        String token = GCMUTil.getRegistrationToken(getContext());

        new RegistrationTask(email, userName, password, phoneNumber, token)
                .execute();

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
