package com.pgizka.gsenger.addUsersToChatView;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.mainView.friends.ContactsAdapter;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.SelectionManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AddUsersToChatFragment extends Fragment implements AddUsersToChatContract.View {

    @Inject
    AddUsersToChatContract.Presenter presenter;

    @BindView(R.id.empty_text_view) TextView emptyTextView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ContactsAdapter contactsAdapter;
    private SelectionManager<User> selectionManager;

    private ProgressDialog progressDialog;

    public AddUsersToChatFragment() {
        setHasOptionsMenu(true);
        GSengerApplication.getApplicationComponent().inject(this);
        contactsAdapter = new ContactsAdapter(this);
        contactsAdapter.setMultiselectionMode(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_list, container, false);
        ButterKnife.bind(this, view);

        emptyTextView.setText("You do not have any users to add");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contactsAdapter);

        int chatId = getArguments().getInt(AddUsersToChatActivity.CHAT_ID_ARGUMENT);
        presenter.onCreate(this, chatId);

        setToolbarTitle(chatId);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_users_to_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_users_to_chat_action) {
            presenter.addUsers(selectionManager.getSelectedItems());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setToolbarTitle(int chatId) {
        Realm realm = Realm.getDefaultInstance();
        Chat chat = realm.where(Chat.class).equalTo("id", chatId).findFirst();
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle("Add to " + chat.getChatName());
    }

    @Override
    public void displayUsersList(List<User> users) {
        selectionManager = new SelectionManager<>(users);
        contactsAdapter.setUsers(users);
        contactsAdapter.setSelectionManager(selectionManager);
        contactsAdapter.notifyDataSetChanged();

        int visibility = users.isEmpty() ? View.VISIBLE : View.GONE;
        emptyTextView.setVisibility(visibility);
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Adding users to chat");
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void closeWindow() {
        getActivity().finish();
    }
}
