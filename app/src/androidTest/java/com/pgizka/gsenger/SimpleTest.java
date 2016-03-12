package com.pgizka.gsenger;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.dagger2.DaggerSimpleComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.dagger2.SimpleComponent;
import com.pgizka.gsenger.dagger2.TestApiModule;
import com.pgizka.gsenger.jobqueue.RefreshFriendsJob;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SimpleTest {

    @Test
    public void testSimpleDependency() {
        SimpleComponent simpleComponent = DaggerSimpleComponent.builder()
                .releaseApiModule(new TestApiModule())
                .build();

        GSengerApplication.setSimpleComponent(simpleComponent);

        RefreshFriendsJob refreshFriendsJob = new RefreshFriendsJob();
        try {
            refreshFriendsJob.onRun();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        Assert.assertEquals("dupa", refreshFriendsJob.getSample());


    }

}
