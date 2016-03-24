package com.pgizka.gsenger.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.welcome.registration.UserRegistrationRequest;
import com.pgizka.gsenger.welcome.registration.UserRegistrationResponse;

import io.realm.Realm;

public class UserAccountManager {
    private static final String USER_ACCOUNT_PREFERENCES = "userAccountPreferences";
    private static final String USER_REGISTERED_KEY = "userRegisteredKey";

    private Context context;

    public UserAccountManager(Context context) {
        this.context = context;
    }

    private SharedPreferences getDefaultPreferences() {
        return context.getSharedPreferences(USER_ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean isUserRegistered() {
        SharedPreferences sharedPreferences = getDefaultPreferences();
        return  sharedPreferences.getBoolean(USER_REGISTERED_KEY, false);
    }

    public int setUserRegistered(UserRegistrationRequest request, UserRegistrationResponse response) {
        SharedPreferences sharedPreferences = getDefaultPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_REGISTERED_KEY, true);
        editor.commit();

        Realm realm = Realm.getDefaultInstance();
        User user = new User();
        user.setId(0);
        user.setServerId(response.getUserId());
        user.setUserName(request.getUserName());
        user.setAddedDate(System.currentTimeMillis());

        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();

        return user.getServerId();
    }

}
