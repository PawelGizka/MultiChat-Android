package com.pgizka.gsenger.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.welcome.GcmTokenObtainedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class GCMUTil {
    public static final String TAG = GCMUTil.class.getSimpleName();

    private static final String GCM_PREFERENCES = "gcmPreferences";
    private static final String GCM_TOKEN = "gcmToken";

    public static void register(Context context) {
        new RegistrationTak(context).execute();
    }

    static class RegistrationTak extends AsyncTask<Void, Void, Void> {
        private Context context;

        public RegistrationTak(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            InstanceID instanceID = InstanceID.getInstance(context);
            String token;
            try {
                token = instanceID.getToken(context.getString(R.string.google_app_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                Log.i(TAG, "GCM Registration Token: " + token);

                persistToken(token, context);
                sendBroadcast(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;


        }
    }

    private static void persistToken(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GCM_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GCM_TOKEN, token);
        editor.commit();
    }

    private static void sendBroadcast(String token) {
        GcmTokenObtainedEvent gcmTokenObtainedEvent = new GcmTokenObtainedEvent(token);
        EventBus.getDefault().post(gcmTokenObtainedEvent);
    }

    public static String getRegistrationToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GCM_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(GCM_TOKEN, null);
    }

    public static boolean isRegistered(Context context) {
        String token = getRegistrationToken(context);
        return !TextUtils.isEmpty(token);
    }

}
