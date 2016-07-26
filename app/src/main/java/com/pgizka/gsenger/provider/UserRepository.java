package com.pgizka.gsenger.provider;


import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class UserRepository {

    private Repository repository;

    public UserRepository(Repository repository) {
        this.repository = repository;
    }

    public List<User> insertFountContacts(List<User> fountContacts) {
        Realm realm = Realm.getDefaultInstance();
        List<User> insertedContacts = new ArrayList<>(fountContacts.size());

        for (User foundContact : fountContacts) {
            User localContact = realm.where(User.class)
                    .equalTo("serverId", foundContact.getServerId())
                    .findFirst();

            boolean contactExists = localContact != null;
            if (contactExists) {
                foundContact.setId(localContact.getId());
            } else {
                foundContact.setId(repository.getUserNextId());
                insertedContacts.add(foundContact);
            }

            realm.copyToRealmOrUpdate(foundContact);
        }
        return insertedContacts;
    }

    public User getOrCreateLocalUser(User userFromServer) {
        Realm realm = Realm.getDefaultInstance();

        User localUser = realm.where(User.class)
                .equalTo("serverId", userFromServer.getServerId())
                .findFirst();

        boolean localUserExists = localUser != null;
        if (!localUserExists) {
            localUser = userFromServer;
            localUser.setId(repository.getUserNextId());
            localUser.setInContacts(false);
            localUser = realm.copyToRealm(localUser);
        }

        return localUser;
    }

}
