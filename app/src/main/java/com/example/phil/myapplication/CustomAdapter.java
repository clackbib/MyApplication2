package com.example.phil.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter
{
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public CustomAdapter(ArrayList<String> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    public void refresh(ArrayList<String> items)
    {
        this.list = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return -1;

        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.message_entry, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton messageButton = (ImageButton) view.findViewById(R.id.message_button);

        messageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final LayoutInflater li = LayoutInflater.from(context);
                View customMessageView = li.inflate(R.layout.custom_message_entry_prompt, null);
                final EditText userInput = (EditText) customMessageView.findViewById(R.id.addMessageEditText);
                final TextView displayMessage = (TextView) customMessageView.findViewById(R.id.addMessageTextView);

                String message = MainActiv.getBlacklistMessage(listItemText.getText());

                if(message != null)
                {
                    displayMessage.setText(message);
                }
                else
                {
                    displayMessage.setText("No custom message set!");
                }

                //Initialize AlertDialogBuilder and pass custom_number_entry_prompt view reference to it
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(customMessageView);

                //Set alert buttons:
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        String message = userInput.getText().toString();
                        String previousMessage = MainActiv.getBlacklistMessage(listItemText.getText());

                        if(message.equals(""))
                        {
                            Toast.makeText(context, "No message entered!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(!message.equals(previousMessage))
                            {
                                MainActiv.getBlacklist().put(listItemText.getText().toString(), message);
                                Toast.makeText(context, "Message updated!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context, "Desired message already set!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
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

        return view;
    }
}
