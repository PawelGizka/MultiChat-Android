package com.pgizka.gsenger.jobqueue.refreshFriends;

import com.pgizka.gsenger.io.RetrofitFactory;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;

public class RefreshFriendsRetrofit {

    public static interface RefreshFriends {

        @GET("friends")
        Call<RefreshFriendsResponseDTO> register(@Body RefreshFriendsRequestDTO requestDTO);

    }

    public static RefreshFriends getRefreshFriendsInterface() {
        boolean test = false;

        if (test) {

            RefreshFriends refreshFriends = new RefreshFriends() {
                @Override
                public Call<RefreshFriendsResponseDTO> register(@Body RefreshFriendsRequestDTO requestDTO) {
                    return new Call<RefreshFriendsResponseDTO>() {

                        @Override
                        public Response<RefreshFriendsResponseDTO> execute() throws IOException {
                            return Response.success(new RefreshFriendsResponseDTO());
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
        } else {
            return RetrofitFactory.getInstance().create(RefreshFriends.class);
        }

    }



}
