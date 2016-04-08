package com.pgizka.gsenger.jobqueue.updateUser;


import com.path.android.jobqueue.Params;
import com.pgizka.gsenger.jobqueue.BaseJob;

public class UpdateUserStatusJob extends BaseJob {

    public UpdateUserStatusJob() {
        super(new Params(5).persist().requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected void onCancel() {

    }
}
