package com.pgizka.gsenger.userStatusView;


import android.graphics.Bitmap;

public interface UserProfileContract {

    interface View {
        void setUserName(String userName);

        void setStatus(String status);

        void setUserPhoto(Bitmap userPhoto);
    }

    interface Presenter {
        void onCreate(View view);

        void onResume();

        void onSaveChanges(Bitmap userPhoto, String userName, String status);
    }

}
