package com.pgizka.gsenger.mainView.chats;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private List<Chat> chats;

    private OnChatClickListener onChatClickListener;
    private Fragment fragment;

    public ChatsAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        @BindView(R.id.chat_item_main_image) ImageView mainImageView;
        @BindView(R.id.chat_item_chat_name_text) TextView chatNameTextView;
        @BindView(R.id.chat_item_description_text) TextView descriptionTextView;
        @BindView(R.id.chat_item_date_text) TextView dateTextView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        final Chat chat = chats.get(position);

        User user = chat.getUsers().first();

        Message message = null;
        RealmList<Message> messages = chat.getMessages();
        if (!messages.isEmpty()) {
            message = messages.last();
        }

        holder.view.setOnClickListener(v -> {
            if (onChatClickListener != null) {
                onChatClickListener.onChatClicked(chat);
            }
        });

        if(chat.getType() == Chat.Type.SINGLE_CONVERSATION.code) {
            if (user.getId() == 0) {
                user = chat.getUsers().last();
            }
            if (user.isInContacts()) {
                holder.chatNameTextView.setText(user.getUserName());
            } else {
                holder.chatNameTextView.setText(String.valueOf(user.getPhoneNumber()));
            }
        } else {
            holder.chatNameTextView.setText(chat.getChatName());

        }

        if (message == null) {
            holder.descriptionTextView.setText("No messages");
        } else if(message.getType() == Message.Type.TEXT_MESSAGE.code) {
            TextMessage textMessage = message.getTextMessage();
            holder.descriptionTextView.setText(textMessage.getText());
        } else {
            MediaMessage mediaMessage = message.getMediaMessage();
            int type = mediaMessage.getMediaType();
            if(type == MediaMessage.Type.FILE.code) {
                holder.descriptionTextView.setText("File");
            } else if(type == MediaMessage.Type.PHOTO.code) {
                holder.descriptionTextView.setText("Photo");
            } else if(type == MediaMessage.Type.VIDEO.code) {
                holder.descriptionTextView.setText("Video");
            }
        }

        if (message != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String date = simpleDateFormat.format(new Date(message.getSendDate()));
            holder.dateTextView.setText(date);
            holder.dateTextView.setVisibility(View.VISIBLE);
        } else {
            holder.dateTextView.setVisibility(View.GONE);
        }

        String userPhotoHash = user.getPhotoHash();
        if (userPhotoHash != null) {
            Glide.with(fragment)
                    .load(ApiModule.buildUserPhotoPath(user))
                    .signature(new StringSignature(userPhotoHash))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.defult_contact_image)
                    .into(holder.mainImageView);
        }
    }


    @Override
    public int getItemCount() {
        if(chats != null) {
            return chats.size();
        } else {
            return 0;
        }
    }

    public void setChatToDisplays(List<Chat> chats) {
        this.chats = chats;
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }

    public static interface OnChatClickListener {
        void onChatClicked(Chat chat);
    }

}
