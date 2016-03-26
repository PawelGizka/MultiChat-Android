package com.pgizka.gsenger.conversationView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.pgizka.gsenger.provider.Message.State.CANNOT_SEND;
import static com.pgizka.gsenger.provider.Message.State.RECEIVED;
import static com.pgizka.gsenger.provider.Message.State.SENDING;
import static com.pgizka.gsenger.provider.Message.State.SENT;
import static com.pgizka.gsenger.provider.Message.State.WAITING_TO_SEND;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;

    private List<Message> messages;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView infoText;
        public ProgressBar progressBar;
        public TextView fileNameText;
        public TextView filePathText;
        public ImageView image;
        public ImageView videoImage;
        public TextView messageText;
        public FrameLayout imageFrameLayout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.infoText = (TextView) view.findViewById(R.id.message_info_text);
            this.progressBar = (ProgressBar) view.findViewById(R.id.message_progress_bar);
            this.fileNameText = (TextView) view.findViewById(R.id.message_file_name_text);
            this.filePathText = (TextView)view.findViewById(R.id.message_file_path_text);
            this.image = (ImageView) view.findViewById(R.id.message_image_view);
            this.videoImage = (ImageView) view.findViewById(R.id.message_video_image_view);
            this.messageText = (TextView) view.findViewById(R.id.message_text_view);
            this.imageFrameLayout = (FrameLayout) view.findViewById(R.id.message_image_frame_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        if (viewType == VIEW_TYPE_LEFT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_left, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_right, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        Message message = messages.get(position);

        holder.progressBar.setVisibility(View.GONE);
        holder.fileNameText.setVisibility(View.GONE);
        holder.filePathText.setVisibility(View.GONE);
        holder.image.setVisibility(View.GONE);
        holder.videoImage.setVisibility(View.GONE);
        holder.image.setVisibility(View.GONE);
        holder.imageFrameLayout.setVisibility(View.GONE);

        if(message.getType() == Message.Type.TEXT_MESSAGE.code) {
            TextMessage textMessage = message.getTextMessage();
            holder.messageText.setText(textMessage.getText());
        } else {
            MediaMessage mediaMessage = message.getMediaMessage();
        }

        String sendDate = getSendDate(message);
        String status = getStatus(message);

        User sender = message.getSender();
        boolean outgoing = sender.getId() == 0;
        if (outgoing) {
            holder.infoText.setText(status + " " + sendDate + " Me:");
        } else {
            holder.infoText.setText(status + " " + sendDate + sender.getUserName());
        }
    }

    private String getSendDate(Message message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return simpleDateFormat.format(new Date(message.getSendDate()));
    }

    private String getStatus(Message message) {
        int state = message.getState();
        if (state == WAITING_TO_SEND.code) {
            return "Waiting to send";
        } else if (state == SENDING.code) {
            return "Sending...";
        } else if (state == CANNOT_SEND.code) {
            return "Cannot send";
        } else if (state == SENT.code) {
            return "Sent";
        } else if (state == RECEIVED.code) {
            return "Received";
        } else {
            return "";
        }
    }


    @Override
    public int getItemCount() {
        if(messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        boolean isOutgoing = message.getSender().getId() == 0;
        if (isOutgoing) {
            return VIEW_TYPE_LEFT;
        } else {
            return VIEW_TYPE_RIGHT;
        }
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
