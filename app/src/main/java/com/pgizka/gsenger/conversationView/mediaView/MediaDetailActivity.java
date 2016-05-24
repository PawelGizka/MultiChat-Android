package com.pgizka.gsenger.conversationView.mediaView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.pgizka.gsenger.R;

public class MediaDetailActivity extends AppCompatActivity {

    public static final String MESSAGE_ID_ARGUMENT = "messageIdArgument";

    private MediaDetailFragment mediaDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            mediaDetailFragment = new MediaDetailFragment();

            Bundle arguments = getIntent().getExtras();
            mediaDetailFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mediaDetailFragment)
                    .commit();

        }
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
