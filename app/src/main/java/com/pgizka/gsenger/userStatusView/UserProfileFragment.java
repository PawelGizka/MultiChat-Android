package com.pgizka.gsenger.userStatusView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.util.ImagePickerUtil;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

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

    private Uri updatedUserPhoto;

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
            presenter.onSaveChanges(updatedUserPhoto,
                    userNameEditText.getText().toString(), statusEditText.getText().toString());
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
            if (resultCode == AppCompatActivity.RESULT_OK) {
                updatedUserPhoto = data.getData();
                Picasso.with(getContext()).load(updatedUserPhoto).into(mainImage);
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
            Picasso.with(getActivity()).load(userPhoto).into(mainImage);
        }
    }
}
