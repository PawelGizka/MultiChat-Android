package com.pgizka.gsenger.createChatsView;


import android.text.TextUtils;

import com.pgizka.gsenger.api.ChatRestService;
import com.pgizka.gsenger.api.dtos.chats.PutChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatResponse;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.UserAccountManager;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChatPresenter implements CreateChatContract.Presenter {


    @Inject
    ChatRestService chatRestService;

    @Inject
    ChatRepository chatRepository;

    @Inject
    UserAccountManager userAccountManager;

    private CreateChatContract.View view;

    private Realm realm;

    @Override
    public void onCreate(CreateChatContract.View view) {
        GSengerApplication.getApplicationComponent().inject(this);
        this.view = view;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        //get all users in contacts except owner of this phone
        List<User> users = realm.where(User.class)
                .notEqualTo("id", 0) //owner id = 0
                .equalTo("inContacts", true) // only show users in contacts
                .findAll();
        view.displayUsersList(users);
    }

    @Override
    public void createChat(String chatName, List<User> participants) {
        if (TextUtils.isEmpty(chatName)) {
            view.displayErrorMessage("Please enter chat name");
            return;
        }

        if (participants.isEmpty()) {
            view.displayErrorMessage("Please select at least one participant");
            return;
        }

        view.showProgressDialog();

        participants.add(userAccountManager.getOwner());

        PutChatRequest putChatRequest = new PutChatRequest(chatName, participants);
        Call<PutChatResponse> call = chatRestService.createChat(putChatRequest);
        call.enqueue(new Callback<PutChatResponse>() {
            @Override
            public void onResponse(Call<PutChatResponse> call, Response<PutChatResponse> response) {
                view.dismissProgressDialog();
                if (response.isSuccess()) {
                    PutChatResponse putChatResponse = response.body();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    chatRepository.createGroupChat(putChatRequest, putChatResponse, participants);
                    realm.commitTransaction();
                    view.closeWindow();
                } else {
                    view.displayErrorMessage("Cannot create Chat, problem with connection.");
                }
            }

            @Override
            public void onFailure(Call<PutChatResponse> call, Throwable t) {
                view.dismissProgressDialog();
                view.displayErrorMessage("Cannot create Chat, problem with connection.");
            }
        });

    }
}
