package com.pgizka.gsenger.api;

import com.pgizka.gsenger.api.dtos.chats.PutChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface ChatRestService {

    @PUT("chat")
    Call<PutChatResponse> createChat(@Body PutChatRequest putChatRequest);

}
