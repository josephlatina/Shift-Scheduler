package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;

public class ShiftWeekEnd extends AppCompatActivity {

    //references to layout controls
    Button backbtn;
    EditText shiftdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_weekend);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.weekendBack);
        shiftdate = (EditText) findViewById(R.id.shiftDateWeekEnd);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftWeekEnd.this, ShiftCalendar.class);
                startActivity(myIntent);
            }
        });
    }
}