package com.pgizka.gsenger.io;


import com.pgizka.gsenger.mainView.chats.ChatToDisplay;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DUP {

    public void nic() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("sfdsa")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChatsI chatsI = retrofit.create(ChatsI.class);

        Call<ChatToDisplay> chatCall = chatsI.listChats("id");

        try {
            chatCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        chatCall.enqueue(new Callback<ChatToDisplay>() {

            @Override
            public void onResponse(Call<ChatToDisplay> call, Response<ChatToDisplay> response) {

            }

            @Override
            public void onFailure(Call<ChatToDisplay> call, Throwable t) {

            }
        });

        Call<ChatToDisplay> chatCall1 = new Call<ChatToDisplay>() {
            @Override
            public Response<ChatToDisplay> execute() throws IOException {
                return Response.success(new ChatToDisplay());
            }

            @Override
            public void enqueue(Callback<ChatToDisplay> callback) {
                callback.onResponse(this, Response.success(new ChatToDisplay()));
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<ChatToDisplay> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

    }

}
