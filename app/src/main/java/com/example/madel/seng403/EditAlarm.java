package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author  Ore and Mehnaz
 * This class is an Activity that is started everytime new alarm buttom is clicked or edit button is clicked
 * It creates the alarm object and sets the alarm manager at user chosen time
 *
 */
public class EditAlarm extends AppCompatActivity {

    private AlarmManager alarm_manager;
    private TimePicker alarm_timepicker;
    private EditText edit;
    private String label = null;
    private String previousLabel = null;

    private Context context;
    private int index;
    private Calendar calendar;

    private Boolean initalCheck;
    private Boolean checkboxFlag = false;

    final CheckBox sn_checkBox = (CheckBox) findViewById(R.id.verify_SN);
    final CheckBox m_checkBox = (CheckBox) findViewById(R.id.verify_M);
    final CheckBox t_checkBox = (CheckBox) findViewById(R.id.verify_T);
    final CheckBox w_checkBox = (CheckBox) findViewById(R.id.verify_W);
    final CheckBox tr_checkBox = (CheckBox) findViewById(R.id.verify_TR);
    final CheckBox f_checkBox = (CheckBox) findViewById(R.id.verify_F);
    final CheckBox s_checkBox = (CheckBox) findViewById(R.id.verify_S);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState== null){
            Bundle extras= getIntent().getExtras();//getting alarm id that we pass in
            if(extras!=null){
                index=extras.getInt("AlarmId");
                previousLabel = extras.getString("AlarmLabel");
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initializing all the field variables
        this.context=this;
        alarm_manager= (AlarmManager) getSystemService((ALARM_SERVICE));
        alarm_timepicker=(TimePicker) findViewById(R.id.timePicker);
        edit=(EditText) findViewById(R.id.editText2) ;
        edit.setText(previousLabel);

        //this block handles the action related to clicking of save button
        Button save = (Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // getting the label
                    label = edit.getText().toString();

                    // getting the hour and minute from timePicker
                    int hour = alarm_timepicker.getHour();
                    int minute = alarm_timepicker.getMinute();
                    Log.e("Log message", "time picker time " + hour + minute);

                    calendar = Calendar.getInstance();

                    // setting the user input time in a calendar object
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Calendar calCurr = Calendar.getInstance();
                    if (repeatWeekCheck(calendar, false, hour, minute)){
                        repeatWeekCheck(calendar, true, hour, minute);
                    }
                    else {
                        // if time  chosen is before current time then increment by 24 hours
                        if (calendar.getTime().before(calCurr.getTime())) {
                            calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour() + 24);
                        }

                        // index == -1 means this activity was started from new alarm button
                        if (index == -1) {
                            setAlarm(calendar);

                        }
                        // else its an edit action
                        else {
                            editAlarm(hour, minute);
                        }
                    }
                // goes back to previous fragment
               onBackPressed();
            }
        });
    }


    /**
     * sets an alarm and makes it active
     * adds alarm to the alarmlist and saves the list
     * @param targetCal
     */
    private void setAlarm(Calendar targetCal){

        // creating an intent associated with AlarmReceiver class
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        final int alarmID = (int) System.currentTimeMillis(); // assigns a unique alarmID
        Log.e("Log message: ", "alarm created with id: " + alarmID);

        // passing the alarm Id to AlarmReiver
        alarmIntent.putExtra("AlarmId", alarmID);
        Log.e("Log/MESSAGE:Label", "label " + label);

        // adding it to the global list
        MainActivity.getList().add(new AlarmDBItem(targetCal, alarmID,true ,label));
        Log.e("Log message: ", "the alarm list id is: " + MainActivity.getList().get(MainActivity.getList().size()-1).getID());

        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarm_manager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        AlarmListFragment.updateListView();

        MainActivity.saveFile(context);
    }


    /**
     * edits the hour and minute field members of the alarmDBitem
     * and updates the pendingIntent to be active at the changed time
     * @param hour from timepicker
     * @param minute from time picker
     */
    private void editAlarm(int hour, int minute){
        ArrayList<AlarmDBItem> list = MainActivity.getList();

        // find the AlarmDBItem with the same id and sets the hour and minute as the input
        for (int i = 0; i < MainActivity.getList().size(); i++) {
            if (list.get(i).getID() == index) {
                list.get(i).setHour(hour);
                list.get(i).setMinute(minute);
                list.get(i).setLabel(label);

            }
        }

        // for testing purpose
        for (AlarmDBItem a :MainActivity.getList()){
            if (a.getID() == index){
                Log.e("Log message", "time" + a.getHourString() + a.getMinuteString());
                break;
            }
        }

        // saving the updated file
        MainActivity.saveFile(context);


        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        // creating an intent associated with AlarmReceiver class
        Log.e("Log message: ", "alarm created with id: " + index);

        Log.e("Log message: ", "the alarm list id is: " + MainActivity.getList().get(MainActivity.getList().size() - 1).getID());
        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, index, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        AlarmListFragment.updateListView();

    }

    private Boolean repeatWeekCheck(Calendar targetCal, Boolean secondIt, int hour, int minute) {
        //Checking which days the user requested
        while (initalCheck) {
            if (m_checkBox.isChecked()) {
                checkboxFlag = true;
                if (secondIt) {
                    repeatWeek(2, targetCal, hour, minute);
                }
            }
            if (t_checkBox.isChecked()) {
                checkboxFlag = true;
                if (secondIt) {
                    repeatWeek(3, targetCal, hour, minute);
                }
            }
            if (w_checkBox.isChecked()) {
                checkboxFlag = true;
                if (secondIt) {
                    repeatWeek(4, targetCal, hour, minute);
                }
            }
            if (tr_checkBox.isChecked()) {
                checkboxFlag = true;
                if (secondIt) {
                    repeatWeek(5, targetCal, hour, minute);
                }
            }
            if (f_checkBox.isChecked()) {
                checkboxFlag = true;
                if (secondIt) {
                    repeatWeek(6, targetCal, hour, minute);
                }
            }
            if (s_checkBox.isChecked()) {
                checkboxFlag = true;
                if (secondIt) {
                    repeatWeek(7, targetCal, hour, minute);
                }
            }
            if (sn_checkBox.isChecked()) {
                checkboxFlag = true;
                if (secondIt) {
                    repeatWeek(1, targetCal, hour, minute);
                }
            }
            initalCheck = false;
        }
        return checkboxFlag;
    }

    private void repeatWeek (int week, Calendar targetCal, int hour, int minute) {
        targetCal.set(Calendar.DAY_OF_WEEK, week);
        //If new alarm
        if (index == -1) {
            // creating an intent associated with AlarmReceiver class
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            final int alarmID = (int) System.currentTimeMillis(); // assigns a unique alarmID
            Log.e("Log message: ", "alarm created with id: " + alarmID);

            // passing the alarm Id to AlarmReiver
            alarmIntent.putExtra("AlarmId", alarmID);
            Log.e("Log/MESSAGE:Label", "label " + label);

            // adding it to the global list
            MainActivity.getList().add(new AlarmDBItem(targetCal, alarmID,true ,label));
            Log.e("Log message: ", "the alarm list id is: " + MainActivity.getList().get(MainActivity.getList().size()-1).getID());

            // creating  a pending intent that delays the intent until the specified calender time is reached
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // setting the alarm Manager to set alarm at exact time of the user chosen time
            alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), 24 * 7 * 60 * 60 * 1000, pendingIntent);
            AlarmListFragment.updateListView();

            MainActivity.saveFile(context);
        }
        //Edit Alarm
        else {
            ArrayList<AlarmDBItem> list = MainActivity.getList();

            // find the AlarmDBItem with the same id and sets the hour and minute as the input
            for (int i = 0; i < MainActivity.getList().size(); i++) {
                if (list.get(i).getID() == index) {
                    list.get(i).setHour(hour);
                    list.get(i).setMinute(minute);
                    list.get(i).setLabel(label);
                }
            }

            // for testing purpose
            for (AlarmDBItem a :MainActivity.getList()){
                if (a.getID() == index){
                    Log.e("Log message", "time" + a.getHourString() + a.getMinuteString());
                    break;
                }
            }

            // saving the updated file
            MainActivity.saveFile(context);

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);

            // creating an intent associated with AlarmReceiver class
            Log.e("Log message: ", "alarm created with id: " + index);

            Log.e("Log message: ", "the alarm list id is: " + MainActivity.getList().get(MainActivity.getList().size() - 1).getID());
            // creating  a pending intent that delays the intent until the specified calender time is reached
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, index, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // setting the alarm Manager to set alarm at exact time of the user chosen time
            alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 7 * 60 * 60 * 1000 ,pendingIntent);
            AlarmListFragment.updateListView();
        }
    }
    /**
     * this method takes view back to the previoue fragment in the fragment stack
     */
    public void onBackPressed(){
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
        }





}


















