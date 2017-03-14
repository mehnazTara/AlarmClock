package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.madel.seng403.MainActivity.alarmList;

public class EditAlarm extends AppCompatActivity {
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    int idforbuttonalarm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context=this;
        alarm_manager= (AlarmManager) getSystemService((ALARM_SERVICE));
        alarm_timepicker=(TimePicker) findViewById(R.id.timePicker);
       update_text=(TextView) findViewById(R.id.list_item_string) ;
        Calendar calendar= Calendar.getInstance();

//initialize buttons
        Button save = (Button)findViewById(R.id.save_button);

       save.setOnClickListener(new View.OnClickListener() {
           

           @Override
           public void onClick(View v) {



               Toast toast = Toast.makeText(context.getApplicationContext(), "Alarm Cancelled!", Toast.LENGTH_LONG);
               toast.show();

           }
       });
    }


    public void toList(View view) {
        Intent intentLoadNewActivity = new Intent(EditAlarm.this, AlarmListFragment.class); // change activity
        startActivity(intentLoadNewActivity);
    }



}



















