package com.pgizka.gsenger.createChatsView;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.mainView.friends.ContactsAdapter;
import com.pgizka.gsenger.provider.User;
import com.pgizka.gsenger.util.SelectionManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateChatFragment extends Fragment implements CreateChatContract.View {

    @Inject
    CreateChatContract.Presenter presenter;

    @Bind(R.id.create_chat_chat_name_edit_text) EditText chatNameEditText;
    @Bind(R.id.create_chat_recycler) RecyclerView recyclerView;

    private ContactsAdapter contactsAdapter;
    private SelectionManager<User> selectionManager;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);
        contactsAdapter = new ContactsAdapter(this);
        presenter.onCreate(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_chat, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contactsAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.create_chat_action) {
            presenter.createChat(chatNameEditText.getText().toString(), selectionManager.getSelectedItems());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayUsersList(List<User> users) {
        selectionManager = new SelectionManager<>(users);
        contactsAdapter.setUsers(users);
        contactsAdapter.setSelectionManager(selectionManager);
        contactsAdapter.setMultiselectionMode(true);
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Creating Chat");
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
