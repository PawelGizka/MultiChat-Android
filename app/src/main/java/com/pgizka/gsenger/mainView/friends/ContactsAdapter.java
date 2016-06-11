package com.pgizka.gsenger.mainView.friends;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.pgizka.gsenger.R;
import com.pgizka.gsenger.api.ApiModule;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.SelectionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    private List<User> users;

    private OnContactClickListener onContactClickListener;
    private Fragment fragment;

    private SelectionManager<User> selectionManager;
    private boolean multiselectionMode;

    public ContactsAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        @BindView(R.id.contact_item_main_image) ImageView contactImage;
        @BindView(R.id.contact_item_user_name_text) TextView userNameText;
        @BindView(R.id.contact_item_status_text) TextView statusText;
        @BindView(R.id.contact_item_checkbox) CheckBox checkBox;
        @BindView(R.id.contact_item_facebook_label) ImageView facebookLabelImage;
        @BindView(R.id.contact_item_phone_label) TextView phoneLabelText;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
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
        holder.checkBox.setVisibility(View.GONE);

        final User user = users.get(position);
        holder.view.setOnClickListener(v -> {
            if(onContactClickListener != null) {
                onContactClickListener.onContactClicked(position, user);
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
                    .error(R.drawable.defult_contact_image)
                    .into(holder.contactImage);
        }

        if (multiselectionMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(selectionManager.isItemSelected(position));

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    selectionManager.setItemSelected(position, isChecked));
        }

        if (user.isFromFacebook()) {
            holder.facebookLabelImage.setVisibility(View.VISIBLE);
        } else {
            holder.facebookLabelImage.setVisibility(View.GONE);
        }

        if (user.isFromPhoneNumbers()) {
            holder.phoneLabelText.setVisibility(View.VISIBLE);
        } else {
            holder.phoneLabelText.setVisibility(View.GONE);
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

    public void setSelectionManager(SelectionManager<User> selectionManager) {
        this.selectionManager = selectionManager;
    }

    public void setMultiselectionMode(boolean multiselectionMode) {
        this.multiselectionMode = multiselectionMode;
        notifyDataSetChanged();
    }

    public void setOnContactClickListener(OnContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public interface  OnContactClickListener {
        void onContactClicked(int position, User user);
    }

}
