package com.example.madel.seng403;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private fields
    private TimePicker pickerTime;
    private  Button buttonSetAlarm;
    public static ArrayList<AlarmDBItem> alarmList = new ArrayList<AlarmDBItem>();
    private ListView listView;
    private AlarmAdapter adapter;

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
        loadFile(this.getContext());
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
        this.adapter = new AlarmAdapter(this.getContext(), alarmList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // FAB for alarm list
        // creates new instance of TimePickerFragment to choose the alarm time
        FloatingActionButton alarmListFab = (FloatingActionButton) view.findViewById(R.id.alarm_list_fab);
        alarmListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerDialogFragment = new TimePickerFragment();

                timePickerDialogFragment.show(getFragmentManager(), "TimePicker");
                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton listbutton = (FloatingActionButton) view.findViewById(R.id.listbutton);
        listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                System.out.println("hello");
                checkListFunction();
                adapter = new AlarmAdapter(getContext(), alarmList);
                listView.setAdapter(adapter);
            }
        });
        // TODO move this AlarmList implementation to onTimeSet method in TimePickerFragment
//
//
//        // set on click listener method for set button
//        buttonSetAlarm.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//                // the reason for using two calenders for future extension
//                // for checking is current time was selected , for example not a time from past
//                Calendar calCurr = Calendar.getInstance();
//
//                String time = calCurr.getTime().toString();
//                Log.e("Log meesage: ", time);
//                Calendar calTarget = (Calendar) calCurr.clone();
//
//                // getting the time from timepicker and setting it to the caldendar
//                calTarget.set(Calendar.HOUR_OF_DAY, pickerTime.getHour());
//                calTarget.set(Calendar.MINUTE, pickerTime.getMinute());
//                calTarget.set(Calendar.SECOND, 0);
//                calTarget.set(Calendar.MILLISECOND, 0);
//
//
//                String time1 = calTarget.getTime().toString();
//
//                Log.e("Log message: ", "set time " + time1);
//
//
//                setAlarm(calTarget);
//                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Alarm Set!", Toast.LENGTH_LONG);
//                toast.show();
//
//            }});
    }

    public void checkListFunction()
    {
        loadFile(this.getContext());
        for(int i=0; i<alarmList.size(); i++)
        {
            System.out.println(alarmList.get(i).getHour()+":"+alarmList.get(i).getMinute());
        }
        adapter.notifyDataSetChanged();
    }
    /*
        private void setAlarm(Calendar targetCal){

            // creating an intent associated with AlarmReceiver class
            Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);

            final int alarmID = (int) System.currentTimeMillis();
            alarmList.add(new AlarmDBItem(targetCal, alarmID));

            // creating  a pending intent that delays the intent until the specified calender time is reached
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // setting the alarm Manager to set alarm at exact time of the user chosen time
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        }
    */
    // TODO: Rename method, update argument and hook method into UI event
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

    public static String fileName = "alarmSaveFile.ser";

    public void loadFile(Context context){
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.alarmList = (ArrayList<AlarmDBItem>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This in'terface must be implemented by activities that contain this
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