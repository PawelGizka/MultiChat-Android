package com.pgizka.gsenger.io;


import android.util.Log;

import java.io.IOException;
import java.net.URL;

import okhttp3.*;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static Retrofit retrofit;

    private static void init() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(httpLoggingInterceptor);
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.100:8080/GSengerGradle-1.0-SNAPSHOT/webresources/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized Retrofit getInstance() {
        if(retrofit == null) {
            init();
        }
        return retrofit;
    }

}
