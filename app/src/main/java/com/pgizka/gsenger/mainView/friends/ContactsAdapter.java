package com.pgizka.gsenger.mainView.friends;

import android.content.Context;
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
import com.pgizka.gsenger.provider.User;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    private List<User> users;

    private OnContactClickListener onContactClickListener;
    private Fragment fragment;

    public ContactsAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

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
        final User user = users.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onContactClickListener != null) {
                    onContactClickListener.onContactClicked(position, user);
                }
            }
        });

        holder.userNameText.setText(user.getUserName());
        holder.statusText.setText(user.getStatus());

        String userPhotoHash = user.getPhotoHash();
        if (userPhotoHash != null) {
            Glide.with(fragment)
                    .load(ApiModule.buildUserPhotoPath(user))
                    .signature(new StringSignature(userPhotoHash))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.contactImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(users != null) {
            return users.size();
        } else {
            return 0;
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setOnContactClickListener(OnContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public interface  OnContactClickListener {
        void onContactClicked(int position, User user);
    }

}
