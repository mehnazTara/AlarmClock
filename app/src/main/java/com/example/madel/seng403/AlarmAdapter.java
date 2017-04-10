package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Quinn on 2017-02-27.
 * Adapter for the list view of alarmlistfragment for formatting
 */

public class AlarmAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AlarmDBItem> alarmList;
    private boolean[] weeklyLabels = new boolean[7];
    private boolean dailyFlag;

    public AlarmAdapter(Context c, ArrayList<AlarmDBItem> alarms)
    {
        this.context = c;
        this.alarmList = alarms;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int i) {
        return alarmList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alarmList.get(i).getID();
    }
//remove this ;ater
    public long ItemClicked(int i) {
        return alarmList.get(i).getID();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if(view==null)
        {
            view = inflater.inflate(R.layout.rowlayout, viewGroup, false);
        }
        TextView name = (TextView) view.findViewById(R.id.list_item_string);

        weeklyLabels = alarmList.get(i).getWeeklyRepeats();
        String[] weekStrings = new String[7];
        for (int j = 0; j < 7; j++) {
            weekStrings[j] = ""; }

        String dailyString = "";
        dailyFlag = alarmList.get(i).getDailyRepeat();
        if (dailyFlag)
            dailyString = "Daily";
        else {
            if (weeklyLabels[0] == true)
                weekStrings[0] = "Sun ";
            if (weeklyLabels[1] == true)
                weekStrings[1] = "Mon ";
            if (weeklyLabels[2] == true)
                weekStrings[2] = "Tues ";
            if (weeklyLabels[3] == true)
                weekStrings[3] = "Wed ";
            if (weeklyLabels[4] == true)
                weekStrings[4] = "Thurs ";
            if (weeklyLabels[5] == true)
                weekStrings[5] = "Fri ";
            if (weeklyLabels[6] == true)
                weekStrings[6] = "Sat";
        }



        name.setText(alarmList.get(i).getHourString() + ":" + alarmList.get(i).getMinuteString() + "\n" + alarmList.get(i).getLabel() + " " + dailyString + " " +
                weekStrings[0] + weekStrings[1] + weekStrings[2] + weekStrings[3] + weekStrings[4] + weekStrings[5] + weekStrings[6]);
        //Log.e("Log message: ", "List View Labels: " + name.getText().toString());
        final Switch cancelToggle = (Switch) view.findViewById(R.id.cancel_btn);
        cancelToggle.setTag(i);
        AlarmDBItem alarm =  alarmList.get((int) cancelToggle.getTag());

        // only enable if alarm is active
        if(alarm.getStatus()) {
            cancelToggle.setChecked(true);
        }
        cancelToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Cancel toggle function
                if (!isChecked) {
                    Intent cancelIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) cancelToggle.getTag()).getID(), cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    am.cancel(cancelPendingIntent);

                    //For disabling weekly alarms
                    for(int j = 1; j < 8; j++) {
                        cancelIntent = new Intent(context, AlarmReceiver.class);
                        cancelPendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) cancelToggle.getTag()).getID() + j, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        am.cancel(cancelPendingIntent);
                    }

                    // making sure alarm is disabled
                    MainActivity.changeAlarmToInactive(alarmList.get((int) cancelToggle.getTag()).getID(),context);
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Cancelled!", Toast.LENGTH_LONG);
                    toast.show();
                }
                //Enable alarm toggle function
                else {

                    int cancelAlarmListID = alarmList.get((int) cancelToggle.getTag()).getID();

                    boolean weeklyArray[] = alarmList.get((int)cancelToggle.getTag()).getWeeklyRepeats();
                    boolean dailyRepeat = alarmList.get((int)cancelToggle.getTag()).getDailyRepeat();

                    Calendar calCurr = Calendar.getInstance();

                    calCurr.set(Calendar.HOUR, alarmList.get((int) cancelToggle.getTag()).getHour());
                    calCurr.set(Calendar.MINUTE, alarmList.get((int) cancelToggle.getTag()).getMinute());
                    calCurr.set(Calendar.SECOND, 0);
                    calCurr.set(Calendar.MILLISECOND, 0);

                    while(calCurr.before(Calendar.getInstance()))
                    {
                        calCurr.set(Calendar.DAY_OF_MONTH, (int) calCurr.getTimeInMillis() + (int) AlarmManager.INTERVAL_DAY);
                        Log.e("Log message: ", "Current Day of the Week:" + calCurr.DAY_OF_WEEK);
                    }

                    //check to see if it is a repeating alarm
                    if(weeklyArray[0] != true && weeklyArray[1] != true && weeklyArray[2] != true && weeklyArray[3] != true && weeklyArray[4] != true && weeklyArray[5] != true && weeklyArray[6] != true && dailyRepeat != true) {
                        //not a repeating alarm
                        Intent enableIntent = new Intent(context, AlarmReceiver.class);
                        PendingIntent enablePendingIntent = PendingIntent.getBroadcast(context, cancelAlarmListID, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                        am.setExact(am.RTC_WAKEUP, calCurr.getTimeInMillis(), enablePendingIntent);
                    } else if (dailyRepeat == true) { //it is a daily repeating alarm
                        Intent enableIntent = new Intent(context, AlarmReceiver.class);
                        PendingIntent enablePendingIntent = PendingIntent.getBroadcast(context, cancelAlarmListID, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                        am.setInexactRepeating(am.RTC_WAKEUP, calCurr.getTimeInMillis(), AlarmManager.INTERVAL_DAY, enablePendingIntent);
                    } else { //its a weekly repeating alarm
                        Calendar tempCal = calCurr;
                        Intent enableIntent = new Intent(context, AlarmReceiver.class);
                        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                        if (weeklyArray[0] == true) {
                            while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                                tempCal.add(Calendar.DATE, 1);
                            PendingIntent pendingIntentSunday = PendingIntent.getBroadcast(context, cancelAlarmListID + 1, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentSunday);
                            tempCal = calCurr;
                        }
                        if (weeklyArray[1] == true) {
                            while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
                                tempCal.add(Calendar.DATE, 1);
                            PendingIntent pendingIntentMonday = PendingIntent.getBroadcast(context, cancelAlarmListID + 2, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentMonday);
                            tempCal = calCurr;
                        }
                        if (weeklyArray[2] == true) {
                            while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY)
                                tempCal.add(Calendar.DATE, 1);
                            PendingIntent pendingIntentTuesday = PendingIntent.getBroadcast(context, cancelAlarmListID + 3, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentTuesday);
                            tempCal = calCurr;
                        }
                        if (weeklyArray[3] == true) {
                            while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY)
                                tempCal.add(Calendar.DATE, 1);
                            PendingIntent pendingIntentWednesday = PendingIntent.getBroadcast(context, cancelAlarmListID + 4, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentWednesday);
                            tempCal = calCurr;
                        }
                        if (weeklyArray[4] == true) {
                            while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY)
                                tempCal.add(Calendar.DATE, 1);
                            PendingIntent pendingIntentThursday = PendingIntent.getBroadcast(context, cancelAlarmListID + 5, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentThursday);
                            tempCal = calCurr;
                        }
                        if (weeklyArray[5] == true) {
                            while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY)
                                tempCal.add(Calendar.DATE, 1);
                            PendingIntent pendingIntentFriday = PendingIntent.getBroadcast(context, cancelAlarmListID + 6, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentFriday);
                            tempCal = calCurr;
                        }
                        if (weeklyArray[6] == true) {
                            while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                                tempCal.add(Calendar.DATE, 1);
                            PendingIntent pendingIntentSaturday = PendingIntent.getBroadcast(context, cancelAlarmListID + 7, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tempCal.getTimeInMillis(), 24 * 60 * 60 * 7 * 1000, pendingIntentSaturday);
                        }
                    }
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Enabled!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        final ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_btn);
        deleteButton.setTag(i);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            //functionality of the delete button for a given alarm
            //prevents the alarm from ringing before it goes off then deletes the alarm from the list
            @Override
            public void onClick(View v) {
                deleteButton.setBackgroundColor(0xff0000);

                Intent deleteIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) deleteButton.getTag()).getID(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                int temp = alarmList.get((int) deleteButton.getTag()).getID();
                AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                am.cancel(deletePendingIntent);

                //Deletes weekly alarms too
                for(int j = 1; j < 8; j++) {
                    deletePendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) deleteButton.getTag()).getID() + j, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.cancel(deletePendingIntent);
                }

                MainActivity.getList().remove((int) deleteButton.getTag());
                MainActivity.saveFile(context);

                AlarmListFragment.updateListView();
                Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Deleted!", Toast.LENGTH_LONG);
                toast.show();
            }
        });

       final Button edit = (Button) view.findViewById(R.id.edit);
        edit.setTag(i);
        edit.setOnClickListener(new View.OnClickListener(){
            //functionality of the cancel button for a given alarm
            //prevents the alarm from ringing before it goes off.
            @Override
            public void onClick(View v) {
                int alarmId=  alarmList.get((int) cancelToggle.getTag()).getID();

                Intent intentLoadNewActivity = new Intent(context, EditAlarm.class); // change activity
                intentLoadNewActivity.putExtra("AlarmId", alarmId);
                intentLoadNewActivity.addCategory("Edit");

                context.startActivity(intentLoadNewActivity);
            }
        });

        return view;
    }
}
