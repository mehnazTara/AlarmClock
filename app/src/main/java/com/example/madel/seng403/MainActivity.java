package com.example.madel.seng403;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, AlarmListFragment.OnFragmentInteractionListener {

    ViewPager viewPager;
    public static ArrayList<AlarmDBItem> alarmList = new ArrayList<AlarmDBItem>();
    public static final String fileName = "alarmSaveFile.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do nothing (required to use sliding fragments)
    }

    public static ArrayList<AlarmDBItem> getList()
    {
        return alarmList;
    }

    //loads the file of alarms from storage.
    //Called when the app is shut down or fragment is reloaded after timepicker fragment.
    public static void loadFile(Context context){
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            alarmList = (ArrayList<AlarmDBItem>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //saves the alarm list to a file
    public static void saveFile(Context context)
    {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(alarmList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFile(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadFile(this);
    }
    public void Edit(View view) {

        Intent intentLoadNewActivity = new Intent(MainActivity.this, EditAlarm.class); // change activity
        startActivity(intentLoadNewActivity);
    }


    public void Ringtone(View view) {
        Intent intentLoadNewActivity = new Intent(MainActivity.this, Edit.class); // change activity
        startActivity(intentLoadNewActivity);
    }






    /**
     * finds the alarmDBitem in the list and changes its active field variable to false
     * @param id
     * @param context
     */
    public static void changeAlarmToInactive( int id, Context context ){
        for (AlarmDBItem alarm: alarmList){
            if(alarm.getID()== id){
                alarm.setStatus(false);
            }
        }
       saveFile(context);
    }

}
