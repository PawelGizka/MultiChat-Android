package com.pgizka.gsenger.io;

import com.pgizka.gsenger.welcome.registration.UserRegistrationRequestDTO;

import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface UserRestService {

    @PUT("/user/register")
    public void registerUser(@Body UserRegistrationRequestDTO requestDTO);

}
