package com.example.madel.seng403;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Set;

public class SetAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setAlarmButton(View view)
    {
        Intent intent = new Intent(SetAlarmActivity.this, SENG403.class);
        startActivity(intent);
    }

    public void cancelAlarmButton(View view)
    {
        Intent intent = new Intent(SetAlarmActivity.this, SENG403.class);
        startActivity(intent);
    }

}
