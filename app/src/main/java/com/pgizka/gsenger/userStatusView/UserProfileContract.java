package com.pgizka.gsenger.userStatusView;


import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public interface UserProfileContract {

    interface View {
        void setUserName(String userName);

        void setStatus(String status);

        void setUserPhotoPath(File userPhoto);
    }

    interface Presenter {
        void onCreate(View view);

        void onResume();

        void onSaveChanges(Uri userPhoto, String userName, String status);
    }

}
