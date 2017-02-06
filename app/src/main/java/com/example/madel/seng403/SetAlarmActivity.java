package com.example.madel.seng403;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
        Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelAlarmButton(View view)
    {
        Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
