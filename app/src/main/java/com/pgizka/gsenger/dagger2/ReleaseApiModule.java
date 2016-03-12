package com.pgizka.gsenger.dagger2;

import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = ApiModule.class)
public class ReleaseApiModule {

    @Provides
    @Singleton
    public RefreshFriendsRetrofit.RefreshFriends provideRefreshFriendsService(Retrofit retrofit) {
        return retrofit.create(RefreshFriendsRetrofit.RefreshFriends.class);
    }

    @Provides
    @Singleton
    public TestDependency provideTestDependency() {
        return new TestDependency();
    }

}
