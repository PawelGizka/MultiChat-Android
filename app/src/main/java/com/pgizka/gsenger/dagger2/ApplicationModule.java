package com.pgizka.gsenger.dagger2;

import android.content.Context;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.repositories.FriendRepository;
import com.pgizka.gsenger.util.ContactsUtil;
import com.pgizka.gsenger.util.UserAccountManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final GSengerApplication application;

    public ApplicationModule(GSengerApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public ProviderUtils providesProviderUtils() {
        return new ProviderUtils(application);
    }

    @Provides
    @Singleton
    public FriendRepository providesFriendsRepository(ProviderUtils providerUtils) {
        return new FriendRepository(application, providerUtils);
    }

    @Provides
    @Singleton
    public EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    public JobManager providesJobManager() {
        Configuration config = new Configuration.Builder(application)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
//                .customLogger(L.getJobLogger())
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(application.getApplicationComponent());
                        }
                    }
                })
                .build();
        return new JobManager(application, config);
    }

    @Provides
    @Singleton
    public UserAccountManager providesUserAccountManager() {
        return new UserAccountManager(application);
    }

    @Provides
    @Singleton
    public ContactsUtil providesContactsUtil() {
        return new ContactsUtil(application);
    }

}
