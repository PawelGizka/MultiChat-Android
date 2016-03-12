package com.pgizka.gsenger.dagger2;

import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsRequestDTO;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsResponseDTO;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsRetrofit;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;


public class TestApiModule extends ReleaseApiModule {


    public RefreshFriendsRetrofit.RefreshFriends provideRefreshFriendsService(Retrofit retrofit) {
        RefreshFriendsRetrofit.RefreshFriends refreshFriends = new RefreshFriendsRetrofit.RefreshFriends() {
            @Override
            public Call<RefreshFriendsResponseDTO> register(@Body RefreshFriendsRequestDTO requestDTO) {
                return new Call<RefreshFriendsResponseDTO>() {
                    @Override
                    public Response<RefreshFriendsResponseDTO> execute() throws IOException {
                        return null;
                    }

                    @Override
                    public void enqueue(Callback<RefreshFriendsResponseDTO> callback) {

                    }

                    @Override
                    public boolean isExecuted() {
                        return false;
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public boolean isCanceled() {
                        return false;
                    }

                    @Override
                    public Call<RefreshFriendsResponseDTO> clone() {
                        return null;
                    }

                    @Override
                    public Request request() {
                        return null;
                    }
                };
            }
        };
        return refreshFriends;
    }

    public TestDependency provideTestDependency() {
        return new ETestDependency();
    }

}
