package com.example.madel.seng403;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static final int MY_NOTIFICATION_ID=1;
    NotificationManager notificationManager;
    Notification myNotification;
    private String dismiss = "dismiss";
    private final String myBlog = "http://android-er.blogspot.com/";

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra("id", 0);
        Log.e("LOG MESSAGE:", "inside alarm receiver");


        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        // start ringtone service
        context.startService(service_intent);

        Toast.makeText(context, "-------Alarm on!!!!!--------", Toast.LENGTH_LONG).show();
        // for activating vibration
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);


        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                0);

        Intent dismissIntent  = new Intent(context,DismissReceiver.class);
        PendingIntent disMissPendingIntent = PendingIntent.getBroadcast(context,0,dismissIntent,0);
       myNotification = new NotificationCompat.Builder(context)

                .setContentTitle("Alarm Notification!")
                .setContentText("An alarm is ringing")
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)

                .setSmallIcon(R.mipmap.notify_icon)

                .addAction(R.mipmap.snooze_icon, "snooze", null)
                .addAction(R.mipmap.xicon, "dismiss", null)



               .addAction(R.mipmap.snooze_icon, "snooze", null)
               .addAction(R.mipmap.xicon, "dismiss", disMissPendingIntent)






               .build();

        notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
    }

}






  /* public void onReceive(Context context, Intent intent, View view) {

        // for testing
        Log.e("LOG MESSAGE:", "inside alarm receiver");
        // create intent to the ringtone and notification service

        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        // start ringtone service
        context.startService(service_intent);

        Toast.makeText(context, "-------Alarm on!!!!!--------", Toast.LENGTH_LONG).show();
        // for activating vibration
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);




*/





