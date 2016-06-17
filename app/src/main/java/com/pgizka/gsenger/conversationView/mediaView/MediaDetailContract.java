package com.pgizka.gsenger.conversationView.mediaView;


import com.pgizka.gsenger.provider.Message;

import java.util.List;

public interface MediaDetailContract {

    interface View {

        void displayMessages(List<Message> messages);

        void setInitialPosition(int position);

    }

    interface Presenter {

        void onCreate(View view, int messageId, int savedPosition);

        void onDestroy();

        void onResume();

    }

}
