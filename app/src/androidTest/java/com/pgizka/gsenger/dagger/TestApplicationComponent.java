package com.pgizka.gsenger.dagger;

import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.converstation.ConversationTest;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface TestApplicationComponent extends ApplicationComponent {

    void inject(ConversationTest conversationTest);

}
