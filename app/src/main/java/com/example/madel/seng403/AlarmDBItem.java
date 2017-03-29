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
    private boolean alarmRepeatSettings[];

    public AlarmDBItem(){}

    public AlarmDBItem(Calendar calendar, int id,  boolean status, String text)
    {
        this.id = id;
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.active = status;
        if (text != null){
            this.label = text;
        }
        else{
            this.label = "default label";
        }
        //in this boolean array, position 0 represents a daily repeating alarm
        //positions 1-7 represent days of the week from sunday to saturday
        //1 = sunday, 2 = monday, 3 = tuesday, 4 = wednesday, 5 = thursday, 6 = friday, 7 = saturday
        this.alarmRepeatSettings = new boolean[8];
        for(int i = 0; i < 8; i++)
            this.alarmRepeatSettings[i] = false;
    }

    public int getID()
    {
        return this.id;
    }
    public void  setHour(int i)
    {
        this.hour = i;
    }

    public void  setMinute(int i)
    {
       this.minute = i;
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

    public boolean getAlarmRepeatSettings(int index) {
        return this.alarmRepeatSettings[index];
    }

    public void setAlarmRepeatSettings(int index, boolean setting) {
        this.alarmRepeatSettings[index] = setting;
    }

    public boolean isAlarmRepeating() {
        for(int i = 0; i < 8; i++) {
            if (alarmRepeatSettings[i] == true)
                return true;
        }
        return false;
    }

    public boolean[] getAlarmRepeatArray() { return this.alarmRepeatSettings; }
}
