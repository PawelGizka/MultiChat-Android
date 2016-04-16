package com.pgizka.gsenger.conversationView;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaActivity;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaFragment;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConversationFragment extends Fragment implements ConversationContract.View {

    @Inject
    ConversationContract.Presenter presenter;

    @Bind(R.id.conversation_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.conversation_empty_text_view) TextView emptyTextView;
    @Bind(R.id.conversation_main_edit_text) EditText messageText;
    private TextView usernameTextView;
    private ImageView userImageView;

    private List<Message> messages = new ArrayList<>();

    private ConversationAdapter conversationAdapter;

    private int friendId;
    private int chatId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        GSengerApplication.getApplicationComponent().inject(this);
        conversationAdapter = new ConversationAdapter();
        Bundle arguments = getArguments();
        friendId = arguments.getInt(ConversationActivity.USER_ID_ARGUMENT, -1);
        chatId = arguments.getInt(ConversationActivity.CHAT_ID_ARGUMENT, -1);
        presenter.onCreate(this, friendId, chatId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this, view);

        usernameTextView = (TextView) getActivity().findViewById(R.id.conversation_username_text);
        userImageView = (ImageView) getActivity().findViewById(R.id.conversation_user_image);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(conversationAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_conversation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.show_attachment_menu_action: {
                showAttachmentPopupMenu();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAttachmentPopupMenu() {
        View showAttachmentView = getActivity().findViewById(R.id.show_attachment_menu_action);
        PopupMenu popup = new PopupMenu(getActivity(), showAttachmentView);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_conversation_attachment, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            String action = null;
            if (id == R.id.chose_photo_action) {
                action = SendMediaFragment.CHOSE_PHOTO_ACTION;
            }

            Intent intent = new Intent(getContext(), SendMediaActivity.class);
            intent.putExtra(SendMediaFragment.ACTION_ARGUMENT, action);
            intent.putExtra(SendMediaFragment.USER_ID_ARGUMENT, friendId);
            intent.putExtra(SendMediaFragment.CHAT_ID_ARGUMENT, chatId);
            startActivity(intent);
            return true;
        });
    }

    @OnClick(R.id.conversation_send_message_floating_button)
    public void onSendMessageButtonClicked() {
        presenter.sendMessage(messageText.getText().toString());
        messageText.setText("");
        recyclerView.smoothScrollToPosition(messages.size());
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
    public void displayUserImage(User user) {
        Glide.with(this)
                .load(ApiModule.buildUserPhotoPath(user))
                .signature(new StringSignature(user.getPhotoHash()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImageView);
    }

    @Override
    public void displayUsername(String userName) {
        usernameTextView.setText(userName);
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
