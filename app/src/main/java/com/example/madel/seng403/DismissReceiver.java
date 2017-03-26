package com.example.madel.seng403;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mehnaz on 2017-02-27.
 * Function for the dismiss button to stop the currently ringing alarm
 */

public class DismissReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // calls on destroy method for RingtonePlayingService
        context.stopService(new Intent(context,RingtonePlayingService.class));

        // dismiss notification
        AlarmReceiver.dismissNotification();

        // set dismiss button to invisible again
        MainFragment.setDismissButtonVisible(false);
    }
}
