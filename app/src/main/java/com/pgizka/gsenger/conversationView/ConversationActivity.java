package com.pgizka.gsenger.conversationView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pgizka.gsenger.R;

public class ConversationActivity extends AppCompatActivity {

    public static final String CHAT_ID_ARGUMENT = "chatIdArgument";
    public static final String FRIEND_ID_ARGUMENT = "friendIdArgument";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
    }


}
