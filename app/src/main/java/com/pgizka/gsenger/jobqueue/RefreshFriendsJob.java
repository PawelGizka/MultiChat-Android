package com.pgizka.gsenger.jobqueue;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.dagger2.TestDependency;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsRequestDTO;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsResponseDTO;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsRetrofit;
import com.pgizka.gsenger.util.ContactsUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class RefreshFriendsJob extends Job {



    public RefreshFriendsJob() {
        super(new Params(1).persist().requireNetwork().addTags("refreshFriends"));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        List<String> phoneNumbers = ContactsUtil.listAllContactsPhoneNumbers(null);
        RefreshFriendsRequestDTO friendsRequestDTO = prepareRequest(phoneNumbers);

        Response<RefreshFriendsResponseDTO> response =
                RefreshFriendsRetrofit.getRefreshFriendsInterface().register(friendsRequestDTO).execute();

        if (response.isSuccess()) {
            processResponse(response.body());
        } else {
            throw new Exception();
        }

    }

    private RefreshFriendsRequestDTO prepareRequest(List<String> phoneNumbers) {

        return new RefreshFriendsRequestDTO();
    }

    private void processResponse(RefreshFriendsResponseDTO responseDTO) {

    }


    @Override
    protected void onCancel() {

    }
}
