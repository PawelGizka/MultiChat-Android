package com.pgizka.gsenger.dagger2;

import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.api.UserRestService;

import org.mockito.Mockito;

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
}
