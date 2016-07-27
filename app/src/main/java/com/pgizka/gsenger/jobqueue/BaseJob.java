package com.pgizka.gsenger.jobqueue;


import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.pgizka.gsenger.config.ApplicationComponent;

import io.realm.Realm;

abstract public class BaseJob extends Job {

    public BaseJob(Params params) {
        super(params);
    }

    public void inject(ApplicationComponent applicationComponent) {

    }
}
