package com.pgizka.gsenger.conversationView.sendMediaView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.util.ImageUtil;
import com.pgizka.gsenger.util.StorageResolver;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMediaFragment extends Fragment implements SendMediaContract.View {

    public static final String ACTION_ARGUMENT = "action";
    public static final String CHOSE_PHOTO_ACTION = "chosePhotoAction";
    public static final String TAKE_PHOTO_ACTION = "takePhotoAction";
    public static final String CHOSE_VIDEO_ACTION = "choseVideoAction";
    public static final String TAKE_VIDEO_ACTION = "takeVideoAction";
    public static final String CHOSE_FILE_ACTION = "choseFileAction";

    public static final String CHAT_ID_ARGUMENT = "chatIdArgument";
    public static final String USER_ID_ARGUMENT = "friendIdArgument";

    private static final int PICK_PHOTO_REQUEST = 0;
    private static final int TAKE_PHOTO_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private static final int TAKE_VIDEO_REQUEST = 3;
    private static final int PICK_FILE_REQUEST = 4;

    @BindView(R.id.send_media_main_image) ImageView mainImageView;
    @BindView(R.id.send_media_description_edit_text) EditText descriptionEditText;
    @BindView(R.id.send_media_file_path_text) TextView filePathText;
    @BindView(R.id.send_media_file_size_text) TextView fileSizeText;
    private Toolbar toolbar;

    @Inject
    SendMediaContract.Presenter presenter;

    private ProgressDialog progressDialog;

    private Uri dataToSend;
    private String action;
    private File mediaTempFile;

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

    private void processAction() {
        if (action.equals(CHOSE_PHOTO_ACTION)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_PHOTO_REQUEST);
        } else if (action.equals(TAKE_PHOTO_ACTION)) {
            mediaTempFile = ImageUtil.createImageFile();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaTempFile));
            intent.putExtra("return-data", true);
            startActivityForResult(intent, TAKE_PHOTO_REQUEST);
        } else if (action.equals(CHOSE_VIDEO_ACTION)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, PICK_VIDEO_REQUEST);
        } else if (action.equals(TAKE_VIDEO_ACTION)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(intent, TAKE_VIDEO_REQUEST);
        } else if (action.equals(CHOSE_FILE_ACTION)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_FILE_REQUEST);
        }

    }

    private void setActionBarTitle() {
        if (action.equals(CHOSE_PHOTO_ACTION) || action.equals(TAKE_PHOTO_ACTION)) {
            toolbar.setTitle("Send Photo");
        } else if (action.equals(CHOSE_VIDEO_ACTION) || action.equals(TAKE_VIDEO_ACTION)) {
            toolbar.setTitle("Send Video");
        } else if (action.equals(CHOSE_FILE_ACTION)) {
            toolbar.setTitle("Send File");
        }
    }

    @Override
    public void setToolbarSubtitle(String subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (data == null) {
                dataToSend = Uri.fromFile(mediaTempFile);
            } else {
                dataToSend = data.getData();
            }
        } else {
            finish();
            return;
        }

        if (requestCode == PICK_PHOTO_REQUEST || requestCode == TAKE_PHOTO_REQUEST) {
            Glide.with(this)
                    .load(dataToSend)
                    .into(mainImageView);
        } else if (requestCode == PICK_VIDEO_REQUEST || requestCode == TAKE_VIDEO_REQUEST) {
            String path = StorageResolver.getRealPathFromUri(dataToSend, getActivity());
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            mainImageView.setImageBitmap(bitmap);
        } else if (requestCode == PICK_FILE_REQUEST) {
            filePathText.setVisibility(View.VISIBLE);
            fileSizeText.setVisibility(View.VISIBLE);
            File file = new File(dataToSend.getPath());
            filePathText.setText(file.getPath());
            String sizeText = Formatter.formatFileSize(getActivity(), file.length());
            fileSizeText.setText(sizeText);
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
        if (action.equals(CHOSE_PHOTO_ACTION) || action.equals(TAKE_PHOTO_ACTION)) {
            presenter.sendPhoto(dataToSend, descriptionEditText.getText().toString());
        } else if (action.equals(CHOSE_VIDEO_ACTION) || action.equals(TAKE_VIDEO_ACTION)) {
            presenter.sendVideo(dataToSend, descriptionEditText.getText().toString());
        } else if (action.equals(CHOSE_FILE_ACTION)) {
            presenter.sendFile(dataToSend, descriptionEditText.getText().toString());
        }
    }

    @OnClick(R.id.send_media_cancel_button)
    public void onCancelButtonClicked() {
        finish();
    }


}
