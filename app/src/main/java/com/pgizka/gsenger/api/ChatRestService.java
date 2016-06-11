package com.pgizka.gsenger.api;

import com.pgizka.gsenger.api.dtos.chats.AddUsersToChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ChatRestService {

    @PUT("chat")
    Call<PutChatResponse> createChat(@Body PutChatRequest putChatRequest);

    @POST("chat/addUsers")
    Call<Response> addUsersToChat(@Body AddUsersToChatRequest addUsersToChatRequest);

}
