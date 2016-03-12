package com.pgizka.gsenger.dagger2;

import android.app.Application;

import com.pgizka.gsenger.BuildConfig;

public class GSengerApplication extends Application {

    private static SimpleComponent simpleComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        simpleComponent = DaggerSimpleComponent.builder()
                .releaseApiModule(new ReleaseApiModule())
                .build();
    }

    public static SimpleComponent getSimpleComponent() {
        return simpleComponent;
    }

    public static void setSimpleComponent(SimpleComponent simpleComponent) {
        GSengerApplication.simpleComponent = simpleComponent;
    }
}
