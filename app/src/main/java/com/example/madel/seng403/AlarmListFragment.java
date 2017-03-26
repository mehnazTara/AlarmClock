package com.example.madel.seng403;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AlarmListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int idforbuttonalarm;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private fields
    private TimePicker pickerTime;
    private static ListView listView;
    private AlarmAdapter adapter;
    private static Context context;

    private OnFragmentInteractionListener mListener;

    public AlarmListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmListFragment newInstance(String param1, String param2) {
        AlarmListFragment fragment = new AlarmListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);
        listView = (ListView) view.findViewById(R.id.alarm_list_view);
        this.adapter = new AlarmAdapter(this.getContext(), MainActivity.getList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //adapteriew instead of parent
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int item = adapter.getCount();
                Intent intent = new Intent(getActivity(), EditAlarm.class);
                startActivity(intent);
            }
        });
        context = this.getContext();
        return view;
    }

    //sets up the list view when the view is created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // FAB for alarm list
        // creates new instance of TimePickerFragment to choose the alarm time

        FloatingActionButton alarmListFab = (FloatingActionButton) view.findViewById(R.id.alarm_list_fab);
        alarmListFab.setOnClickListener(new View.OnClickListener() {

//setting onclick listener for FAB
            @Override
            public void onClick(View view) {
                int alarmId= -1;

                Intent intentLoadNewActivity = new Intent(context, EditAlarm.class); // change activity
                intentLoadNewActivity.putExtra("AlarmId", alarmId);


                context.startActivity(intentLoadNewActivity);

            }



      /*  alarmListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerDialogFragment = new TimePickerFragment();
                timePickerDialogFragment.show(getFragmentManager(), "TimePicker");
            }*/
        });

    }

    //confirms the correct alarms are loaded
    public void checkListFunction()
    {
        for(int i=0; i<MainActivity.getList().size(); i++)
        {
            System.out.println(MainActivity.getList().get(i).getHour() + ":" + MainActivity.getList().get(i).getMinute());
        }
    }

    // functionality for the button.
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void updateListView()
    {
        listView.setAdapter(new AlarmAdapter(context, MainActivity.getList()));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}