package com.pgizka.gsenger.dagger;

import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.converstation.ConversationPresenterTest;
import com.pgizka.gsenger.converstation.SendMediaPresenterTest;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.ApplicationModule;
import com.pgizka.gsenger.gcm.NewTextMessageCommandTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface TestApplicationComponent extends ApplicationComponent {

    void inject(ConversationPresenterTest conversationPresenterTest);

    void inject(SendMediaPresenterTest sendMediaPresenterTest);

    void inject(NewTextMessageCommandTest newTextMessageCommandTest);

}
