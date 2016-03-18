package com.pgizka.gsenger.provider.repositories;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.pgizka.gsenger.TestUtils;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.Friend;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class FriendRepositoryTest {

    private Context context;
    private ProviderUtils providerUtils;

    private FriendRepository friendRepository;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getContext();
        TestUtils.cleanDB(context);
        if(providerUtils == null) {
            providerUtils = new ProviderUtils(context);
        }
        friendRepository = new FriendRepository(context, providerUtils);
    }

    @Test
    public void insertNewFriend() {
        Friend friend = TestUtils.prepareFriendStub();

        int id1 = friendRepository.insertFriend(friend);
        Friend insertedFriend1 = friendRepository.getFriendById(id1);
        assertEquals(friend.getUserName(), insertedFriend1.getUserName());

        //insert second friend
        int id2 = friendRepository.insertFriend(friend);
        Friend insertedFriend2 = friendRepository.getFriendById(id2);
        assertEquals(friend.getUserName(), insertedFriend2.getUserName());
    }

    @Test
    public void testUpdate() {
        Friend friend = TestUtils.prepareFriendStub();
        String userName = "asia";

        int id = friendRepository.insertFriend(friend);
        friend.setUserName(userName);
        friendRepository.updateFriend(friend);

        friend = friendRepository.getFriendById(id);
        assertEquals(friend.getUserName(), userName);
    }

    @Test
    public void testGetByServerId() {
        Friend friend = TestUtils.prepareFriendStub();
        int serverId = 12;
        friend.setServerId(12);

        friendRepository.insertFriend(friend);

        friend = friendRepository.getFriendByServerId(serverId);
        assertNotNull(friend);
    }

    @Test
    public void testDelete() {
        Friend friend1 = TestUtils.prepareFriendStub();
        Friend friend2 = TestUtils.prepareFriendStub();

        int id1 = friendRepository.insertFriend(friend1);
        int id2 = friendRepository.insertFriend(friend2);

        friendRepository.deleteFriend(friend1);

        friend1 = friendRepository.getFriendById(id1);
        assertNull(friend1);

        friend2 = friendRepository.getFriendById(id2);
        assertNotNull(friend2);
    }

}
