package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pgizka.gsenger.R;

public class ConversationActivity extends AppCompatActivity {

    public static final String CHAT_ID_ARGUMENT = "chatIdArgument";
    public static final String FRIEND_ID_ARGUMENT = "friendIdArgument";

    private static final String CONVERSATION_PRESENTER_TAG = "conversationPresenterTag";

    private ConversationFragment conversationFragment;
    private ConversationPresenter conversationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

            conversationFragment = new ConversationFragment();
            conversationPresenter = new ConversationPresenter();

            Bundle arguments = getIntent().getExtras();
            conversationFragment.setArguments(arguments);
            conversationPresenter.setArguments(arguments);

            conversationFragment.setTargetFragment(conversationPresenter, 0);
            conversationPresenter.setTargetFragment(conversationFragment, 0);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.conversation_activity_container, conversationFragment)
                    .add(conversationPresenter, CONVERSATION_PRESENTER_TAG)
                    .commit();
        }

    }


}
