package com.pgizka.gsenger.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pgizka.gsenger.gcm.GcmUtil;
import com.pgizka.gsenger.mainView.MainActivity;

public class BootStrapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!GcmUtil.isRegistered(this)) {
            GcmUtil.register(this);
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
