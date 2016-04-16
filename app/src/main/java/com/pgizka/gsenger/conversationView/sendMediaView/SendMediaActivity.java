package com.pgizka.gsenger.conversationView.sendMediaView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pgizka.gsenger.R;

public class SendMediaActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_media);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            SendMediaFragment sendMediaFragment = new SendMediaFragment();
            sendMediaFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.send_media_activity_container, sendMediaFragment)
                    .commit();
        }

    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
