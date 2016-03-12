package com.pgizka.gsenger.mainView.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgizka.gsenger.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    private List<Contact> contacts;

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
        final Contact contact = contacts.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onContactClickListener != null) {
                    onContactClickListener.onContactClicked(0, position, contact);
                }
            }
        });

        holder.userNameText.setText(contact.getUserName());
        holder.statusText.setText(contact.getStatus());

    }

    @Override
    public int getItemCount() {
        if(contacts != null) {
            return contacts.size();
        } else {
            return 0;
        }
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setOnContactClickListener(OnContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public static interface  OnContactClickListener {
        void onContactClicked(int contactId, int position, Contact contact);
    }

}
