package com.pgizka.gsenger.dagger;

import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.api.ChatRestService;
import com.pgizka.gsenger.api.MessageRestService;
import com.pgizka.gsenger.api.UserRestService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;


public class TestApiModule extends ApiModule {

    @Override
    public OkHttpClient provideHttpClient() {
        return mock(OkHttpClient.class);
    }

    @Override
    public UserRestService providesUserRestService(Retrofit retrofit) {
        return mock(UserRestService.class);
    }

    @Override
    public MessageRestService providesMessageRestService(Retrofit retrofit) {
        return mock(MessageRestService.class);
    }

    @Override
    public ChatRestService providesChatRestService(Retrofit retrofit) {
        return mock(ChatRestService.class);
    }

}

