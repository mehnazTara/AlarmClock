package com.example.madel.seng403;

import android.app.PendingIntent;
import android.content.Intent;

/**
 * Created by Madel on 2017-03-27.
 */

public class RepeatWeekly extends Activity {

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.content_layout_id);

        //Initalize repeat weekly checboxes
        final CheckBox sn_checkBox = (CheckBox) findViewById(R.id.verify_SN);
        final CheckBox m_checkBox = (CheckBox) findViewById(R.id.verify_M);
        final CheckBox t_checkBox = (CheckBox) findViewById(R.id.verify_T);
        final CheckBox w_checkBox = (CheckBox) findViewById(R.id.verify_W);
        final CheckBox tr_checkBox = (CheckBox) findViewById(R.id.verify_TR);
        final CheckBox f_checkBox = (CheckBox) findViewById(R.id.verify_F);
        final CheckBox s_checkBox = (CheckBox) findViewById(R.id.verify_S);

        if (m_checkBox.isChecked()) {
            forday(2);
        }
        if (t_checkBox.isChecked()) {
            forday(3);
        }
        if (w_checkBox.isChecked()) {
            forday(4);
        }
        if (tr_checkBox.isChecked()) {
            forday(5);
        }
        if (f_checkBox.isChecked()) {
            forday(6);
        }
        if (s_checkBox.isChecked()) {
            forday(7);
        }
        if (sn_checkBox.isChecked()) {
            forday(1);
        }
    }
    public void repeatForDay(int week) {

        Intent enableIntent = new Intent(context, AlarmReceiver.class);

        PendingIntent enablePendingIntent = PendingIntent.getBroadcast(context, alarmList.get((int) cancelToggle.getTag()).getID(), enableIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        calSet.set(Calendar.DAY_OF_WEEK, week);
        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, minuts);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 7 * 60 * 60 * 1000, pendingIntent);
    }

}
