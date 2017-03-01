package com.example.madel.seng403;

import android.icu.util.Calendar;
import android.provider.BaseColumns;
import android.widget.TimePicker;

/**
 * Created by Quinn on 2017-02-27.
 */

public class AlarmDBItem implements java.io.Serializable {

    private int id;
    private int hour;
    private int minute;

    public AlarmDBItem(){}

    public AlarmDBItem(Calendar calendar, int id)
    {
        this.id = id;
        this.hour = calendar.get(Calendar.HOUR);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public int getID()
    {
        return this.id;
    }

    public int getHour()
    {
        return this.hour;
    }

    public int getMinute()
    {
        return this.minute;
    }

    public String getHourString()
    {
        return Integer.toString(this.hour);
    }

    public String getMinuteString()
    {
        if(minute<10)
        {
            return "0"+minute;
        }
        else
        {
            return Integer.toString(minute);
        }
    }

}
