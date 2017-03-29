package com.example.madel.seng403;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        int index = intent.getExtras().getInt("AlarmId");
        long id = intent.getLongExtra("id", 0);
        Log.e("LOG MESSAGE:", "inside alarm receiver ID " + index);

        //check to see if the alarm is a repeating alarm
        if(MainActivity.alarmList.get(index).isAlarmRepeating()) {
            //if it is a daily repeating alarm
            if(MainActivity.alarmList.get(index).getAlarmRepeatSettings(0) == true) {
                //set a new alarm with the same id for tomorrow
            } else {
                int day = 0; // initialized to today
                boolean sameDayInThePast = false; // for if the repeat weekly is in the past on the same day it is set
                for(int i = 0; i < 7; i++) {

                    if(MainActivity.alarmList.get(index).getAlarmRepeatSettings(day) == true) {

                        if(false) {
                            //if the alarm is in the past of today
                            sameDayInThePast = true;
                        } else {
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






