package com.pgizka.gsenger.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.api.dtos.user.UserRegistrationRequest;
import com.pgizka.gsenger.api.dtos.user.UserRegistrationResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.realm.Realm;

public class UserAccountManager {
    private static final String USER_ACCOUNT_PREFERENCES = "userAccountPreferences";
    private static final String USER_REGISTERED_KEY = "userRegisteredKey";

    private static final String OWNER_IMAGE_PATH = "ownerImage";

    private Context context;

    public UserAccountManager(Context context) {
        this.context = context;
    }

    public User getOwner() {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(User.class)
                .equalTo("id", 0)
                .findFirst();
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

    private SharedPreferences getDefaultPreferences() {
        return context.getSharedPreferences(USER_ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String saveOwnerImage(Uri uri) {

        String photoPath = OWNER_IMAGE_PATH;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            File existingPhoto = new File(context.getFilesDir(), photoPath);
            if (existingPhoto.exists()) {
                existingPhoto.delete();
            }
            outputStream = context.openFileOutput(photoPath, Context.MODE_PRIVATE);
            inputStream = context.getContentResolver().openInputStream(uri);

            byte[] buffer = new byte[8192];
            int cnt = 0;
            while ((cnt = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, cnt);
            }

            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return photoPath;
    }

    public File getOwnerImage() {
        return new File(context.getFilesDir(), OWNER_IMAGE_PATH);
    }

}
