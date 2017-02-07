package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * This class deals with setting the alarm and activating the alarm
 * @author Group 13
 */

public class SetAlarmActivity extends AppCompatActivity {

    // private fields
    private TimePicker pickerTime;
    private  Button buttonSetAlarm;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pickerTime = (TimePicker)findViewById(R.id.timePicker);

        // getting the system alarm service
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        buttonSetAlarm = (Button)findViewById(R.id.set_alarm);


        // set on click listener method for set button
        buttonSetAlarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                // the reason for using two calenders for future extension
                // for checking is current time was selected , for example not a time from past
                Calendar calCurr = Calendar.getInstance();

                String time = calCurr.getTime().toString();
                Log.e("Log meesage: ", time);
                Calendar calTarget = (Calendar) calCurr.clone();

                // getting the time from timepicker and setting it to the caldendar
                calTarget.set(Calendar.HOUR_OF_DAY, pickerTime.getHour());
                calTarget.set(Calendar.MINUTE, pickerTime.getMinute());
                calTarget.set(Calendar.SECOND, 0);
                calTarget.set(Calendar.MILLISECOND, 0);


                String time1 = calTarget.getTime().toString();

                Log.e("Log message: ", "set time " + time1);


                setAlarm(calTarget);

            }});
    }


    /*
     *creates the intent, pendingIndent and sets time for alarmManager object
     * @param calendar set to user specified time
     */
    private void setAlarm(Calendar targetCal){

        // creating an intent associated with AlarmReceiver class
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SetAlarmActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        goBacktoMain();
    }


    /*
     * starts the MainActivity intent , i.e takes us back to main page
     *
     */
    public void goBacktoMain()
    {
        Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
        startActivity(intent);
    }


    /*
     * takes us back to MainActivity
     *
     */
    public void cancelAlarmButton(View view)
    {
        Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
