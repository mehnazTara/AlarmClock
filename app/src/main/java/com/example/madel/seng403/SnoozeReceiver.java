package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Quinn on 2017-03-01.
 * Stops the alarm currently ringing, and sets a new alarm to start in 15 seconds.
 * Activated by the snooze button.
 */

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.stopService(new Intent(context, RingtonePlayingService.class));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + (1000*15)), pendingIntent);
    }
}
