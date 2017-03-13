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
 * Created by Mehnaz on 2017-03-13.
 */

public class BootReceiver extends BroadcastReceiver {

    AlarmManager alarmManager;
    @Override
    public void onReceive(Context context, Intent intent) {

        MainActivity.loadFile(context);
        ArrayList<AlarmDBItem> list = MainActivity.getList();
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Calendar calTarget = Calendar.getInstance();
        if ((intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
                || (intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON"))) {
            for (AlarmDBItem alarm : list) {
                int id = alarm.getID();
                int hour = alarm.getHour();
                int mins = alarm.getMinute();

                // getting the time from timepicker and setting it to the calendar
                calTarget.set(Calendar.HOUR_OF_DAY, hour);
                calTarget.set(Calendar.MINUTE, mins);
                calTarget.set(Calendar.SECOND, 0);
                calTarget.set(Calendar.MILLISECOND, 0);
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // setting the alarm Manager to set alarm at exact time of the user chosen time
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);

            }
        }
    }

}