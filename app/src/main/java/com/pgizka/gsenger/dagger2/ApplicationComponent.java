package com.pgizka.gsenger.dagger2;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.conversationView.ConversationPresenter;
import com.pgizka.gsenger.jobqueue.refreshFriends.RefreshFriendsJob;
import com.pgizka.gsenger.mainView.chats.ChatsContract;
import com.pgizka.gsenger.mainView.chats.ChatsFragment;
import com.pgizka.gsenger.mainView.friends.FriendsContract;
import com.pgizka.gsenger.mainView.friends.FriendsFragment;
import com.pgizka.gsenger.mainView.friends.FriendsPresenter;
import com.pgizka.gsenger.util.ContactsUtil;
import com.pgizka.gsenger.util.UserAccountManager;
import com.pgizka.gsenger.welcome.registration.RegistrationTask;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    EventBus eventBus();

    JobManager jobManager();

    OkHttpClient okHttpClient();

    Retrofit retrofit();

    ContactsUtil contactsUtil();

    UserAccountManager userAccountManager();

    UserRestService userRestService();

    FriendsContract.Presenter friendsPresenter();

    ChatsContract.Presenter chatsPresenter();

    void inject(RefreshFriendsJob refreshFriendsJob);

    void inject(RegistrationTask registrationTask);

    void inject(FriendsPresenter friendsPresenter);

    void inject(ConversationPresenter conversationPresenter);

    void inject(FriendsFragment friendsFragment);

    void inject(ChatsFragment chatsFragment);

}
