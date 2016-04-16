package com.pgizka.gsenger.conversationView.sendMediaView;

import android.content.Context;
import android.net.Uri;

public interface SendMediaContract {

    interface View {

        void showProgressDialog(String title);

        void dismissProgressDialog();

        void setToolbarSubtitle(String subtitle);

        void finish();
    }

    interface Presenter {

        void onCreate(View view, Context context, int friendId, int chatId);

        void onResume();

        void sendPhoto(Uri photoUri, String description);
    }

}
