package com.pgizka.gsenger.conversationView.sendMediaView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.dagger2.GSengerApplication;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMediaFragment extends Fragment implements SendMediaContract.View {

    public static final String ACTION_ARGUMENT = "action";
    public static final String CHOSE_PHOTO_ACTION = "chosePhotoAction";
    public static final String CHAT_ID_ARGUMENT = "chatIdArgument";
    public static final String USER_ID_ARGUMENT = "friendIdArgument";

    private static final int PICK_PHOTO_REQUEST = 0;

    @Bind(R.id.send_media_main_image) ImageView mainImageView;
    @Bind(R.id.send_media_description_edit_text) EditText descriptionEditText;
    private Toolbar toolbar;

    @Inject
    SendMediaContract.Presenter presenter;

    private ProgressDialog progressDialog;

    private Uri dataToSend;
    private String action;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);

        Bundle arguments = getArguments();
        int userId = arguments.getInt(USER_ID_ARGUMENT);
        int chatId = arguments.getInt(CHAT_ID_ARGUMENT);
        presenter.onCreate(this, getActivity(), userId, chatId);

        action = arguments.getString(ACTION_ARGUMENT);

        if (savedInstanceState == null) {
            processAction();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_media, container, false);
        ButterKnife.bind(this, view);
        toolbar = ((SendMediaActivity) getActivity()).getToolbar();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBarTitle();
        presenter.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("dataToSend", dataToSend);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        dataToSend = savedInstanceState.getParcelable("dataToSend");
        if (dataToSend != null) {
            Glide.with(this)
                    .load(dataToSend)
                    .into(mainImageView);
        }
    }

    private void setActionBarTitle() {
        if (action.equals(CHOSE_PHOTO_ACTION)) {
            toolbar.setTitle("Send Photo");
        }
    }

    @Override
    public void setToolbarSubtitle(String subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    private void processAction() {
        if (action.equals(CHOSE_PHOTO_ACTION)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_PHOTO_REQUEST);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_REQUEST) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                dataToSend = data.getData();
                Glide.with(this)
                        .load(dataToSend)
                        .into(mainImageView);
            } else {
                finish();
            }
        }

    }

    @Override
    public void showProgressDialog(String title) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(title);
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @OnClick(R.id.send_media_send_button)
    public void onSendButtonClicked() {
        if (action.equals(CHOSE_PHOTO_ACTION)) {
            presenter.sendPhoto(dataToSend, descriptionEditText.getText().toString());
        }
    }

    @OnClick(R.id.send_media_cancel_button)
    public void onCancelButtonClicked() {
        finish();
    }


}
