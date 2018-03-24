package com.example.phil.myapplication.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.phil.myapplication.R;
import com.example.phil.myapplication.repository.BlackListEntry;
import com.example.phil.myapplication.repository.BlacklistRepository;
import com.example.phil.myapplication.ui.adapter.BlackListEntryAdapter;
import com.example.phil.myapplication.ui.adapter.MessageEntryAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * <p>
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    BlacklistRepository.BlackListChangeListener listChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        final RecyclerView messageRv = view.findViewById(R.id.messageRecyclerView);

        messageRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        listChangeListener = new BlacklistRepository.BlackListChangeListener() {
            @Override
            public void onListReceived(List<BlackListEntry> blackListEntries) {
                Log.d("PHIL", "Hey! Got a new Message List!!");
                messageRv.setAdapter(new MessageEntryAdapter(blackListEntries));
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
}

