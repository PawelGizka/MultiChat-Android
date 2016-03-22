package com.pgizka.gsenger.conversationView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.provider.realm.Message;

import java.util.List;

public class ConversationFragment extends Fragment implements ConversationContract.View {

    private ConversationContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView emptyTextView;

    private ConversationAdapter conversationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationAdapter = new ConversationAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        presenter = (ConversationContract.Presenter) getTargetFragment();

        emptyTextView = (TextView) view.findViewById(R.id.conversation_empty_text_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.conversation_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(conversationAdapter);

        return view;
    }

    @Override
    public void displayConversationItems(List<Message> messages) {
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
