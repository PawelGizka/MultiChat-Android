package com.pgizka.gsenger.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserAccountManager {

    private static final String USER_ACCOUNT_PREFERENCES = "userAccountPreferences";

    private static final String USER_REGISTERED_KEY = "userRegisteredKey";
    private static final String USER_SERVER_ID_KEY = "userServerIdKey";

    private static SharedPreferences getDefaultPreferences(Context context) {
        return context.getSharedPreferences(USER_ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static boolean isUserRegistered(Context context) {
        SharedPreferences sharedPreferences = getDefaultPreferences(context);
        return  sharedPreferences.getBoolean(USER_REGISTERED_KEY, false);
    }

    public static void setUserRegistered(Context context, int userServerId) {
        SharedPreferences sharedPreferences = getDefaultPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_REGISTERED_KEY, true);
        editor.putInt(USER_SERVER_ID_KEY, userServerId);
        editor.commit();
    }

}
