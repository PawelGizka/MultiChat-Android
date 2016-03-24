package com.pgizka.gsenger.jobqueue.getContacts;

import android.test.suitebuilder.annotation.SmallTest;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.TestUtils;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.util.ContactsUtil;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class RefreshFriendsJobTest {

    FriendRepository friendRepository;

    UserRestService userRestService;

    JobManager jobManager;

    EventBus eventBus;

    ContactsUtil contactsUtil;

    @Mock
    GSengerApplication gSengerApplication;

    private GetContactsJob getContactsJob;

    private ApplicationComponent applicationComponent;

    @Before
    public void setupRefreshFriendsJobTest() {
        MockitoAnnotations.initMocks(this);

        applicationComponent = TestUtils.getTestApplicationComponent(gSengerApplication);
        GSengerApplication.setApplicationComponent(applicationComponent);

        friendRepository = applicationComponent.friendRepository();
        userRestService = applicationComponent.userRestService();
        eventBus = applicationComponent.eventBus();
        contactsUtil = applicationComponent.contactsUtil();

        getContactsJob = new GetContactsJob();
    }

    @Test
    public void refreshFriendsSuccessfully() throws Throwable {
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("111");
        phoneNumbers.add("222");
        phoneNumbers.add("333");

        GetContactsRequest getContactsRequest = new GetContactsRequest();
        getContactsRequest.setPhoneNumbers(phoneNumbers);

        Friend friend1 = new Friend();
        int friend1ServerId = 0;
        friend1.setId(0);
        friend1.setServerId(friend1ServerId);
        friend1.setUserName("pawel");

        Friend friend2 = new Friend();
        int friend2ServerId = 1;
        friend2.setServerId(friend2ServerId);

        GetContactsResponse getContactsResponse = new GetContactsResponse();
        List<Friend> foundFriends = new ArrayList<>();
        foundFriends.add(friend1);
        foundFriends.add(friend2);
        getContactsResponse.setUsers(foundFriends);

        when(contactsUtil.listAllContactsPhoneNumbers()).thenReturn(phoneNumbers);
        when(userRestService.refreshFriends(getContactsRequest)).thenReturn(TestUtils.createCall(getContactsResponse));
        when(friendRepository.getFriendByServerId(friend1ServerId)).thenReturn(friend1);
        when(friendRepository.getFriendByServerId(friend2ServerId)).thenReturn(null);

        getContactsJob.inject(applicationComponent);
        getContactsJob.onRun();

        verify(friendRepository).updateFriend(friend1);
        verify(friendRepository).insertFriend(friend2);
        verify(eventBus).post(new GetContactsFinishedEvent());
    }




}
