package com.pgizka.gsenger.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserAccountManager {
    private static final String USER_ACCOUNT_PREFERENCES = "userAccountPreferences";

    private static final String USER_REGISTERED_KEY = "userRegisteredKey";
    private static final String USER_SERVER_ID_KEY = "userServerIdKey";

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

    public int setUserRegistered(int userServerId) {
        SharedPreferences sharedPreferences = getDefaultPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_REGISTERED_KEY, true);
        editor.putInt(USER_SERVER_ID_KEY, userServerId);
        editor.commit();
        return 0;
    }

}
