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
import android.widget.TextView;
import android.widget.Toast;

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

        final Button cancelButton = (Button) view.findViewById(R.id.cancel_btn);
        cancelButton.setTag(i);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            //functionality of the cancel button for a given alarm
            //prevents the alarm from ringing before it goes off.
            @Override
            public void onClick(View v) {
                cancelButton.setBackgroundColor(0xff0000);
                //Log.e("Log message: ", "button id: " + cancelButton.getTag());
                //Log.e("Log message: ", "alarm cancelled with id: " + alarmList.get((int) cancelButton.getTag()).getID());

                Intent cancelIntent = new Intent(context,AlarmReceiver.class);
                int alarmId = alarmList.get((int) cancelButton.getTag()).getID();


                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) cancelButton.getTag()).getID(), cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                am.cancel(cancelPendingIntent);

                // alarm is inactivated when cancels so that it does not fire up after reboot
                MainActivity.changeAlarmToInactive(alarmId,context);
                Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Cancelled!", Toast.LENGTH_LONG);
                toast.show();

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

                AlarmListFragment.updateListView();
                Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Deleted!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        return view;
    }
}
