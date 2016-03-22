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
import com.pgizka.gsenger.provider.realm.Friend;

import java.util.List;

import javax.inject.Inject;

public class FriendsFragment extends Fragment implements FriendsContract.View {

    @Inject
    FriendsContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FriendsAdapter friendsAdapter;


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSengerApplication.getApplicationComponent().inject(this);
        friendsAdapter = new FriendsAdapter();
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
        recyclerView.setAdapter(friendsAdapter);

        friendsAdapter.setOnContactClickListener(new FriendsAdapter.OnContactClickListener() {
            @Override
            public void onContactClicked(int position, Friend friend) {
                presenter.friendClicked(position, friend);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshFriends();
            }
        });

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
    public void displayContactsList(List<Friend> friends) {
        boolean noContacts = friends.size() == 0;
        if(noContacts) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            friendsAdapter.setFriends(friends);
            friendsAdapter.notifyDataSetChanged();
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
