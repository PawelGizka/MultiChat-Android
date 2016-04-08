package com.pgizka.gsenger.dagger2;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.pgizka.gsenger.api.ApiModule;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;

public class GSengerApplication extends Application {
    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build());

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .apiModule(new ApiModule())
                .build();

        OkHttpClient okHttpClient = applicationComponent.okHttpClient();
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
        Picasso.setSingletonInstance(picasso);
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @VisibleForTesting
    public static void setApplicationComponent(ApplicationComponent applicationComponent) {
        GSengerApplication.applicationComponent = applicationComponent;
    }
}
