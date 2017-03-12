package com.example.madel.seng403;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.Provider;

/**
 * Created by coolest person ever on 2017-02-14.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("LocalService", "Recieved start id"+ startId+ ":" + intent);
        media_song= MediaPlayer.create(this, R.raw.song1);
        media_song.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (media_song.isPlaying())
                    media_song.stop();
            }
        },30000);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){

        //tell us we have stopped
        Toast.makeText(this, "on destroy called", Toast.LENGTH_SHORT). show();
        media_song.stop();
    }




}