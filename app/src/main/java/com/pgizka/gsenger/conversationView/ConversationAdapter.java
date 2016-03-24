package com.pgizka.gsenger.conversationView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.provider.Message;

import java.util.List;

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


        /*holder.progressBar.setVisibility(View.GONE);
        holder.fileNameText.setVisibility(View.GONE);
        holder.filePathText.setVisibility(View.GONE);
        holder.image.setVisibility(View.GONE);
        holder.videoImage.setVisibility(View.GONE);*/



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
        if (message.isOutgoing()) {
            return VIEW_TYPE_LEFT;
        } else {
            return VIEW_TYPE_RIGHT;
        }
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
