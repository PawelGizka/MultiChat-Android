package com.pgizka.gsenger.userStatusView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.util.ImagePickerUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileFragment extends Fragment implements UserProfileContract.View {

    static final int PICK_PHOTO_REQUEST = 0;
    static final int CROP_PHOTO_REQUEST = 1;

    @Bind(R.id.user_profile_main_image) ImageView mainImage;
    @Bind(R.id.user_profile_username_edit_text) EditText userNameEditText;
    @Bind(R.id.user_profile_status_edit_text) EditText statusEditText;
    @Bind(R.id.user_profile_change_photo_button) FloatingActionButton changePhotoButton;

    @Inject
    UserProfileContract.Presenter presenter;

    @Inject
    ImagePickerUtil imagePickerUtil;

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
        Intent intent = imagePickerUtil.getPickImageIntent(getActivity());
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

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_REQUEST) {
            Intent cropIntent = imagePickerUtil.getCropImageIntent(data);
            startActivityForResult(cropIntent, CROP_PHOTO_REQUEST);
        } else if (requestCode == CROP_PHOTO_REQUEST) {
            Bitmap userPhoto = imagePickerUtil.getImageFromResult(getActivity(), resultCode, data);
            if (userPhoto != null) {
                mainImage.setImageBitmap(userPhoto);
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
    public void setUserPhoto(Bitmap userPhoto) {
        mainImage.setImageBitmap(userPhoto);
    }
}
