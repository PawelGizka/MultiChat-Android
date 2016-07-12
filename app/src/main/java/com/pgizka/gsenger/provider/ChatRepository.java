package com.pgizka.gsenger.provider;


import android.util.Log;

import com.pgizka.gsenger.api.dtos.chats.PutChatRequest;
import com.pgizka.gsenger.api.dtos.chats.PutChatResponse;
import com.pgizka.gsenger.api.dtos.chats.ChatData;
import com.pgizka.gsenger.util.UserAccountManager;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class ChatRepository {

    private Repository repository;
    private UserRepository userRepository;
    private UserAccountManager userAccountManager;

    public ChatRepository(Repository repository, UserRepository userRepository, UserAccountManager userAccountManager) {
        this.repository = repository;
        this.userRepository = userRepository;
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

    public Chat createGroupChat(PutChatRequest request, PutChatResponse response, List<User> participants) {
        Realm realm = Realm.getDefaultInstance();

        Chat chat = new Chat();
        chat.setId(repository.getChatNextId());
        chat.setServerId(response.getChatId());
        chat.setChatName(request.getChatName());
        chat.setStartedDate(request.getStartedDate());
        chat.setType(Chat.Type.GROUP.code);

        chat = realm.copyToRealm(chat);

        RealmList<User> realmList = new RealmList<>();
        for (User participant : participants) {
            realmList.add(participant);
            participant.getChats().add(chat);
        }

        chat.setUsers(realmList);


        return chat;
    }

    public Chat createGroupChat(ChatData chatData) {
        Realm realm = Realm.getDefaultInstance();

        int chatId = chatData.getChatId();
        Chat chat = realm.where(Chat.class).equalTo("serverId", chatId).findFirst();

        boolean chatAlreadyExists = chat != null;
        if (chatAlreadyExists) {
            return chat;
        }

        chat = new Chat();
        chat.setId(repository.getChatNextId());
        chat.setServerId(chatData.getChatId());
        chat.setType(Chat.Type.GROUP.code);
        chat.setChatName(chatData.getName());
        chat.setStartedDate(chatData.getStartedDate());

        chat = realm.copyToRealm(chat);

        RealmList<User> participants = new RealmList<>();
        for (User participant : chatData.getParticipants()) {
            User localParticipant = userRepository.getOrCreateLocalUser(participant);
            participants.add(localParticipant);
            localParticipant.getChats().add(chat);
        }
        chat.setUsers(participants);

        return chat;
    }

    public Chat addUsersToGroupChat(ChatData chatData) {
        Realm realm = Realm.getDefaultInstance();

        Chat chat = realm.where(Chat.class).equalTo("serverId", chatData.getChatId()).findFirst();
        if (chat == null) {
            Log.i("ChatRepository", "chat with server id: " + chatData.getChatId());
            return null;
        }

        addUsersToChat(chat, chatData.getParticipants());

        return null;
    }

    public void addUsersToChat(Chat chat, List<User> users) {
        Realm realm = Realm.getDefaultInstance();

        for (User user : users) {
            User alreadyAddedUser = realm.where(User.class).equalTo("chats.serverId", chat.getServerId()).findFirst();
            if (alreadyAddedUser == null) {
                user.getChats().add(chat);
                chat.getUsers().add(user);
            }
        }
    }

}
