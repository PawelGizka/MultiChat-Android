package com.pgizka.gsenger.dagger2;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.util.StorageResolver;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import okhttp3.OkHttpClient;

public class GSengerApplication extends Application {
    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmMigration realmMigration = (realm, oldVersion, newVersion) -> {
            RealmSchema realmSchema = realm.getSchema();

            if (oldVersion == 0 || oldVersion == 1) {
                realmSchema.get("User")
                        .addField("phoneNumber", String.class)
                        .addField("isInContacts", boolean.class);
                oldVersion++;
                oldVersion++;
            }

            if (oldVersion == 2 || oldVersion == 3) {
                realmSchema.get("User")
                        .removeField("isInContacts")
                        .addField("inContacts", boolean.class);
                oldVersion++;
                oldVersion++;
            }

            if (oldVersion == 4) {
                realmSchema.get("User")
                        .removeField("phoneNumber")
                        .addField("phoneNumber", int.class);
                oldVersion++;
            }

            if (oldVersion == 5) {
                realmSchema.get("User")
                        .removeField("sentTextMessages")
                        .addField("sentMessages", Message.class);
                oldVersion++;
            }

            if (oldVersion == 6) {
                realmSchema.get("User")
                        .addField("fromPhoneNumbers", boolean.class)
                        .addField("fromFacebook", boolean.class);
                oldVersion++;
            }

        };

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .schemaVersion(7)
                .migration(realmMigration)
                .build());

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .apiModule(new ApiModule())
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
