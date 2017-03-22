package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 *
 * Created by Mehnaz on 2017-03-13.
 * This class is called whenever the system has completed rebooting , example restarted
 * This iterates through all the alarms created so far and fired off the ones in the future and are active
 *
 */

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        // getting the file with arrayList object
        MainActivity.loadFile(context);
        ArrayList<AlarmDBItem> list = MainActivity.getList();
     //   if ((intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
       //         || (intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON"))) {
            for (AlarmDBItem alarm : list) {
                int id = alarm.getID();
                int hour = alarm.getHour();
                int mins = alarm.getMinute();
                boolean active = alarm.getStatus();

                // only set the alarms that are active and in future than current time
                if (active) {
                    // for testing
                    Log.e("Inside Active if", "id was " + id);
                    Calendar calTarget = Calendar.getInstance();
                    Log.e("TimeSet", hour + ":" + mins);

                    // getting the time from AlarmDBitem  and setting it to the calendar
                    calTarget.set(Calendar.HOUR_OF_DAY, hour);
                    calTarget.set(Calendar.MINUTE, mins);
                    calTarget.set(Calendar.SECOND, 0);
                    calTarget.set(Calendar.MILLISECOND, 0);

                    // getting current calendar to compare
                    Calendar now = Calendar.getInstance();

                    // checking if the set time is not in the past
                    if (!calTarget.before(now)) {
                        String time1 = calTarget.getTime().toString();

                        Log.e("Log message: ", "set time " + time1);

                        // firting up the pending intents
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                        intent.putExtra("AlarmId", id);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                        // setting the alarm Manager to set alarm at exact time of the user chosen time
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);

                    }
                }
            }
        }


}