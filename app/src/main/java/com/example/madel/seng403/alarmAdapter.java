package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

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

    public AlarmAdapter(Context c, ArrayList<AlarmDBItem> alarms) {
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


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflator.inflate(R.layout.rowlayout, viewGroup, false);
        }
        TextView name = (TextView) view.findViewById(R.id.list_item_string);
        name.setText(alarmList.get(i).getHourString() + ":" + alarmList.get(i).getMinuteString());
        idforbuttonalarm = i;

        final ToggleButton cancelToggle = (ToggleButton) view.findViewById(R.id.cancel_btn);
        cancelToggle.setTag(i);
        cancelToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Cancel toggle function
                if (isChecked) {
                    Intent cancelIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, (int) alarmList.get((int) cancelToggle.getTag()).getID(), cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    am.cancel(cancelPendingIntent);
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Cancelled!", Toast.LENGTH_LONG);
                    toast.show();
                }
                //Enable alarm toggle function
                else {
                    Intent enableIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent enablePendingIntent = PendingIntent.getBroadcast(context, (int) alarmList.get((int) cancelToggle.getTag()).getID(), enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                    Calendar calCurr = Calendar.getInstance();

                    calCurr.set(Calendar.HOUR, alarmList.get((int) cancelToggle.getTag()).getHour());
                    calCurr.set(Calendar.MINUTE, alarmList.get((int) cancelToggle.getTag()).getMinute());
                    calCurr.set(Calendar.SECOND, 0);
                    calCurr.set(Calendar.MILLISECOND, 0);

                    am.setExact(am.RTC_WAKEUP, calCurr.getTimeInMillis(), enablePendingIntent);
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Enabled!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        final Button deleteButton = (Button) view.findViewById(R.id.delete_btn);
        deleteButton.setTag(i);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            //functionality of the delete button for a given alarm
            //prevents the alarm from ringing before it goes off then deletes the alarm from the list
            @Override
            public void onClick(View v) {
                deleteButton.setBackgroundColor(0xff0000);

                Intent deleteIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) deleteButton.getTag()).getID(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                am.cancel(deletePendingIntent);

                MainActivity.getList().remove(i);
                MainActivity.saveFile(context);

                AlarmListFragment.updateListView();
                Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Deleted!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        return view;
    }
}
