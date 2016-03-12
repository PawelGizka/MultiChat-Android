package com.pgizka.gsenger.io;

import com.pgizka.gsenger.welcome.registration.UserRegistrationRequestDTO;
import com.pgizka.gsenger.welcome.registration.UserRegistrationResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface UserRegistrationI {

    /**
     * Register user on server
     * @param requestDTO
     * @return
     */
    @PUT("user/register/")
    Call<UserRegistrationResponseDTO> register(@Body UserRegistrationRequestDTO requestDTO);

}
