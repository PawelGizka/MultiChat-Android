package com.pgizka.gsenger.createChatsView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pgizka.gsenger.R;

public class CreateChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Chat");

        if (savedInstanceState == null) {
            CreateChatFragment createChatFragment = new CreateChatFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, createChatFragment)
                    .commit();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
