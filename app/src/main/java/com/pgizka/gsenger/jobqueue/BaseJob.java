package com.pgizka.gsenger.jobqueue;


import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.pgizka.gsenger.config.ApplicationComponent;

import io.realm.Realm;

abstract public class BaseJob extends Job {

    public BaseJob(Params params) {
        super(params);
    }

    protected void findResource(Realm realm, Function1 function1) {
        Object foundObject = function1.apply();
        if (foundObject == null) {
            realm.waitForChange();
            function1.apply();
        }
    }

    public interface Function1 {
        Object apply();
    }

    public void inject(ApplicationComponent applicationComponent) {

    }
}
