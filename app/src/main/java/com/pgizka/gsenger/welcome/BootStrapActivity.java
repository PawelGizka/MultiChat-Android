package com.pgizka.gsenger.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pgizka.gsenger.gcm.GCMUTil;
import com.pgizka.gsenger.mainView.MainActivity;
import com.pgizka.gsenger.provider.GSengerDatabase;
import com.pgizka.gsenger.util.UserAccountManager;

public class BootStrapActivity extends AppCompatActivity {
    static final String TAG = BootStrapActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!GCMUTil.isRegistered(this)) {
            GCMUTil.register(this);
        }

        if(WelcomeActivity.shouldDisplay(this)) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
