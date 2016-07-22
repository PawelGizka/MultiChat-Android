package com.pgizka.gsenger.config;


import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.provider.UserRepository;
import com.pgizka.gsenger.util.UserAccountManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    private final GSengerApplication application;

    public RepositoryModule(GSengerApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Repository providesRepository() {
        return new Repository(application);
    }


    @Provides
    @Singleton
    public UserAccountManager providesUserAccountManager() {
        return new UserAccountManager(application);
    }

    @Provides
    @Singleton
    public UserRepository providesUserRepository(Repository repository) {
        return new UserRepository(repository);
    }

    @Provides
    @Singleton
    public MessageRepository providesMessageRepository(Repository repository, ChatRepository chatRepository,
                                                       UserRepository userRepository, UserAccountManager userAccountManager) {
        return new MessageRepository(repository, chatRepository, userRepository, userAccountManager);
    }

    @Provides
    @Singleton
    public ChatRepository providesChatRepository(Repository repository, UserRepository userRepository,
                                                 UserAccountManager userAccountManager) {
        return new ChatRepository(repository, userRepository, userAccountManager);
    }

}
