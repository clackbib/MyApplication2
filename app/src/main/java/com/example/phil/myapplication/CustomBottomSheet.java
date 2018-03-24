package com.example.phil.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomBottomSheet extends BottomSheetDialogFragment
{
    Button recent_button, contacts_button, custom_button;
    String inputString;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.number_source_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        recent_button = (Button)view.findViewById(R.id.recent_button);
        contacts_button = (Button) view.findViewById(R.id.contacts_button);
        custom_button = (Button) view.findViewById(R.id.custom_button);

        recent_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Uri allCalls = Uri.parse("content://call_log/calls");

                Cursor cursor = getActivity().getContentResolver().query(allCalls, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

                ArrayList<String> callList = new ArrayList<>();

                int i = 0;

                cursor.moveToFirst();

                while(!cursor.isLast() || i < 10)
                {
                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.MISSED_TYPE
                            || Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.BLOCKED_TYPE
                            || Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.REJECTED_TYPE
                            || Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.INCOMING_TYPE)
                    {
                        callList.add(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    }
                    cursor.moveToNext();
                    i++;
                }
                cursor.close();

                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.lists, null);
                alertDialog.setView(convertView);
                final ListView lv = (ListView) convertView.findViewById(R.id.recentCallsListView);
                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,callList);
                lv.setAdapter(adapter2);
                alertDialog.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        int pos = lv.getCheckedItemPosition();

                        inputString = lv.getAdapter().getItem(pos).toString();
                        MainActiv.blacklistStringList.add(inputString);
                        ((MainActiv)getActivity()).blacklistAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        contacts_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        custom_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.custom_number_entry_prompt, null);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.addNumberEditText);

                //Initialize AlertDialogBuilder and pass custom_number_entry_prompt view reference to it
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptsView);

                //Set alert buttons:
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        inputString = userInput.getText().toString();

                        if(!MainActiv.getBlacklistStringList().contains(inputString))
                        {
                            MainActiv.addToArray(inputString);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Error. Blacklist already contains the specified number.", Toast.LENGTH_SHORT).show();
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

            }
        });
    }

    public Button getRecent_button()
    {
        return recent_button;
    }

    public Button getContacts_button()
    {
        return contacts_button;
    }

    public Button getCustom_button()
    {
        return custom_button;
    }

}
