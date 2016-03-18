package com.pgizka.gsenger.conversationView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pgizka.gsenger.R;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private List<ConversationItem> conversationItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View view) {
            super(view);
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
        final ConversationItem conversationItem = conversationItems.get(position);


    }


    @Override
    public int getItemCount() {
        if(conversationItems != null) {
            return conversationItems.size();
        } else {
            return 0;
        }
    }

    public void setConversationItems(List<ConversationItem> conversationItems) {
        this.conversationItems = conversationItems;
    }


}
