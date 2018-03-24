package com.example.phil.myapplication.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phil.myapplication.R;
import com.example.phil.myapplication.repository.BlackListEntry;

import java.util.List;

public class BlackListEntryAdapter extends RecyclerView.Adapter<BlackListEntryAdapter.BlackListViewHolder> {
    private List<BlackListEntry> data;

    public BlackListEntryAdapter(List<BlackListEntry> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BlackListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new BlackListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlackListViewHolder holder, int position) {
        BlackListEntry current = data.get(position);
        holder.title.setText(current.contactName);
        holder.descriptor.setText(current.phoneNumber);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BlackListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView descriptor;

        BlackListViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.entryTitle);
            descriptor = itemView.findViewById(R.id.entryDescriptor);
        }
    }
}
