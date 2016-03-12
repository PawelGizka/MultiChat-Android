package com.pgizka.gsenger.dagger2;

import android.app.Application;

import com.pgizka.gsenger.jobqueue.RefreshFriendsJob;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsRetrofit;
import com.pgizka.gsenger.mainView.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ReleaseApiModule.class})
public interface SimpleComponent {

    OkHttpClient getOkHttpClient();

    Retrofit getRetrofit();

    RefreshFriendsRetrofit.RefreshFriends getRefreshFriends();

    TestDependency getTestDependency();

}
