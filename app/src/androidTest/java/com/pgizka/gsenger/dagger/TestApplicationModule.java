package com.pgizka.gsenger.dagger;


import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.config.ApplicationModule;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.provider.UserRepository;
import com.pgizka.gsenger.util.UserAccountManager;

import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class TestApplicationModule extends ApplicationModule {

    public TestApplicationModule(GSengerApplication application) {
        super(application);
    }

    @Override
    public MessageRepository providesMessageRepository(Repository repository, ChatRepository chatRepository, UserRepository userRepository, UserAccountManager userAccountManager) {
        return mock(MessageRepository.class);
    }

    @Override
    public JobManager providesJobManager() {
        return mock(JobManager.class);
    }
}
