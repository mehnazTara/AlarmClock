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
 * This class represents the RingtoneService. This class uses MediaPlayer to play mp4 from
 * raw folder.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
//    int startid;
//    boolean isReturning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }



    @Override
    /**
     * this methods is called when this Service class is instantiated
     * it starts the mp3 from raw folder
     */
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("LocalService", "Recieved start id"+ startId+ ":" + intent);
        media_song= MediaPlayer.create(this, R.raw.song1);

        // starts the song
        media_song.start();

        // this block of code makes the song stop after 30 seconds
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
        // song is stopped on destroy
        media_song.stop();
    }



}