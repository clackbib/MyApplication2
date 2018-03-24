package com.example.phil.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>
{
    private LayoutInflater inflater;
    private List<RecyclerItem> data = Collections.emptyList();

    public RecyclerAdapter(Context context, List<RecyclerItem> data)
    {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position)
    {
        RecyclerItem current = data.get(position);
        holder.title.setText(current.title);
        holder.descriptor.setText(current.descriptor);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView descriptor;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.entryTitle);
            descriptor = itemView.findViewById(R.id.entryDescriptor);
        }
    }
}
