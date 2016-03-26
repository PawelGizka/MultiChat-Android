package com.pgizka.gsenger.api;

import com.pgizka.gsenger.jobqueue.sendMessge.PutMessageResponse;
import com.pgizka.gsenger.jobqueue.sendMessge.PutTextMessageRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface MessageRestService {

    @PUT("message/text")
    Call<PutMessageResponse> sendTextMessage(@Body PutTextMessageRequest textMessageRequest);

}
