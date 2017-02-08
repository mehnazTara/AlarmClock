package com.example.madel.seng403;


import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Mehnaz  Tarannum on 2017-02-07.
 * This class represents the receiver of the alarm set by the user and it uses system Alarm Service to
 * activate alarm. Alarm is activated even if app is not open since we are inheriting WakefulBroadcastReiver class
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    /*
    *onRecive method is called when object of this class is created
    * it handles the actions when an alarm is activated
    * it produces a toast message and activates vibration for 5 seconds
    * @param Context, pendingIntent associated with the Alarm Manager
    *
     */
    public void onReceive(Context context, Intent intent) {

        // for testing
        Log.e("LOG MESSAGE:","inside alarm receiver");
        Toast.makeText(context, "-------Alarm on!!!!!--------", Toast.LENGTH_LONG).show();
        // for activating vibration
        Vibrator vibrator = (Vibrator)context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);

        //@@ Ore this is where you should put the code for dialog fragment
    }
}
