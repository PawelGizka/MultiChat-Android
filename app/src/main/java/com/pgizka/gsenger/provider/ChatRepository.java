package com.pgizka.gsenger.provider;


import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.util.UserAccountManager;

import java.util.List;

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
        Chat chat = getSingleConversationChatWith(friend);

        if (chat == null) {
            chat = createSingleConversationChatWith(friend);
        }

        return chat;
    }

    public Chat getSingleConversationChatWith(User friend) {
        Realm realm = Realm.getDefaultInstance();

        Chat chat = realm.where(Chat.class)
                .equalTo("users.id", friend.getId())
                .equalTo("users.id", userAccountManager.getOwner().getId())
                .equalTo("type", Chat.Type.SINGLE_CONVERSATION.code)
                .findFirst();

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

    public Chat createGroupChat(int chatServerId, String chatName, long startedDate, List<User> participants) {
        Realm realm = Realm.getDefaultInstance();

        Chat chat = new Chat();
        chat.setId(repository.getChatNextId());
        chat.setServerId(chatServerId);
        chat.setChatName(chatName);
        chat.setStartedDate(startedDate);
        chat.setType(Chat.Type.GROUP.code);

        RealmList<User> realmList = new RealmList<>();
        for (User participant : participants) {
            realmList.add(participant);
        }
        realmList.add(userAccountManager.getOwner());

        chat.setUsers(realmList);

        realm.beginTransaction();
        chat = realm.copyToRealm(chat);
        realm.commitTransaction();

        return chat;
    }

}
