package com.example.phil.myapplication.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.example.phil.myapplication.ui.adapter.BlackListEntryAdapter;
import com.example.phil.myapplication.ui.dialog.AddBlacklistEntryDialogFragment;
import com.example.phil.myapplication.R;
import com.example.phil.myapplication.repository.BlackListEntry;
import com.example.phil.myapplication.repository.BlacklistRepository;

import java.util.List;

//If calling from fragment, use Childmanager, otherwise regular fragment manager.

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class BlackListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private RecyclerView recyclerView;

    ImageButton addEntryBtn;

    BlacklistRepository.BlackListChangeListener listChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blacklist, container, false);
        recyclerView = view.findViewById(R.id.blacklistRecyclerView);

        //view = inflater.inflate(R.layout.fragment_blacklist, container, false);
        addEntryBtn = view.findViewById(R.id.addButton);
        addEntryBtn.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listChangeListener = new BlacklistRepository.BlackListChangeListener() {
            @Override
            public void onListReceived(List<BlackListEntry> blackListEntries) {
                Log.d("PHIL", "Hey! Got a Blacklist List!!");
                recyclerView.setAdapter(new BlackListEntryAdapter(blackListEntries));
            }
        };
        BlacklistRepository.getInstance().addBlackListChangeObserver(listChangeListener);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BlacklistRepository.getInstance().removeBlackListChangeObserver(listChangeListener);
    }

    @Override
    public void onClick(View view) {
        AddBlacklistEntryDialogFragment sheet = new AddBlacklistEntryDialogFragment();
        sheet.show(getChildFragmentManager(), "onClick");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        // Get reference to confirmations view
        final LayoutInflater li = LayoutInflater.from(getActivity());
        View confirmationsView = li.inflate(R.layout.confirmations, null);

        //Initialize AlertDialogBuilder and pass confirmations view reference to it
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(confirmationsView);

        //Set alert buttons:
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BlacklistRepository.getInstance().removeBlackListEntry(i);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        // Create and show AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return true;
    }


}
