package com.pgizka.gsenger.mainView.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.provider.realm.Friend;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{

    private List<Friend> friends;

    private OnContactClickListener onContactClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView contactImageView;
        public TextView userNameText;
        public TextView statusText;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.contactImageView = (ImageView) view.findViewById(R.id.contact_item_main_image);
            userNameText = (TextView) view.findViewById(R.id.contact_item_user_name_text);
            statusText = (TextView) view.findViewById(R.id.contact_item_status_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Friend friend = friends.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onContactClickListener != null) {
                    onContactClickListener.onContactClicked(position, friend);
                }
            }
        });

        holder.userNameText.setText(friend.getUserName());
        holder.statusText.setText(friend.getStatus());

    }

    @Override
    public int getItemCount() {
        if(friends != null) {
            return friends.size();
        } else {
            return 0;
        }
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public void setOnContactClickListener(OnContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public static interface  OnContactClickListener {
        void onContactClicked(int position, Friend friend);
    }

}
