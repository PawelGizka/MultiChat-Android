package com.pgizka.gsenger.dagger2;

import com.path.android.jobqueue.JobManager;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.api.UserRestService;
import com.pgizka.gsenger.conversationView.ConversationFragment;
import com.pgizka.gsenger.conversationView.ConversationPresenter;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.jobqueue.getContacts.GetContactsJob;
import com.pgizka.gsenger.jobqueue.sendMessge.SendMessageJob;
import com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob;
import com.pgizka.gsenger.jobqueue.updateUser.UpdateUserPhotoJob;
import com.pgizka.gsenger.jobqueue.updateUser.UpdateUserStatusJob;
import com.pgizka.gsenger.mainView.chats.ChatsContract;
import com.pgizka.gsenger.mainView.chats.ChatsFragment;
import com.pgizka.gsenger.mainView.friends.ContactsContract;
import com.pgizka.gsenger.mainView.friends.ContactsFragment;
import com.pgizka.gsenger.mainView.friends.ContactsPresenter;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.userStatusView.UserProfileFragment;
import com.pgizka.gsenger.userStatusView.UserProfilePresenter;
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

    Repository repository();

    UserAccountManager userAccountManager();

    ChatRepository chatRepository();

    void inject(MyGlideModule myGlideModule);

    void inject(GetContactsJob getContactsJob);

    void inject(SendMessageJob sendMessageJob);

    void inject(SetMessageStateJob setMessageStateJob);

    void inject(UpdateUserStatusJob updateUserStatusJob);

    void inject(UpdateUserPhotoJob updateUserPhotoJob);

    void inject(RegistrationTask registrationTask);

    void inject(NewTextMessageCommand newTextMessageCommand);

    void inject(ContactsPresenter contactsPresenter);

    void inject(ConversationPresenter conversationPresenter);

    void inject(UserProfilePresenter userProfilePresenter);

    void inject(ContactsFragment contactsFragment);

    void inject(ChatsFragment chatsFragment);

    void inject(ConversationFragment conversationFragment);

    void inject(UserProfileFragment userProfileFragment);

}
