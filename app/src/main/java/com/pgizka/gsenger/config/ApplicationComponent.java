package com.pgizka.gsenger.config;

import com.pgizka.gsenger.addUsersToChatView.AddUsersToChatFragment;
import com.pgizka.gsenger.addUsersToChatView.AddUsersToChatPresenter;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.conversationView.ConversationFragment;
import com.pgizka.gsenger.conversationView.ConversationPresenter;
import com.pgizka.gsenger.conversationView.mediaView.MediaDetailFragment;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaFragment;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaPresenter;
import com.pgizka.gsenger.createChatsView.CreateChatFragment;
import com.pgizka.gsenger.createChatsView.CreateChatPresenter;
import com.pgizka.gsenger.gcm.commands.AddUsersToGroupChatCommand;
import com.pgizka.gsenger.gcm.commands.AddedToChatCommand;
import com.pgizka.gsenger.gcm.commands.MessageStateChangedCommand;
import com.pgizka.gsenger.gcm.commands.NewGroupChatCommand;
import com.pgizka.gsenger.gcm.commands.NewMediaMessageCommand;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.jobqueue.getContacts.GetContactsJob;
import com.pgizka.gsenger.jobqueue.getMediaMessageData.GetMediaMessageDataJob;
import com.pgizka.gsenger.jobqueue.getMessages.GetMessagesJob;
import com.pgizka.gsenger.jobqueue.sendMessge.SendMessageJob;
import com.pgizka.gsenger.jobqueue.setMessageState.SetMessageStateJob;
import com.pgizka.gsenger.jobqueue.updateUser.UpdateUserPhotoJob;
import com.pgizka.gsenger.jobqueue.updateUser.UpdateUserStatusJob;
import com.pgizka.gsenger.mainView.chats.ChatsFragment;
import com.pgizka.gsenger.mainView.friends.ContactsFragment;
import com.pgizka.gsenger.mainView.friends.ContactsPresenter;
import com.pgizka.gsenger.provider.ChatRepository;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.userStatusView.UserProfileFragment;
import com.pgizka.gsenger.userStatusView.UserProfilePresenter;
import com.pgizka.gsenger.util.StorageResolver;
import com.pgizka.gsenger.util.UserAccountManager;
import com.pgizka.gsenger.welcome.registration.RegistrationFragment;
import com.pgizka.gsenger.welcome.registration.RegistrationTask;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    Repository repository();

    UserAccountManager userAccountManager();

    ChatRepository chatRepository();

    StorageResolver storageResolver();

    void inject(RegistrationFragment registrationFragment);

    void inject(MyGlideModule myGlideModule);

    void inject(GetContactsJob getContactsJob);

    void inject(SendMessageJob sendMessageJob);

    void inject(SetMessageStateJob setMessageStateJob);

    void inject(UpdateUserStatusJob updateUserStatusJob);

    void inject(UpdateUserPhotoJob updateUserPhotoJob);

    void inject(GetMediaMessageDataJob getMediaMessageDataJob);

    void inject(GetMessagesJob getMessagesJob);

    void inject(RegistrationTask registrationTask);

    void inject(NewTextMessageCommand newTextMessageCommand);

    void inject(NewMediaMessageCommand newMediaMessageCommand);

    void inject(NewGroupChatCommand newGroupChatCommand);

    void inject(MessageStateChangedCommand messageStateChangedCommand);

    void inject(AddedToChatCommand addedToChatCommand);

    void inject(AddUsersToGroupChatCommand addUsersToGroupChatCommand);

    void inject(ContactsPresenter contactsPresenter);

    void inject(ConversationPresenter conversationPresenter);

    void inject(UserProfilePresenter userProfilePresenter);

    void inject(ContactsFragment contactsFragment);

    void inject(ChatsFragment chatsFragment);

    void inject(ConversationFragment conversationFragment);

    void inject(UserProfileFragment userProfileFragment);

    void inject(SendMediaFragment sendMediaFragment);

    void inject(SendMediaPresenter sendMediaPresenter);

    void inject(CreateChatFragment chatFragment);

    void inject(CreateChatPresenter createChatPresenter);

    void inject(MediaDetailFragment mediaDetailFragment);

    void inject(AddUsersToChatFragment addUsersToChatFragment);

    void inject(AddUsersToChatPresenter addUsersToChatPresenter);
}
