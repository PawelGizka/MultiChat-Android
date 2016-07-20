package com.pgizka.gsenger.conversationView;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.provider.MediaMessage;
import com.pgizka.gsenger.provider.Message;
import com.pgizka.gsenger.provider.ReceiverInfo;
import com.pgizka.gsenger.provider.TextMessage;
import com.pgizka.gsenger.provider.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pgizka.gsenger.provider.Message.State.CANNOT_SEND;
import static com.pgizka.gsenger.provider.Message.State.DOWNLOADING;
import static com.pgizka.gsenger.provider.Message.State.RECEIVED;
import static com.pgizka.gsenger.provider.Message.State.SENDING;
import static com.pgizka.gsenger.provider.Message.State.SENT;
import static com.pgizka.gsenger.provider.Message.State.WAITING_TO_DOWNLOAD;
import static com.pgizka.gsenger.provider.Message.State.WAITING_TO_SEND;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;

    private List<Message> messages;

    private Fragment fragment;

    private OnMessageClickListener onMessageClickListener;

    public ConversationAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.message_info_text) TextView infoText;
        @BindView(R.id.message_progress_bar) ProgressBar progressBar;
        @BindView(R.id.message_file_name_text) TextView fileNameText;
        @BindView(R.id.message_file_path_text) TextView filePathText;
        @BindView(R.id.message_image_view) ImageView image;
        @BindView(R.id.message_video_image_view) ImageView videoImage;
        @BindView(R.id.message_text_view) TextView messageText;
        @BindView(R.id.message_image_frame_layout) FrameLayout imageFrameLayout;
        @BindView(R.id.header) RelativeLayout headerRelativeLayout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
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
        holder.headerRelativeLayout.setVisibility(View.GONE);

        holder.view.setOnClickListener(v -> {
            if (onMessageClickListener != null) {
                onMessageClickListener.onMessageClicked(message);
            }
        });

        if(message.getType() == Message.Type.TEXT_MESSAGE.code) {
            TextMessage textMessage = message.getTextMessage();
            holder.messageText.setVisibility(View.VISIBLE);
            holder.messageText.setText(textMessage.getText());
        } else {
            MediaMessage mediaMessage = message.getMediaMessage();

            String description = mediaMessage.getDescription();
            if (!TextUtils.isEmpty(description)) {
                holder.messageText.setVisibility(View.VISIBLE);
                holder.messageText.setText(description);
            } else {
                holder.messageText.setVisibility(View.GONE);
            }

            int type = mediaMessage.getMediaType();
            if (type == MediaMessage.Type.PHOTO.code) {
                String path = mediaMessage.getPath();
                if (!TextUtils.isEmpty(path)) {
                    holder.imageFrameLayout.setVisibility(View.VISIBLE);
                    holder.image.setVisibility(View.VISIBLE);

                    Glide.with(fragment)
                            .load(path)
                            .into(holder.image);
                }
            } else if (type == MediaMessage.Type.VIDEO.code) {
                String path = mediaMessage.getPath();
                if (!TextUtils.isEmpty(path)) {
                    holder.imageFrameLayout.setVisibility(View.VISIBLE);
                    holder.videoImage.setVisibility(View.VISIBLE);
                    holder.image.setVisibility(View.VISIBLE);

                    Glide.with(fragment)
                            .load(path)
                            .asBitmap()
                            .videoDecoder(new FileDescriptorBitmapDecoder(fragment.getActivity()))
                            .into(holder.image);
                }
            } else if (type == MediaMessage.Type.FILE.code) {
                holder.fileNameText.setVisibility(View.VISIBLE);
                holder.filePathText.setVisibility(View.VISIBLE);

                holder.fileNameText.setText(mediaMessage.getFileName());
                holder.filePathText.setText(mediaMessage.getPath());
            }

        }

        String sendDate = getSendDate(message);
        String status = getStatus(message);

        User sender = message.getSender();
        boolean outgoing = sender.getId() == 0;
        if (outgoing) {
            holder.infoText.setText(status + " " + sendDate + " Me:");
        } else {
            String userName;
            if (sender.isInContacts()) {
                userName = sender.getUserName();
            } else {
                userName = String.valueOf(sender.getPhoneNumber());
            }
            holder.infoText.setText(status + " " + sendDate + userName);
        }
    }

    private String getSendDate(Message message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return simpleDateFormat.format(new Date(message.getSendDate()));
    }

    private String getStatus(Message message) {
        ReceiverInfo receiverInfo = message.getReceiverInfos().first();

        if (receiverInfo.getViewed() > 0) {
            return "Viewed";
        } else if (receiverInfo.getDelivered() > 0) {
            return "Delivered";
        }

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
        } else if (state == WAITING_TO_DOWNLOAD.code) {
            return "Waiting to Download";
        } else if (state == DOWNLOADING.code) {
            return "Downloading";
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

    public interface OnMessageClickListener {
        void onMessageClicked(Message message);
    }

    public void setOnMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }
}
