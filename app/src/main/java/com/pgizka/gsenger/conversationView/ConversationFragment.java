package com.pgizka.gsenger.conversationView;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.pgizka.gsenger.addUsersToChatView.AddUsersToChatActivity;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.conversationView.mediaView.MediaDetailActivity;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaActivity;
import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaFragment;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ConversationFragment extends Fragment implements ConversationContract.View {

    @Inject
    ConversationContract.Presenter presenter;

    @BindView(R.id.conversation_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.conversation_empty_text_view) TextView emptyTextView;
    @BindView(R.id.conversation_main_edit_text) EditText messageText;

    private TextView usernameTextView;
    private ImageView userImageView;

    private List<Message> messages = new ArrayList<>();
    private boolean groupChat;

    private ConversationAdapter conversationAdapter;

    private int chatId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        GSengerApplication.getApplicationComponent().inject(this);
        conversationAdapter = new ConversationAdapter(this);
        Bundle arguments = getArguments();
        chatId = arguments.getInt(ConversationActivity.CHAT_ID_ARGUMENT, -1);
        presenter.onCreate(this, chatId);
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

        conversationAdapter.setOnMessageClickListener(message -> presenter.messageClicked(message));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.add_users_to_chat_action);
        boolean isVisible = groupChat;
        menuItem.setVisible(isVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.show_attachment_menu_action: {
                showAttachmentPopupMenu();
                return true;
            }
            case R.id.add_users_to_chat_action: {
                navigateToAddUsersToChatView();
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
            } else if (id == R.id.take_photo_action) {
                action = SendMediaFragment.TAKE_PHOTO_ACTION;
            } else if (id == R.id.chose_video_action) {
                action = SendMediaFragment.CHOSE_VIDEO_ACTION;
            } else if (id == R.id.take_video_action) {
                action = SendMediaFragment.TAKE_VIDEO_ACTION;
            } else if (id == R.id.chose_file_action) {
                action = SendMediaFragment.CHOSE_FILE_ACTION;
            }

            Intent intent = new Intent(getContext(), SendMediaActivity.class);
            intent.putExtra(SendMediaFragment.ACTION_ARGUMENT, action);
            intent.putExtra(SendMediaFragment.CHAT_ID_ARGUMENT, chatId);
            startActivity(intent);
            return true;
        });
    }

    private void navigateToAddUsersToChatView() {
        Intent intent = new Intent(getActivity(), AddUsersToChatActivity.class);
        intent.putExtra(AddUsersToChatActivity.CHAT_ID_ARGUMENT, chatId);
        startActivity(intent);
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
    public void displayChatImage(User user) {
        Glide.with(this)
                .load(ApiModule.buildUserPhotoPath(user))
                .signature(new StringSignature(user.getPhotoHash()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImageView);
    }

    @Override
    public void displayChatName(String userName) {
        usernameTextView.setText(userName);
    }

    @Override
    public void navigateToMediaDetailView(int messageId) {
        Intent intent = new Intent(getActivity(), MediaDetailActivity.class);
        intent.putExtra(MediaDetailActivity.MESSAGE_ID_ARGUMENT, messageId);
        startActivity(intent);
    }

    @Override
    public void setGroupChat(boolean groupChat) {
        this.groupChat = groupChat;
    }
}
