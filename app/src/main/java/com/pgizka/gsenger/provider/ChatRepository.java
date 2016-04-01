package com.pgizka.gsenger.provider;


import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.util.UserAccountManager;

import io.realm.Realm;
import io.realm.RealmList;

public class ChatRepository {

    private Repository repository;
    private UserAccountManager userAccountManager;

    public ChatRepository(Repository repository, UserAccountManager userAccountManager) {
        this.repository = repository;
        this.userAccountManager = userAccountManager;
    }

    public Chat getOrCreateSingleConversationChatWith(User friend) {
        Realm realm = Realm.getDefaultInstance();

        Chat chat = realm.where(Chat.class)
                .equalTo("users.id", friend.getId())
                .equalTo("users.id", userAccountManager.getOwner().getId())
                .equalTo("type", Chat.Type.SINGLE_CONVERSATION.code)
                .findFirst();

        if (chat == null) {
            chat = createSingleConversationChatWith(friend);
        }

        return chat;
    }

    public Chat createSingleConversationChatWith(User friend) {
        return createSingleConversationChatBetweenUsers(userAccountManager.getOwner(), friend);
    }

    public Chat createSingleConversationChatBetweenUsers(User firstUser, User secondUser) {
        Realm realm = Realm.getDefaultInstance();

        Chat chat = new Chat();
        chat.setId(repository.getChatNextId());
        chat.setType(Chat.Type.SINGLE_CONVERSATION.code);
        chat.setStartedDate(System.currentTimeMillis());

        chat = realm.copyToRealm(chat);

        chat.setUsers(new RealmList<>(firstUser, secondUser));
        firstUser.getChats().add(chat);
        secondUser.getChats().add(chat);

        return chat;
    }

}
