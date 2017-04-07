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

    CheckBox Daily;
    CheckBox Sunday;
    CheckBox Monday;
    CheckBox Tuesday;
    CheckBox Wednesday;
    CheckBox Thursday;
    CheckBox Friday;
    CheckBox Saturday;

    private String rDaily = "";
    private String rSun = "";
    private String rMon = "";
    private String rTues = "";
    private String rWed = "";
    private String rThurs = "";
    private String rFri = "";
    private String rSat = "";

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
                int hour = alarm_timepicker.getCurrentHour();
                int minute = alarm_timepicker.getCurrentMinute();
                Log.e("Log message", "time picker time " + hour + minute);

               calendar= Calendar.getInstance();


                // setting the user input time in a calendar object
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                Calendar calCurr = Calendar.getInstance();

                // if time  chosen is before current time then increment by 24 hours
                if(calendar.getTime().before(calCurr.getTime()))
                {
                    calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getCurrentHour() + 24);
                }


                // index == -1 means this activity was started from new alarm button
                if(index==-1){
                   setAlarm(calendar);

                }
                // else its an edit action
                else {
                    editAlarm(hour,minute);

                }
                // goes back to previous fragment
               onBackPressed();



            }
        });

        this.Daily = (CheckBox) findViewById(R.id.repeatCheckBox);
        this.Sunday = (CheckBox) findViewById(R.id.verify_SN);
        this.Monday = (CheckBox) findViewById(R.id.verify_M);
        this.Tuesday = (CheckBox) findViewById(R.id.verify_T);
        this.Wednesday = (CheckBox) findViewById(R.id.verify_W);
        this.Thursday = (CheckBox) findViewById(R.id.verify_TR);
        this.Friday = (CheckBox) findViewById(R.id.verify_F);
        this.Saturday = (CheckBox) findViewById(R.id.verify_S);
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

        // find the AlarmDBItem with the same id for use in the weekly repeat function
        ArrayList<AlarmDBItem> list = MainActivity.getList();
        int weeklyAlarmDBItemID = -1;
        for (int i = 0; i < MainActivity.getList().size(); i++) {
            if (list.get(i).getID() == alarmID) {
                weeklyAlarmDBItemID = i;
            }
        }

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        if(Daily.isChecked()) {
            rDaily = "Daily";
            alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            list.get(weeklyAlarmDBItemID).setDailyRepeat(true);
            for(int k = 0; k < 7; k++)
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(k, false);

        } else if (Sunday.isChecked() || Monday.isChecked() || Tuesday.isChecked() || Wednesday.isChecked() || Thursday.isChecked() || Friday.isChecked() || Saturday.isChecked()) {
            Calendar tempCal = targetCal;
            list.get(weeklyAlarmDBItemID).setDailyRepeat(false);
            if (Sunday.isChecked()){
                rSun = "Sunday";
                while( tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                    tempCal.add(Calendar.DATE, 1);
                PendingIntent pendingIntentSunday = PendingIntent.getBroadcast(context, alarmID + 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentSunday);

                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(0, true);
                tempCal = targetCal;
            } else {
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(0, false);
            }
            if (Monday.isChecked()) {
                rMon = "Monday";
                while( tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
                    tempCal.add(Calendar.DATE, 1);
                PendingIntent pendingIntentMonday = PendingIntent.getBroadcast(context, alarmID + 2, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentMonday);

                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(1, true);
                tempCal = targetCal;
            } else {
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(1, false);
            }
            if (Tuesday.isChecked()) {
                rTues = "Tuesday";
                while( tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY)
                    tempCal.add(Calendar.DATE, 1);
                PendingIntent pendingIntentTuesday = PendingIntent.getBroadcast(context, alarmID + 3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentTuesday);

                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(2, true);
                tempCal = targetCal;
            } else {
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(2, false);
            }
            if (Wednesday.isChecked()) {
                rWed = "Wednesday";
                while( tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY)
                    tempCal.add(Calendar.DATE, 1);
                PendingIntent pendingIntentWednesday = PendingIntent.getBroadcast(context, alarmID + 4, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentWednesday);

                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(3, true);
                tempCal = targetCal;
            } else {
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(3, false);
            }
            if (Thursday.isChecked()) {
                rThurs = "Thursday";
                while( tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY)
                    tempCal.add(Calendar.DATE, 1);
                PendingIntent pendingIntentThursday = PendingIntent.getBroadcast(context, alarmID + 5, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentThursday);

                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(4, true);
                tempCal = targetCal;
            } else {
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(4, false);
            }

            if (Friday.isChecked()) {
                rFri = "Friday";
                while( tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY)
                    tempCal.add(Calendar.DATE, 1);
                PendingIntent pendingIntentFriday = PendingIntent.getBroadcast(context, alarmID + 6, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentFriday);

                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(5, true);
                tempCal = targetCal;
            } else {
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(5, false);
            }
            if (Saturday.isChecked()) {
                rSat = "Saturday";
                while( tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                    tempCal.add(Calendar.DATE, 1);
                PendingIntent pendingIntentSaturday = PendingIntent.getBroadcast(context, alarmID + 7, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentSaturday);

                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(6, true);
            } else {
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(6, false);
            }
        } else {
            alarm_manager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
            for(int k = 0; k < 7; k++)
                list.get(weeklyAlarmDBItemID).setWeeklyRepeats(k, false);

            list.get(weeklyAlarmDBItemID).setDailyRepeat(false);
        }
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
        alarmIntent.putExtra("AlarmId", index);

        // creating an intent associated with AlarmReceiver class
        Log.e("Log message: ", "alarm created with id: " + index);

        Log.e("Log message: ", "the alarm list id is: " + MainActivity.getList().get(MainActivity.getList().size() - 1).getID());
        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, index, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        AlarmListFragment.updateListView();


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


    public void RingTone (View v){
        // needs implementation for ringtone button

    }

    public boolean inThePast(Calendar examinedCal) {
        if(examinedCal.before(Calendar.getInstance()))
            return true;
        return false;
    }





}


















