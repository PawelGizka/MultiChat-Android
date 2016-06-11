package com.pgizka.gsenger.addUsersToChatView;


import com.pgizka.gsenger.provider.User;

import java.util.List;

public interface AddUsersToChatContract {

    interface View {

        void displayUsersList(List<User> users);

        void displayErrorMessage(String errorMessage);

        void showProgressDialog();

        void dismissProgressDialog();

        void closeWindow();

    }

    interface Presenter {

        void onCreate(View view, int chatId);

        void onResume();

        void addUsers(List<User> users);

    }

}
