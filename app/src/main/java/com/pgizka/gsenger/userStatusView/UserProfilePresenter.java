package com.pgizka.gsenger.userStatusView;

import android.net.Uri;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.jobqueue.updateUser.UpdateUserPhotoJob;
import com.pgizka.gsenger.jobqueue.updateUser.UpdateUserStatusJob;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import javax.inject.Inject;

import io.realm.Realm;

public class UserProfilePresenter implements UserProfileContract.Presenter {

    private UserProfileContract.View view;

    @Inject
    UserAccountManager userAccountManager;

    @Inject
    JobManager jobManager;

    private User owner;

    @Override
    public void onCreate(UserProfileContract.View view) {
        GSengerApplication.getApplicationComponent().inject(this);
        this.view = view;
        owner = userAccountManager.getOwner();
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onResume() {
        view.setUserName(owner.getUserName());
        view.setStatus(owner.getStatus());

        String photoPath = owner.getPhotoPath();
        if (photoPath != null) {
            view.setUserPhotoPath(userAccountManager.getOwnerImage());
        }
    }

    @Override
    public void onSaveChanges(Uri userPhoto, String userName, String status) {
        Realm realm = Realm.getDefaultInstance();
        boolean changesSaved = false;
        if (userPhoto != null) {
            String photoPath = userAccountManager.saveOwnerImage(userPhoto);
            realm.beginTransaction();
            owner.setPhotoPath(photoPath);
            realm.commitTransaction();
            jobManager.addJob(new UpdateUserPhotoJob());
            changesSaved = true;
        }

        if (!userName.equals(owner.getUserName()) || !status.equals(owner.getStatus())) {
            realm.beginTransaction();
            owner.setUserName(userName);
            owner.setStatus(status);
            realm.commitTransaction();
            jobManager.addJob(new UpdateUserStatusJob());
            changesSaved = true;
        }

        if (changesSaved) {
            view.displayMessage("Changes were saved.");
        }

    }
}
