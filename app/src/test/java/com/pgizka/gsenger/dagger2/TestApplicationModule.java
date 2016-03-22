package com.pgizka.gsenger.dagger2;


import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.util.ContactsUtil;
import com.pgizka.gsenger.util.UserAccountManager;

import org.greenrobot.eventbus.EventBus;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class TestApplicationModule extends ApplicationModule {

    public TestApplicationModule(GSengerApplication application) {
        super(application);
    }

    @Override
    public ProviderUtils providesProviderUtils() {
        return mock(ProviderUtils.class);
    }

    @Override
    public FriendRepository providesFriendsRepository(ProviderUtils providerUtils) {
        return mock(FriendRepository.class);
    }

    @Override
    public EventBus providesEventBus() {
        return mock(EventBus.class);
    }

    @Override
    public JobManager providesJobManager() {
        return mock(JobManager.class);
    }

    @Override
    public UserAccountManager providesUserAccountManager() {
        return mock(UserAccountManager.class);
    }

    @Override
    public ContactsUtil providesContactsUtil() {
        return mock(ContactsUtil.class);
    }
}
