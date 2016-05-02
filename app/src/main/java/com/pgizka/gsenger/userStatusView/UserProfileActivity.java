package com.pgizka.gsenger.userStatusView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pgizka.gsenger.R;

public class UserProfileActivity extends AppCompatActivity {

    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        if (savedInstanceState == null) {
            userProfileFragment = new UserProfileFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, userProfileFragment)
                    .commit();

        }

    }

}
