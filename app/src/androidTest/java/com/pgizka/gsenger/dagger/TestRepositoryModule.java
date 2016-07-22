package com.pgizka.gsenger.dagger;


import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.config.RepositoryModule;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.provider.UserRepository;
import com.pgizka.gsenger.util.UserAccountManager;

import org.mockito.Mockito;

public class TestRepositoryModule extends RepositoryModule{

    public TestRepositoryModule(GSengerApplication application) {
        super(application);
    }

    @Override
    public UserRepository providesUserRepository(Repository repository) {
        return Mockito.mock(UserRepository.class);
    }

    @Override
    public MessageRepository providesMessageRepository(Repository repository, ChatRepository chatRepository, UserRepository userRepository, UserAccountManager userAccountManager) {
        return Mockito.mock(MessageRepository.class);
    }

    @Override
    public ChatRepository providesChatRepository(Repository repository, UserRepository userRepository, UserAccountManager userAccountManager) {
        return Mockito.mock(ChatRepository.class);
    }
}
