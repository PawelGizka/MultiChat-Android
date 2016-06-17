package com.pgizka.gsenger.addUsersToChatView;

import com.pgizka.gsenger.api.ChatRestService;
import com.pgizka.gsenger.api.dtos.chats.AddUsersToChatRequest;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUsersToChatPresenter implements AddUsersToChatContract.Presenter {

    @Inject
    ChatRestService chatRestService;

    @Inject
    ChatRepository chatRepository;

    private AddUsersToChatContract.View view;

    private Chat chat;
    private List<User> availableUsers;

    public AddUsersToChatPresenter() {
        GSengerApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void onCreate(AddUsersToChatContract.View view, int chatId) {
        this.view = view;

        Realm realm = Realm.getDefaultInstance();
        chat = realm.where(Chat.class).equalTo("id", chatId).findFirst();
    }

    @Override
    public void onResume() {
        getAvailableUsers();
        view.displayUsersList(availableUsers);
    }

    private void getAvailableUsers() {
        Realm realm = Realm.getDefaultInstance();

        RealmList<User> usersAlreadyInChat = chat.getUsers();

        List<User> allUsers = realm.where(User.class)
                .notEqualTo("id", 0)
                .equalTo("inContacts", true)
                .findAll();

        availableUsers = new ArrayList<>(allUsers.size());

        for (User user : allUsers) {
            User foundUser = usersAlreadyInChat.where()
                    .equalTo("id", user.getId())
                    .findFirst();
            boolean userAlreadyAdded = foundUser != null;
            if (!userAlreadyAdded) {
                availableUsers.add(user);
            }
        }
    }

    @Override
    public void addUsers(List<User> users) {
        AddUsersToChatRequest addUsersToChatRequest = new AddUsersToChatRequest(chat, users);
        Call<ResponseBody> call = chatRestService.addUsersToChat(addUsersToChatRequest);

        view.showProgressDialog();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                view.dismissProgressDialog();
                if (response.isSuccess()) {
                    chatRepository.addUsersToChat(chat, users);
                    view.closeWindow();
                } else {
                    view.displayErrorMessage("Could not add selected users to chat");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.dismissProgressDialog();
                view.displayErrorMessage("Could not add selected users to chat");
            }
        });
    }
}
