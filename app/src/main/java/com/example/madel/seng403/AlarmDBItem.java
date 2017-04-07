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

    private boolean dailyRepeat;
    private boolean weeklyRepeats[];

    public AlarmDBItem(){}

    public AlarmDBItem(Calendar calendar, int id,boolean status, String text)
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
        weeklyRepeats = new boolean[7];
        for(int i = 0; i < 7; i++) {
            weeklyRepeats[i] = false;
        }
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

    public void setWeeklyRepeats(int index, boolean value) {
        weeklyRepeats[index] = value;
    }

    public boolean[] getWeeklyRepeats() {
        return weeklyRepeats;
    }

    public void setDailyRepeat(boolean value) {
        dailyRepeat = value;
    }

    public boolean getDailyRepeat() {
        return dailyRepeat;
    }


}
