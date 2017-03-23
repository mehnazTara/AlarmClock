package com.example.madel.seng403;

import java.util.Calendar;
import android.provider.BaseColumns;
import android.widget.TimePicker;

/**
 * Created by Quinn on 2017-02-27.
 * Object that represents a single alarm.
 * AlarmDbItems are stored within an alarmlist.
 * AlarmDbItems contain information about the alarm: unique id, hour, and minute set.
 */
public class AlarmDBItem implements java.io.Serializable {

    private int id;
    private int hour;
    private int minute;
    private  boolean active;
    private String label;

    public AlarmDBItem(){}

    public AlarmDBItem(Calendar calendar, int id,boolean status)
    {
        this.id = id;
        this.hour = calendar.get(Calendar.HOUR);
        this.minute = calendar.get(Calendar.MINUTE);
        this.active = status;
    }

    public int getID()
    {
        return this.id;
    }
    public void  setHour(int i)
    {
        i= hour;
    }

    public void  setMinute(int i)
    {
        i= minute;
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

    //formats the minute value in a regular :mm format when the minutes value is only one digit.
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

    // gets the active boolean of the object
    public boolean getStatus(){
        return this.active;
    }

    public String getLabel(){
        return this.label;
    }

    public void setStatus(boolean input){
        this.active = input;
    }

    public void setLabel(String input){
        this.label = input;
    }
}
