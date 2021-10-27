package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;

public class ShiftDay extends AppCompatActivity {

    //references to layout controls
    Button backbtn;
    EditText shiftdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_day);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.dayBack);
        shiftdate = (EditText) findViewById(R.id.shiftDate);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftDay.this, ShiftCalendar.class);
                startActivity(myIntent);
            }
        });

    }
}