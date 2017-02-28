package com.example.madel.seng403;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Quinn on 2017-02-27.
 */

public class AlarmAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflator;
    ArrayList<AlarmDBItem> alarmList;

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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view = inflator.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }
        TextView name = (TextView) view.findViewById(android.R.id.text1);
        name.setText(alarmList.get(i).getHourString() + ":" + alarmList.get(i).getMinuteString());
        return view;
    }
}
