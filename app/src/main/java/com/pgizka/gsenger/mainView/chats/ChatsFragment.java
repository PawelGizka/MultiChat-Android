package com.pgizka.gsenger.mainView.chats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pgizka.gsenger.R;


public class ChatsFragment extends Fragment implements ChatsView<ChatsToDisplayModel> {

    private ChatsPresenter presenter;

    private RecyclerView recyclerView;
    private TextView emptyTextView;

    private ChatsAdapter chatsAdapter;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatsAdapter = new ChatsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        emptyTextView = (TextView) view.findViewById(R.id.chats_empty_text_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.chats_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatsAdapter);

        chatsAdapter.setOnChatClickListener(new ChatsAdapter.OnChatClickListener() {
            @Override
            public void onChatClicked(int chatId, int position, ChatToDisplay chatToDisplay) {
                presenter.chatClicked(chatId, position, chatToDisplay);
            }
        });

        return view;
    }

    @Override
    public void setPresenter(ChatsPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public AppCompatActivity getHoldingActivity(){
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void displayChatsList(ChatsToDisplayModel model) {
        boolean noChats = model.getChatToDisplays().size() == 0;
        if(noChats) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            chatsAdapter.setChatToDisplays(model.getChatToDisplays());
            chatsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void displayErrorMessage(AlertDialog alertDialog) {
        alertDialog.show();
    }
}
