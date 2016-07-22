package com.pgizka.gsenger.config;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.facebook.FacebookSdk;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.util.StorageResolver;
//import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class GSengerApplication extends Application {
    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .schemaVersion(7)
                .deleteRealmIfMigrationNeeded()
                .build());

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .apiModule(new ApiModule())
                .repositoryModule(new RepositoryModule(this))
                .build();


        StorageResolver storageResolver = applicationComponent.storageResolver();
        storageResolver.makeAllDirs();

        FacebookSdk.sdkInitialize(this);
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @VisibleForTesting
    public static void setApplicationComponent(ApplicationComponent applicationComponent) {
        GSengerApplication.applicationComponent = applicationComponent;
    }
}
