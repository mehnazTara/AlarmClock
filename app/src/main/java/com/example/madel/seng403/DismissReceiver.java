package com.example.madel.seng403;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mehnaz on 2017-02-27.
 */

public class DismissReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.stopService(new Intent(context,RingtonePlayingService.class));

    }
}
