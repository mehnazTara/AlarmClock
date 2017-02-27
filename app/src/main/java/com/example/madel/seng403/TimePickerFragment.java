package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by raavi on 2017-02-13.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    // private fields
    private TimePicker pickerTime;
    private  Button buttonSetAlarm;
    private AlarmManager alarmManager;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        Calendar c = Calendar.getInstance();
        TimeZone zone = TimeZone.getTimeZone("Canada/Calgary");
        c.setTimeZone(zone);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        Log.e("LOG MESSAGE INITIAL ", String.valueOf(hour));
        Log.e("LOG INITIAL MIN", String.valueOf(minute));


        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TODO add AlarmManager implementation here

        final int  hour = hourOfDay;
        Log.e("LOG MESSAGE HOUR SET " , String.valueOf(hour));
        final int min = minute;
        Log.e("LOG MESSAGE MINUTE SET " , String.valueOf(min));
        alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);

//        buttonSetAlarm = (Button) view.findViewById(R.id.set_alarm_button);
//
//
//        // set on click listener method for set button
//        buttonSetAlarm.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {

                // the reason for using two calenders for future extension
                // for checking is current time was selected , for example not a time from past
                Calendar calCurr = Calendar.getInstance();

                String time = calCurr.getTime().toString();
                Log.e("Log meesage: ", time);
                Calendar calTarget = (Calendar) calCurr.clone();

                // getting the time from timepicker and setting it to the caldendar
                calTarget.set(Calendar.HOUR, hour);
                calTarget.set(Calendar.MINUTE, min);
                calTarget.set(Calendar.SECOND, 0);
                calTarget.set(Calendar.MILLISECOND, 0);


                String time1 = calTarget.getTime().toString();

                Log.e("Log message: ", "set time " + time1);


                setAlarm(calTarget);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Alarm Set!", Toast.LENGTH_LONG);
                toast.show();



    }


    private void setAlarm(Calendar targetCal){

        // creating an intent associated with AlarmReceiver class
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);

        int id = getAlarmId(getContext());
        Log.e("Log message id " , String.valueOf(id));
        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }


    public static int getAlarmId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int alarmId = preferences.getInt("ALARM", 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ALARM", alarmId + 1).apply();
        return alarmId;
    }


}

