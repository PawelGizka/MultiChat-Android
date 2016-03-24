package com.pgizka.gsenger.dagger2;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.pgizka.gsenger.api.ApiModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @VisibleForTesting
    public static void setApplicationComponent(ApplicationComponent applicationComponent) {
        GSengerApplication.applicationComponent = applicationComponent;
    }
}
