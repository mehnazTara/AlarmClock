package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Quinn on 2017-02-27.
 * Adapter for the list view of alarmlistfragment for formatting
 */

public class AlarmAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflator;
    ArrayList<AlarmDBItem> alarmList;
    int idforbuttonalarm;
    long currentHour, currentMin;

    public AlarmAdapter(Context c, ArrayList<AlarmDBItem> alarms)
    {



        this.context = c;
        this.alarmList = alarms;
        inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int i) {
        return alarmList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alarmList.get(i).getID();
    }
//remove this ;ater
    public long ItemClicked(int i) {
        return alarmList.get(i).getID();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {





        if(view==null)
        {
            view = inflator.inflate(R.layout.rowlayout, viewGroup, false);
        }
        TextView name = (TextView) view.findViewById(R.id.list_item_string);
        name.setText(alarmList.get(i).getHourString() + ":" + alarmList.get(i).getMinuteString());
        idforbuttonalarm = i;






        //Madel's stuff
        currentHour = alarmList.get(i).getHour();
        currentMin = alarmList.get(i).getMinute();

        //Cancel Toggle Button
        final ToggleButton cancelToggle = (ToggleButton) view.findViewById(R.id.cancel_btn);
        cancelToggle.setTag(i);
        cancelToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Cancel toggle function
                if (isChecked) {
                    Intent cancelIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) cancelToggle.getTag()).getID(), cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    am.cancel(cancelPendingIntent);
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Set!", Toast.LENGTH_LONG);
                    toast.show();
                }
                //Enable alarm toggle function
                else {
                    Intent enableIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent enablePendingIntent = PendingIntent.getBroadcast(context, 0, enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    long millTime = TimeUnit.HOURS.toMillis(currentHour) + TimeUnit.MINUTES.toMillis(currentMin);
                    am.setExact(am.RTC_WAKEUP, millTime, enablePendingIntent);
                }
            }
        });









        final Button cancelButton = (Button) view.findViewById(R.id.delete_btn);











       final Button edit = (Button) view.findViewById(R.id.edit);
        edit.setTag(i);
        edit.setOnClickListener(new View.OnClickListener(){
            //functionality of the cancel button for a given alarm
            //prevents the alarm from ringing before it goes off.
            @Override
            public void onClick(View v) {
                int alarmId=  alarmList.get((int) cancelButton.getTag()).getID();

                Intent intentLoadNewActivity = new Intent(context, EditAlarm.class); // change activity
                intentLoadNewActivity.putExtra("AlarmId", alarmId);

                context.startActivity(intentLoadNewActivity);


            }
        });











        return view;
    }

}
