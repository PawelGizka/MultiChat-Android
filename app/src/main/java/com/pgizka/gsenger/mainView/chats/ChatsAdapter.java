package com.pgizka.gsenger.mainView.chats;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.provider.GSengerContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private List<ChatToDisplay> chatToDisplays;

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
        final ChatToDisplay chatToDisplay = chatToDisplays.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChatClickListener != null) {
                    onChatClickListener.onChatClicked(0, position, chatToDisplay);
                }
            }
        });

        if(chatToDisplay.getChatType().equals(GSengerContract.Chats.CHAT_TYPE_CONVERSATION)) {
            holder.chatNameTextView.setText(chatToDisplay.getFriendUserName());
        } else {
            holder.chatNameTextView.setText(chatToDisplay.getChatName());
        }

        if(chatToDisplay.getCommonTypeType().equals(GSengerContract.CommonTypes.COMMON_TYPE_MESSAGE)) {
            holder.descriptionTextView.setText(chatToDisplay.getMessageText());
        } else {
            if(chatToDisplay.getMediaType().equals(GSengerContract.Medias.MEDIA_TYPE_FILE)) {
                holder.descriptionTextView.setText("File");
            } else if(chatToDisplay.getMediaType().equals(GSengerContract.Medias.MEDIA_TYPE_PHOTO)) {
                holder.descriptionTextView.setText("Photo");
            } else if(chatToDisplay.getMediaType().equals(GSengerContract.Medias.MEDIA_TYPE_VIDEO)) {
                holder.descriptionTextView.setText("Video");
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date = simpleDateFormat.format(new Date(chatToDisplay.getCommonTypeSendDate()));
        holder.dateTextView.setText(date);


    }


    @Override
    public int getItemCount() {
        if(chatToDisplays != null) {
            return chatToDisplays.size();
        } else {
            return 0;
        }
    }

    public void setChatToDisplays(List<ChatToDisplay> chatToDisplays) {
        this.chatToDisplays = chatToDisplays;
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }

    public static interface OnChatClickListener {
        void onChatClicked(int chatId, int position, ChatToDisplay chatToDisplay);
    }

}
