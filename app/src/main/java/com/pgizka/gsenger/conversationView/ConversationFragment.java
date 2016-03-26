package com.pgizka.gsenger.conversationView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.Message;

import java.util.List;

import javax.inject.Inject;

public class ConversationFragment extends Fragment implements ConversationContract.View {

    @Inject
    ConversationContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private EditText messageText;
    private FloatingActionButton sendButton;
    private List<Message> messages;

    private ConversationAdapter conversationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);
        conversationAdapter = new ConversationAdapter();
        Bundle arguments = getArguments();
        int friendId = arguments.getInt(ConversationActivity.USER_ID_ARGUMENT);
        presenter.onCreate(this, friendId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        emptyTextView = (TextView) view.findViewById(R.id.conversation_empty_text_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.conversation_recycler_view);
        messageText = (EditText) view.findViewById(R.id.conversation_main_edit_text);
        sendButton = (FloatingActionButton) view.findViewById(R.id.conversation_send_message_floating_button);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(conversationAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(messageText.getText().toString());
                messageText.setText("");
                recyclerView.smoothScrollToPosition(messages.size());
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void displayConversationItems(List<Message> messages) {
        this.messages = messages;
        boolean noConversationItems = messages.size() == 0;
        if(noConversationItems) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            conversationAdapter.setMessages(messages);
            conversationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void displayErrorMessage(String message) {

    }

    @Override
    public void setPresenter(ConversationContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public AppCompatActivity getHoldingActivity() {
        return (AppCompatActivity) getActivity();
    }
}
