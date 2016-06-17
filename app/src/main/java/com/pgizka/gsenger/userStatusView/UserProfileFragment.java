package com.pgizka.gsenger.userStatusView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.util.ImageUtil;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileFragment extends Fragment implements UserProfileContract.View {

    static final int PICK_PHOTO_REQUEST = 0;
    static final int CROP_PHOTO_REQUEST = 1;

    @BindView(R.id.user_profile_main_image) ImageView mainImage;
    @BindView(R.id.user_profile_username_edit_text) EditText userNameEditText;
    @BindView(R.id.user_profile_status_edit_text) EditText statusEditText;
    @BindView(R.id.user_profile_change_photo_button) FloatingActionButton changePhotoButton;

    @Inject
    UserProfileContract.Presenter presenter;

    @Inject
    ImageUtil imageUtil;

    private Uri updatedUserPhoto;
    private File tempPhotoFile;

    public UserProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);
        setHasOptionsMenu(true);
        presenter.onCreate(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @OnClick(R.id.user_profile_change_photo_button)
    public void onChangePhotoButtonClicked() {
        tempPhotoFile = imageUtil.createImageFile();
        Intent intent = imageUtil.getPickOrTakeImageIntent(getActivity(), tempPhotoFile);
        startActivityForResult(intent, PICK_PHOTO_REQUEST);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_user_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.user_profile_save_changes_action) {
            presenter.onSaveChanges(updatedUserPhoto,
                    userNameEditText.getText().toString(), statusEditText.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (data == null || data.getData() == null) {
            updatedUserPhoto = Uri.fromFile(tempPhotoFile);
        } else {
            updatedUserPhoto = data.getData();
        }

        if (requestCode == PICK_PHOTO_REQUEST) {
            Intent cropIntent = imageUtil.getCropImageIntent(updatedUserPhoto);
            startActivityForResult(cropIntent, CROP_PHOTO_REQUEST);
        } else if (requestCode == CROP_PHOTO_REQUEST) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Glide.with(this)
                        .load(updatedUserPhoto)
                        .into(mainImage);
            }
        }
    }

    @Override
    public void setUserName(String userName) {
        userNameEditText.setText(userName);
    }

    @Override
    public void setStatus(String status) {
        statusEditText.setText(status);
    }

    @Override
    public void setUserPhotoPath(File userPhoto) {
        if (updatedUserPhoto == null) {
            Glide.with(this)
                    .load(userPhoto)
                    //we want to always load the most recent image
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(mainImage);
        }
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
