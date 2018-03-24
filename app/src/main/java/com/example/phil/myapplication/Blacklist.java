package com.example.phil.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.phil.myapplication.R.layout.confirmations;
import static com.example.phil.myapplication.R.layout.number_source_selection;

//If calling from fragment, use Childmanager, otherwise regular fragment manager.

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Blacklist.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Blacklist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Blacklist extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView blacklist;

    static RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    String inputString = "";
    ImageButton addbuttonbro;
    View view;

    private OnFragmentInteractionListener mListener;

    public Blacklist() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Blacklist newInstance(String param1, String param2)
    {
        Blacklist fragment = new Blacklist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        System.out.println("Made it here at least.");
        recyclerAdapter = new RecyclerAdapter(getActivity(), getData());
        view = inflater.inflate(R.layout.fragment_blacklist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(recyclerAdapter);

        //view = inflater.inflate(R.layout.fragment_blacklist, container, false);
        addbuttonbro = (ImageButton) view.findViewById(R.id.addButton);
        addbuttonbro.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //blacklist.setLongClickable(true);

        //ArrayAdapter<String> adapter = ((MainActiv)getActivity()).getBlacklistAdapter();

        //blacklist.setAdapter(adapter);


        /*blacklist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                // Get reference to confirmations view
                final LayoutInflater li = LayoutInflater.from(getActivity());
                View confirmationsView = li.inflate(R.layout.confirmations, null);

                //Initialize AlertDialogBuilder and pass confirmations view reference to it
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(confirmationsView);

                //Set alert buttons:
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        ((MainActiv)getActivity()).removeFromArray(blacklist.getItemAtPosition(i));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        dialog.cancel();
                    }
                });

                // Create and show AlertDialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });*/


        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view)
    {
        CustomBottomSheet sheet = new CustomBottomSheet();
        sheet.show(getChildFragmentManager(), "onClick");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l)
    {
        // Get reference to confirmations view
        final LayoutInflater li = LayoutInflater.from(getActivity());
        View confirmationsView = li.inflate(R.layout.confirmations, null);

        //Initialize AlertDialogBuilder and pass confirmations view reference to it
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(confirmationsView);

        //Set alert buttons:
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                ((MainActiv)getActivity()).removeFromArray(blacklist.getItemAtPosition(i));
                //((MainActiv)getActivity()).getBlacklistStringList().remove(blacklist.getItemAtPosition(i));
                //((MainActiv)getActivity()).getBlacklistAdapter().notifyDataSetChanged();
                //((MainActiv)getActivity()).getMessageAdapter().notifyDataSetChanged();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                dialog.cancel();
            }
        });

        // Create and show AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return true;
    }

    public List<RecyclerItem> getData()
    {
        List<RecyclerItem> data = new ArrayList<>();
        ArrayList<String> titles = MainActiv.getBlacklistStringList();
        for(int i = 0; i< titles.size(); i++)
        {
            RecyclerItem item = new RecyclerItem();
            item.title = titles.get(i);
            item.descriptor = titles.get(i);
            data.add(item);
            System.out.println("Blacklist title is: " + item.title);
            System.out.println("Blacklist size: " + titles.size());
        }

        return data;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
