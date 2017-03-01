package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
 */

public class AlarmAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflator;
    ArrayList<AlarmDBItem> alarmList;
    int idforbuttonalarm;

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

   /* @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view = inflator.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }
        TextView name = (TextView) view.findViewById(android.R.id.text1);
        name.setText(alarmList.get(i).getHourString() + ":" + alarmList.get(i).getMinuteString());
        return view;
    } */

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view = inflator.inflate(R.layout.rowlayout, viewGroup, false);
        }
        TextView name = (TextView) view.findViewById(R.id.list_item_string);
        name.setText(alarmList.get(i).getHourString() + ":" + alarmList.get(i).getMinuteString());
        idforbuttonalarm = i;
        final Button cancelButton = (Button) view.findViewById(R.id.delete_btn);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancelButton.setBackgroundColor(0xff0000);
                Intent cancelIntent = new Intent(context,AlarmReceiver.class);
                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, alarmList.get(idforbuttonalarm).getID(), cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                am.cancel(cancelPendingIntent);
                Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Cancelled!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        return view;
    }

}
