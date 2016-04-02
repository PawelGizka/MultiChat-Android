package com.pgizka.gsenger.userStatusView;

import android.graphics.Bitmap;

import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import javax.inject.Inject;

public class UserProfilePresenter implements UserProfileContract.Presenter {

    private UserProfileContract.View view;

    @Inject
    UserAccountManager userAccountManager;

    private User owner;

    @Override
    public void onCreate(UserProfileContract.View view) {
        GSengerApplication.getApplicationComponent().inject(this);
        this.view = view;
        owner = userAccountManager.getOwner();
    }

    @Override
    public void onResume() {
        view.setUserName(owner.getUserName());
        view.setStatus(owner.getStatus());
    }

    @Override
    public void onSaveChanges(Bitmap userPhoto, String userName, String status) {

    }
}
