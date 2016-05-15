package com.pgizka.gsenger.mainView.friends;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.config.GSengerApplication;
import com.pgizka.gsenger.provider.User;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsFragment extends Fragment implements ContactsContract.View {

    @Inject
    ContactsContract.Presenter presenter;

    @Bind(R.id.contacts_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.contacts_empty_text_view) TextView emptyTextView;
    @Bind(R.id.contacts_swipe_to_refresh) SwipeRefreshLayout swipeRefreshLayout;

    private ContactsAdapter contactsAdapter;


    public ContactsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);
        contactsAdapter = new ContactsAdapter(this);
        presenter.onCreate(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contactsAdapter);

        contactsAdapter.setOnContactClickListener((position, user) -> presenter.friendClicked(position, user));
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshFriends());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public AppCompatActivity getHoldingActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void displayContactsList(List<User> users) {
        boolean noContacts = users.size() == 0;
        if(noContacts) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            contactsAdapter.setUsers(users);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void displayErrorMessage(AlertDialog alertDialog) {
        alertDialog.show();
    }

    @Override
    public void dismissRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
