package com.pgizka.gsenger.jobqueue.getContacts;

import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.jobqueue.BaseJob;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.ContactsUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

public class GetContactsJob extends BaseJob {
    static final String TAG = GetContactsJob.class.getSimpleName();

    transient private Realm realm;

    @Inject
    transient UserRestService userRestService;

    @Inject
    transient JobManager jobManager;

    @Inject
    transient EventBus eventBus;

    @Inject
    transient ContactsUtil contactsUtil;

    public GetContactsJob() {
        super(new Params(1).requireNetwork().addTags("refreshFriends"));
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        super.inject(applicationComponent);
        applicationComponent.inject(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Log.i(TAG, "getting contacts");
        List<String> phoneNumbers = contactsUtil.listAllContactsPhoneNumbers();
        GetContactsRequest friendsRequest = prepareRequest(phoneNumbers);

        Call<GetContactsResponse> call = userRestService.refreshFriends(friendsRequest);
        Response<GetContactsResponse> response = call.execute();

        if (response.isSuccess()) {
            Log.i(TAG, "response is success");
            processResponse(response.body());
        }

        Log.i(TAG, "posting end of job event");
        eventBus.post(new GetContactsFinishedEvent());
    }

    private GetContactsRequest prepareRequest(List<String> phoneNumbers) {
        GetContactsRequest getContactsRequest = new GetContactsRequest();
        getContactsRequest.setPhoneNumbers(phoneNumbers);
        return getContactsRequest;
    }

    private void processResponse(GetContactsResponse responseDTO) {
        /*List<User> foundFriends = responseDTO.getUsers();

        for (User foundFriend : foundFriends) {
            User localFriend = realm.where(User.class)
                                    .equalTo("serverId", foundFriend.getServerId())
                                    .findFirst();

            boolean friendExists = localFriend != null;
            if (friendExists) {
                checkUserPhotoActuality(foundFriend, localFriend);
                foundFriend.setId(localFriend.getId());
                friendRepository.updateFriend(foundFriend);
            } else {
                friendRepository.insertFriend(foundFriend);
                Chat currentChat = chatRepository.getConversationChatByFriendId(foundFriend.getId());
                if (currentChat == null) {
                    currentChat = new Chat();
                    currentChat.setType(GSengerContract.Chats.CHAT_TYPE_CONVERSATION);
                    chatRepository.insertChat(currentChat);
                    friendHasChatRepository.insertFriendHasChat(foundFriend.getId(), currentChat.getId());
                }
            }
        }*/
    }

    private void checkUserPhotoActuality(User foundUser, User localUser) {
        String remoteHash = foundUser.getPhotoHash();
        String localHash = localUser.getPhotoHash();

        if ((remoteHash != null && localHash == null) ||
                (remoteHash != null && localHash != null && !remoteHash.equals(localHash))) {
            //TODO enqueue new download photo job
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        Log.i(TAG, "onShouldReRunOnThrowable");
        eventBus.post(new GetContactsFinishedEvent());
        return RetryConstraint.CANCEL;
    }

    @Override
    protected void onCancel() {

    }
}
