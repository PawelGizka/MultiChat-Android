package com.pgizka.gsenger.api;

import com.pgizka.gsenger.jobqueue.getContacts.GetContactsResponse;
import com.pgizka.gsenger.jobqueue.getContacts.GetContactsRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationResponse;
import com.pgizka.gsenger.jobqueue.updateUser.UpdateUserStatusRequest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserRestService {

    @PUT("user/register/")
    Call<UserRegistrationResponse> register(@Body UserRegistrationRequest request);

    @POST("user/getContacts")
    Call<GetContactsResponse> getContacts(@Body GetContactsRequest getContactsRequest);

    @POST("user/updateStatus/{userId}")
    Call<ResponseBody> updateStatus(
            @Path("userId") int userServerId,
            @Body UpdateUserStatusRequest updateUserStatusRequest);

    @POST("user/updatePhoto/{userId}")
    Call<ResponseBody> updatePhoto(
            @Path("userId") int userServerId,
            @Body RequestBody requestBody);

}
