package com.pgizka.gsenger.provider;


import android.content.Context;
import android.content.SharedPreferences;

public class Repository {
    private static final String REPOSITORY_PREFERENCES = "realmRepositoryPreferences";

    private static final String USER_NEXT_ID = "userNextId";
    private static final String CHAT_NEXT_ID = "chatNextId";
    private static final String MESSAGE_NEXT_ID = "messageNextId";

    private Context context;

    public Repository(Context context) {
        this.context = context;
    }

    public int getUserNextId() {
        SharedPreferences preferences = getPreferences();
        int id = preferences.getInt(USER_NEXT_ID, 1); // id=0 is reserved for owner of this phone
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(USER_NEXT_ID, id + 1);
        editor.commit();
        return id;
    }

    public int getChatNextId() {
        return getNextId(CHAT_NEXT_ID);
    }

    public int getMessageNextId() {
        return getNextId(MESSAGE_NEXT_ID);
    }

    private int getNextId(String key) {
        SharedPreferences preferences = getPreferences();
        int id = preferences.getInt(key, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, id + 1);
        editor.commit();
        return id;
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(REPOSITORY_PREFERENCES, Context.MODE_PRIVATE);
    }

}
