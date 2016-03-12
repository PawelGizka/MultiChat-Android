package com.pgizka.gsenger.mainView.contacts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.mainView.chats.ChatsPresenter;
import com.pgizka.gsenger.mainView.chats.ChatsView;

public class ContactsFragment extends Fragment implements ContactsView<ContactsModel>{

    private ContactsPresenter presenter;

    private RecyclerView recyclerView;
    private TextView emptyTextView;

    private ContactsAdapter contactsAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsAdapter = new ContactsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.contacts_recycler_view);
        emptyTextView = (TextView) view.findViewById(R.id.contacts_empty_text_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contactsAdapter);

        contactsAdapter.setOnContactClickListener(new ContactsAdapter.OnContactClickListener() {
            @Override
            public void onContactClicked(int contactId, int position, Contact contact) {
                presenter.contactClicked(contactId, position, contact);
            }
        });

        return view;
    }

    @Override
    public AppCompatActivity getHoldingActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void setPresenter(ContactsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayContactsList(ContactsModel model) {
        boolean noContacts = model.getContacts().size() == 0;
        if(noContacts) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            contactsAdapter.setContacts(model.getContacts());
            contactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void displayErrorMessage(AlertDialog alertDialog) {
        alertDialog.show();
    }
}
