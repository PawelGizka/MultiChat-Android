package com.pgizka.gsenger.api;

import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsResponse;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserRestService {

    @PUT("user/register/")
    Call<UserRegistrationResponse> register(@Body UserRegistrationRequest request);

    @POST("user/getFriends")
    Call<RefreshFriendsResponse> refreshFriends(@Body RefreshFriendsRequest refreshFriendsRequest);

}
