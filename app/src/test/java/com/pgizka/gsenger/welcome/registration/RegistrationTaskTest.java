package com.pgizka.gsenger.welcome.registration;


import android.test.suitebuilder.annotation.SmallTest;

import com.pgizka.gsenger.TestUtils;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.api.ResultCode;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.TestApiModule;
import com.pgizka.gsenger.dagger2.TestApplicationModule;
import com.pgizka.gsenger.dagger2.DaggerApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.util.UserAccountManager;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class RegistrationTaskTest {

    UserRestService userRestService;

    UserAccountManager userAccountManager;

    EventBus eventBus;

    @Mock
    GSengerApplication gSengerApplication;

    RegistrationTask registrationTask;

    @Before
    public void setupRegistrationTask() {
        MockitoAnnotations.initMocks(this);

        ApplicationComponent applicationComponent = TestUtils.getTestApplicationComponent(gSengerApplication);
        GSengerApplication.setApplicationComponent(applicationComponent);

        userRestService = applicationComponent.userRestService();
        userAccountManager = applicationComponent.userAccountManager();
        eventBus = applicationComponent.eventBus();

        registrationTask = new RegistrationTask("email", "userName", "password", 123, "token");
    }

    @Test
    public void registerSuccessfully() throws Exception {

        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setEmail("email");
        registrationRequest.setUserName("userName");
        registrationRequest.setPassword("password");
        registrationRequest.setPhoneNumber(123);
        registrationRequest.setGcmToken("token");

        UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse(ResultCode.OK.code);
        userRegistrationResponse.setUserId(1);

        when(userRestService.register(registrationRequest)).thenReturn(TestUtils.createCall(userRegistrationResponse));
        when(userAccountManager.setUserRegistered(1)).thenReturn(0);

        registrationTask.doInBackground(null);

        verify(userAccountManager).setUserRegistered(1);

        RegistrationEvent registrationEvent = new RegistrationEvent();
        registrationEvent.setSuccess(true);
        verify(eventBus).post(registrationEvent);
    }

}
