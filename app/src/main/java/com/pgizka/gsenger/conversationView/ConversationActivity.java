package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pgizka.gsenger.R;

public class ConversationActivity extends AppCompatActivity {

    public static final String CHAT_ID_ARGUMENT = "chatIdArgument";
    public static final String FRIEND_ID_ARGUMENT = "friendIdArgument";

    private ConversationFragment conversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            conversationFragment = new ConversationFragment();

            Bundle arguments = getIntent().getExtras();
            conversationFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.conversation_activity_container, conversationFragment)
                    .commit();
        }

    }


}
