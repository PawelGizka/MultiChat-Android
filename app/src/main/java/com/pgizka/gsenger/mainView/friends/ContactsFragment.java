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
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.User;

import java.util.List;

import javax.inject.Inject;

public class ContactsFragment extends Fragment implements ContactsContract.View {

    @Inject
    ContactsContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ContactsAdapter contactsAdapter;


    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);
        contactsAdapter = new ContactsAdapter(getContext());
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

        recyclerView = (RecyclerView) view.findViewById(R.id.contacts_recycler_view);
        emptyTextView = (TextView) view.findViewById(R.id.contacts_empty_text_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contacts_swipe_to_refresh);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contactsAdapter);

        contactsAdapter.setOnContactClickListener(new ContactsAdapter.OnContactClickListener() {
            @Override
            public void onContactClicked(int position, User user) {
                presenter.friendClicked(position, user);
            }
        });

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
