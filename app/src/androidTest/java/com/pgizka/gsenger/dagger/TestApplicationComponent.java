package com.pgizka.gsenger.dagger;

import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.createChatsView.CreateChatPresenterTest;
import com.pgizka.gsenger.converstation.ConversationPresenterTest;
import com.pgizka.gsenger.converstation.SendMediaPresenterTest;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.ApplicationModule;
import com.pgizka.gsenger.gcm.NewGroupChatCommandTest;
import com.pgizka.gsenger.gcm.NewMediaMessageCommandTest;
import com.pgizka.gsenger.gcm.NewTextMessageCommandTest;
import com.pgizka.gsenger.provider.MessageRepositoryTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface TestApplicationComponent extends ApplicationComponent {

    void inject(ConversationPresenterTest conversationPresenterTest);

    void inject(SendMediaPresenterTest sendMediaPresenterTest);

    void inject(NewTextMessageCommandTest newTextMessageCommandTest);

    void inject(MessageRepositoryTest messageRepositoryTest);

    void inject(NewMediaMessageCommandTest newMediaMessageCommandTest);

    void inject(NewGroupChatCommandTest newGroupChatCommandTest);

    void inject(CreateChatPresenterTest createChatPresenterTest);

}
