package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.User;

import java.util.List;

public interface ConversationContract {

    interface View {

        void displayConversationItems(List<Message> conversationItems);

        void displayChatImage(User user);

        void displayChatName(String userName);

        void setPresenter(ConversationContract.Presenter presenter);

        void navigateToMediaDetailView(int messageId);

    }

    interface Presenter {

        void onCreate(View view, int friendId, int chatId);

        void sendMessage(String text);

        void messageClicked(Message message);

        void onResume();

        void onPause();

    }

}
