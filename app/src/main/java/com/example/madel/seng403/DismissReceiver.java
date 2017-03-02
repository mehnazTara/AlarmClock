package com.example.madel.seng403;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Mehnaz on 2017-02-27.
 * Function for the dismiss button to stop the currently ringing alarm
 */

public class DismissReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // calls on destory method for RingtonePlayingService
        context.stopService(new Intent(context,RingtonePlayingService.class));
    }
}
