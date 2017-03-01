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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by raavi on 2017-02-13.
 * Timepicker for setting a new alarm.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    // private fields
    private TimePicker pickerTime;
    private  Button buttonSetAlarm;
    private AlarmManager alarmManager;
    public static ArrayList<AlarmDBItem> alarmList = new ArrayList<AlarmDBItem>();
    public AlarmAdapter ad;
    private static String fileName = "alarmSaveFile.ser"; //file name for the alarms to be saved.


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
        loadFile(this.getContext());

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

        setAlarm(calTarget, alarmList);
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Alarm Set!", Toast.LENGTH_LONG);
        toast.show();
    }

    //sets an alarm and makes it active.
    //adds alarm to the alarmist and saves the list.
    private void setAlarm(Calendar targetCal, ArrayList<AlarmDBItem> alarmList){

        // creating an intent associated with AlarmReceiver class
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        final int alarmID = (int) System.currentTimeMillis();
        Log.e("Log message: ", "alarm created with id: " + alarmID);
        alarmList.add(new AlarmDBItem(targetCal, alarmID));
        Log.e("Log message: ", "the alarm list id is: " + alarmList.get(alarmList.size()-1).getID());
        saveFile(this.getContext());
        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

    //saves the alarm list to a file
    public void saveFile(Context context)
    {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(alarmList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //loads the alarm list from the file
    public void loadFile(Context context){
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.alarmList = (ArrayList<AlarmDBItem>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
