package com.pgizka.gsenger.mainView.chats;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.provider.realm.Chat;
import com.pgizka.gsenger.provider.realm.Friend;
import com.pgizka.gsenger.provider.realm.MediaMessage;
import com.pgizka.gsenger.provider.realm.Message;
import com.pgizka.gsenger.provider.realm.TextMessage;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private List<Chat> chats;

    private OnChatClickListener onChatClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView mainImageView;
        public TextView chatNameTextView;
        public TextView descriptionTextView;
        public TextView dateTextView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.mainImageView = (ImageView) view.findViewById(R.id.chat_item_main_image);
            this.chatNameTextView = (TextView) view.findViewById(R.id.chat_item_chat_name_text);
            this.descriptionTextView = (TextView) view.findViewById(R.id.chat_item_description_text);
            this.dateTextView = (TextView) view.findViewById(R.id.chat_item_date_text);
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

        Friend friend = chat.getFriends().first();
        Message message = chat.getMessages().last();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChatClickListener != null) {
                    onChatClickListener.onChatClicked(chat);
                }
            }
        });

        if(chat.getType() == Chat.Type.SINGLE_CONVERSATION.code) {
            holder.chatNameTextView.setText(friend.getUserName());
        } else {
            holder.chatNameTextView.setText(chat.getChatName());

        }

        if(message.getType() == Message.Type.TEXT_MESSAGE.code) {
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date = simpleDateFormat.format(new Date(message.getSendDate()));
        holder.dateTextView.setText(date);
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
