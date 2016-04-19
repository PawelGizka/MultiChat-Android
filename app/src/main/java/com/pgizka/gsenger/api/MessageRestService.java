package com.pgizka.gsenger.api;

import com.pgizka.gsenger.jobqueue.sendMessge.PutMessageResponse;
import com.pgizka.gsenger.jobqueue.sendMessge.PutTextMessageRequest;
import com.pgizka.gsenger.jobqueue.setMessageState.MessageStateChangedRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationResponse;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MessageRestService {

    @PUT("message/text")
    Call<PutMessageResponse> sendTextMessage(@Body PutTextMessageRequest textMessageRequest);

    @Multipart
    @PUT("message/media")
    Call<PutMessageResponse> sendMediaMessage(
            @Part("data") RequestBody dataRequestBody,
            @Part("metadata") RequestBody metadataRequestBody);

    @GET("message/mediaData/{messageId}")
    Call<ResponseBody> getMediaMessageData(@Path("messageId") int messageId);

    @POST("message/delivered")
    Call<ResponseBody> setMessageDelivered(@Body MessageStateChangedRequest messageStateChangedRequest);

    @POST("message/viewed")
    Call<ResponseBody> setMessageViewed(@Body MessageStateChangedRequest messageStateChangedRequest);

}
