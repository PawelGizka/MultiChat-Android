package com.pgizka.gsenger.io;

import com.pgizka.gsenger.mainView.chats.ChatToDisplay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatsI {

    @GET("chats/{chats}/resp")
    Call<ChatToDisplay> listChats(@Path("/chats") String chatId);

}
