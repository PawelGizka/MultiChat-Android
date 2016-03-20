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
import com.pgizka.gsenger.provider.pojos.Friend;

public class FriendsFragment extends Fragment implements FriendsContract.View<FriendsModel> {

    private FriendsContract.Presenter presenter;

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
        friendsAdapter = new FriendsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        presenter = (FriendsContract.Presenter) getTargetFragment();

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
    public AppCompatActivity getHoldingActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void displayContactsList(FriendsModel model) {
        boolean noContacts = model.getFriends().size() == 0;
        if(noContacts) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            friendsAdapter.setFriends(model.getFriends());
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
