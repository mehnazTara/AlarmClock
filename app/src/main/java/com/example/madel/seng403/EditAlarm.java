package com.example.madel.seng403;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class EditAlarm extends AppCompatActivity {
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    TextView update_label;
    EditText edit;
    TextView editPageText;
    String label = null;

    Context context;
    int index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState== null){
            Bundle extras= getIntent().getExtras();//getting alarm id that we pass in
            if(extras!=null){
                index=extras.getInt("AlarmId");
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context=this;
        alarm_manager= (AlarmManager) getSystemService((ALARM_SERVICE));
        alarm_timepicker=(TimePicker) findViewById(R.id.timePicker);
        update_text=(TextView) findViewById(R.id.list_item_string) ;
        update_label=(TextView) findViewById(R.id.alarm_lable) ;
        editPageText=(TextView) findViewById(R.id.textView) ;

        edit=(EditText) findViewById(R.id.editText2) ;

//initialize buttons
        Button save = (Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {

            //on click stuff is commented out because my phone can not run. If anyone is using this on the empulatr
            //or API>22 then undo comments
            @Override
            public void onClick(View v) {

                label = edit.getText().toString();


                Calendar calendar= Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
                //             calendar.set(Calendar.HOUR_OF_DAY, 4);
              // calendar.set(Calendar.MINUTE, 20);

                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

                if(index==-1){

                    // creating an intent associated with AlarmReceiver class
                   setAlarm(calendar);

                }
                else {
                    ArrayList<AlarmDBItem> list = MainActivity.getList();

                    for (int i = 0; i < MainActivity.getList().size(); i++) {
                        if (list.get(i).getID() == index) {
                            list.get(i).setHour(alarm_timepicker.getHour());
                           list.get(i).setMinute(alarm_timepicker.getMinute());
                            list.get(i).setLabel(label);
                            //list.get(i).setHour(4);
                            //list.get(i).setMinute(20);


                            //      list.remove(i);

                        }
                    }
                    MainActivity.saveFile(context);

                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                    // creating an intent associated with AlarmReceiver class
                    Log.e("Log message: ", "alarm created with id: " + index);

                    //  MainActivity.getList().add(saveSpot,new AlarmDBItem(calendar, index));
                    Log.e("Log message: ", "the alarm list id is: " + MainActivity.getList().get(MainActivity.getList().size() - 1).getID());
                    // creating  a pending intent that delays the intent until the specified calender time is reached
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, index, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // setting the alarm Manager to set alarm at exact time of the user chosen time
                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    AlarmListFragment.updateListView();

                }












            }
        });
    }




    //sets an alarm and makes it active.
    //adds alarm to the alarmist and saves the list.
    private void setAlarm(Calendar targetCal){

        // creating an intent associated with AlarmReceiver class
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        final int alarmID = (int) System.currentTimeMillis();
        Log.e("Log message: ", "alarm created with id: " + alarmID);

        // passing the alarm Id to AlarmReiver
        alarmIntent.putExtra("AlarmId", alarmID);
        Log.e("Log/MESSAGE:Label", "label " + label);

        MainActivity.getList().add(new AlarmDBItem(targetCal, alarmID,true ,label));
        Log.e("Log message: ", "the alarm list id is: " + MainActivity.getList().get(MainActivity.getList().size()-1).getID());

        // creating  a pending intent that delays the intent until the specified calender time is reached
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // setting the alarm Manager to set alarm at exact time of the user chosen time
        alarm_manager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        AlarmListFragment.updateListView();

        MainActivity.saveFile(context);
    }





    public void toList(View view) {
      /*  AlarmListFragment alarmListFragment= new AlarmListFragment();
        FragmentManager alarmList= alarmListFragment.getFragmentManager();
        FragmentTransaction fragmentTransaction= alarmList.beginTransaction();
        fragmentTransaction.commit();*/

        //  Intent intentLoadNewActivity = new Intent(context, AlarmListFragment.class); // change activity

        //   context.startActivity(intentLoadNewActivity);
    }




}


















