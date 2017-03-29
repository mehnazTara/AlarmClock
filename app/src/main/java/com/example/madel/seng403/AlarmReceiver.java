package com.example.madel.seng403;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

/*
Creates a ringtone for the alarm to sound when an intent is activated.
Notification is also generated in the notification center of the fun.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static final int MY_NOTIFICATION_ID = 1;
    private static NotificationManager notificationManager;
    Notification myNotification;
    private String dismiss = "dismiss";
    private final String note = "alarm notification";
    AlarmDBItem alarmItem ;

    @Override
    public void onReceive(Context context, Intent intent) {
        int index = intent.getExtras().getInt("AlarmId");
        long id = intent.getLongExtra("id", 0);
        Log.e("LOG MESSAGE:", "inside alarm receiver ID " + index);

        ArrayList<AlarmDBItem> list = MainActivity.getList();

        // find the AlarmDBItem with the same id and sets the hour and minute as the input
        for (int i = 0; i < MainActivity.getList().size(); i++) {
            if (list.get(i).getID() == index) {
                alarmItem = list.get(i);
            }
        }
        //check to see if the alarm is a repeating alarm
        if(alarmItem.isAlarmRepeating()) {
            //if it is a daily repeating alarm
            if(alarmItem.getAlarmRepeatSettings(0) == true) {
                int alarmID = index;

                // creating an intent associated with AlarmReceiver class
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                Log.e("Log message: ", "alarm created with id: " + alarmID);

                // passing the alarm Id to AlarmReiver
                alarmIntent.putExtra("AlarmId", alarmID);

                // creating  a pending intent that delays the intent until the specified calender time is reached
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // setting the alarm Manager to set alarm at exact time of the user chosen time
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + (24 * 60 * 60 * 1000)), pendingIntent);
                //set a new alarm with the same id for tomorrow
            } else {
                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK); // initialized to today
                boolean sameDayInThePast = false; // for if the repeat weekly is in the past on the same day it is set
                for(int i = 0; i < 7; i++) {
                    if(alarmItem.getAlarmRepeatSettings(day) == true) {

                        if(day == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                            //if the alarm is in the past of today
                            sameDayInThePast = true;
                        } else {
                            Calendar now = Calendar.getInstance();

                            now.set(Calendar.DAY_OF_WEEK, day);
                            //if the day is in the past, set the alarm for a week in the future
                            if(now.getTime().before(Calendar.getInstance().getTime()))
                            {
                                int alarmID = index;

                                // creating an intent associated with AlarmReceiver class
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                                Log.e("Log message: ", "alarm created with id: " + alarmID);

                                // passing the alarm Id to AlarmReiver
                                alarmIntent.putExtra("AlarmId", alarmID);

                                // creating  a pending intent that delays the intent until the specified calender time is reached
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                // setting the alarm Manager to set alarm at exact time of the user chosen time
                                alarmManager.set(AlarmManager.RTC_WAKEUP, (now.getTimeInMillis() + 7 * 24 * 60 * 60 * 1000), pendingIntent);
                                //set a new alarm with the same id for the day that the alarm is supposed to ring
                            } else {
                                int alarmID =index;

                                // creating an intent associated with AlarmReceiver class
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                                Log.e("Log message: ", "alarm created with id: " + alarmID);

                                // passing the alarm Id to AlarmReiver
                                alarmIntent.putExtra("AlarmId", alarmID);

                                // creating  a pending intent that delays the intent until the specified calender time is reached
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                // setting the alarm Manager to set alarm at exact time of the user chosen time
                                alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);
                                //set a new alarm with the same id for the day that the alarm is supposed to ring
                            }

                            //set a new alarm pending intent for that day with the same id as the current alarm
                            // if the alarm is in the past, set it for a week in the future
                            break;
                        }
                    }
                    //if checking saturday, next day is sunday
                    if(day == 7)
                        day = 0;
                    day++;
                }

                if(sameDayInThePast == true) {
                    //set a new alarm pending intent for 1 week in the future today
                    int alarmID = index;

                    // creating an intent associated with AlarmReceiver class
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    Log.e("Log message: ", "alarm created with id: " + alarmID);

                    // passing the alarm Id to AlarmReiver
                    alarmIntent.putExtra("AlarmId", alarmID);

                    // creating  a pending intent that delays the intent until the specified calender time is reached
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // setting the alarm Manager to set alarm at exact time of the user chosen time
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + (7* 24 * 60 * 60 * 1000)), pendingIntent);
                    //set a new alarm with the same id for one week from now
                }
            }
        } else {
            // changing the alarm to inactive status
            MainActivity.changeAlarmToInactive(index, context);
        }

        // for testing purpose only
        MainActivity.loadFile(context);

        for (AlarmDBItem alarm: MainActivity.getList()){
            if(alarm.getID()== index){
                if(!alarm.getStatus()) {
                    Log.e("Alarm status", "false");
                }
            }
        }

        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        // start ringtone service
        context.startService(service_intent);

        Toast.makeText(context, "-------Alarm on!!!!!--------", Toast.LENGTH_LONG).show();
        // for activating vibration
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);

        // make dismiss button visible
        MainFragment.setDismissButtonVisible(true);

        // creating intent and pending intent for starting notification
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(note));
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);


        // creating intent and pending intent for starting Dismiss BroadcastReceiver
        Intent dismissIntent  = new Intent(context,DismissReceiver.class);
        PendingIntent disMissPendingIntent = PendingIntent.getBroadcast(context,0,dismissIntent,0);

        // creating intent and pending intent for snooze
        Intent snoozeIntent = new Intent(context, SnoozeReceiver.class);
        snoozeIntent.putExtra("notificationId", MY_NOTIFICATION_ID);
        PendingIntent doSnoozeIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent,0);


        // building and setting the motification
        myNotification = new NotificationCompat.Builder(context)
                .setContentTitle("Alarm Notification!")
                .setContentText("An alarm is ringing")
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.notify_icon)
                .addAction(R.mipmap.snooze_icon, "snooze", doSnoozeIntent)
                .addAction(R.mipmap.xicon, "dismiss", disMissPendingIntent)
                .build();

        // starting the notification service
        notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
    }

    public static void dismissNotification() {
        if (notificationManager != null) notificationManager.cancel(MY_NOTIFICATION_ID);
    }

}






