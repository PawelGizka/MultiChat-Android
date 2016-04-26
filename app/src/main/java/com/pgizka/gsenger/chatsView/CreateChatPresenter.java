package com.pgizka.gsenger.chatsView;


import android.text.TextUtils;

import com.pgizka.gsenger.api.ChatRestService;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.jobqueue.chats.PutChatRequest;
import com.pgizka.gsenger.jobqueue.chats.PutChatResponse;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.User;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChatPresenter implements CreateChatContract.Presenter {


    @Inject
    ChatRestService chatRestService;

    @Inject
    ChatRepository chatRepository;

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

        PutChatRequest putChatRequest = new PutChatRequest(chatName, participants);
        Call<PutChatResponse> call = chatRestService.createChat(putChatRequest);
        call.enqueue(new Callback<PutChatResponse>() {
            @Override
            public void onResponse(Call<PutChatResponse> call, Response<PutChatResponse> response) {
                view.dismissProgressDialog();
                if (response.isSuccess()) {
                    PutChatResponse putChatResponse = response.body();
                    chatRepository.createGroupChat(putChatResponse.getChatId(), chatName, putChatRequest.getStartedDate(), participants);
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
