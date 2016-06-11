package com.pgizka.gsenger.addUsersToChatView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pgizka.gsenger.R;

public class AddUsersToChatActivity extends AppCompatActivity {

    public static final String CHAT_ID_ARGUMENT = "chatIdArgument";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            AddUsersToChatFragment addUsersToChatFragment = new AddUsersToChatFragment();
            addUsersToChatFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, addUsersToChatFragment)
                    .commit();
        }

    }
}
