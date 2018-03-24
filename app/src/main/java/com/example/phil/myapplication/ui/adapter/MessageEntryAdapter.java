package com.example.phil.myapplication.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.myapplication.R;
import com.example.phil.myapplication.repository.BlackListEntry;
import com.example.phil.myapplication.repository.BlacklistRepository;
import com.example.phil.myapplication.ui.MainActivity;

import java.util.List;

public class MessageEntryAdapter extends RecyclerView.Adapter<MessageEntryAdapter.MessageViewHolder> {
    private List<BlackListEntry> list;

    public MessageEntryAdapter(List<BlackListEntry> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_entry, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        final BlackListEntry entry = list.get(position);
        holder.title.setText(entry.contactName);
        holder.descriptor.setText(entry.phoneNumber);
        holder.editMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = holder.itemView.getContext();
                final LayoutInflater li = LayoutInflater.from(context);
                View customMessageView = li.inflate(R.layout.custom_message_entry_prompt, null);
                final EditText userInput = customMessageView.findViewById(R.id.addMessageEditText);
                final TextView displayMessage = customMessageView.findViewById(R.id.addMessageTextView);

                final String message = entry.replyMessage;

                if (message != null) {
                    displayMessage.setText(message);
                } else {
                    displayMessage.setText("No custom message set!");
                }

                //Initialize AlertDialogBuilder and pass custom_number_entry_prompt view reference to it
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(customMessageView);

                //Set alert buttons:
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String inputMessage = userInput.getText().toString();

                        if (inputMessage.equals("")) {
                            Toast.makeText(context, "No message entered!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!inputMessage.equals(message)) {
                                //Talk to repo
                                BlacklistRepository.getInstance().changeBlackListEntryMessage(holder.getAdapterPosition(),inputMessage);
                                Toast.makeText(context, "Message updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Desired message already set!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                //Create AlertDialog, set keyboard to show on launch, and show AlertDialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alertDialog.show();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView descriptor;
        ImageButton editMessage;

        MessageViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.messageEntryTitle);
            descriptor = itemView.findViewById(R.id.messageEntryDescriptor);
            editMessage = itemView.findViewById(R.id.message_button);
        }
    }
}
