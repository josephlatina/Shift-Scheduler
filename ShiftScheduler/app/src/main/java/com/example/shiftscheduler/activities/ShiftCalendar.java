package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.models.DayModel;
import com.example.shiftscheduler.models.MonthModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ShiftCalendar extends AppCompatActivity {
    //references to layout controls
    CalendarView calendar;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_calendar);

        //Link the layout controls
        calendar = (CalendarView) findViewById(R.id.calendarView);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //Concatenate to convert date into string format
                String date = year + "-" + (month+1) + "-";
                if (dayOfMonth < 10) {
                    date += "0" + dayOfMonth; //for formatting purposes
                } else {
                    date += dayOfMonth;
                }
                //Determine what day of the week and send to its respective activity
                LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
                int dayOfWeek = localDate.getDayOfWeek().getValue();

                //If It's a weekend, switch to the ShiftWeekEnd Activity. Otherwise, switch to ShiftWeekDay
                if (dayOfWeek == 6 || dayOfWeek == 7) {
                    Intent myIntent = new Intent(ShiftCalendar.this, ShiftWeekEnd.class);
                    myIntent.putExtra("date", date);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(ShiftCalendar.this, ShiftWeekDay.class);
                    myIntent.putExtra("date", date);
                    startActivity(myIntent);
                }
            }
        });
        bottomNavigationView = findViewById(R.id.cal_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    public void onResume() {
        super.onResume();
        //receive intent
        Intent incomingIntent = getIntent();
        DayModel day = (DayModel) incomingIntent.getSerializableExtra("DayObject");
//        if (day != null) {
//            Toast.makeText(ShiftCalendar.this, day.toString(), Toast.LENGTH_SHORT).show();
//        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.employeeFragment:
                            Intent employeeIntent = new Intent(ShiftCalendar.this, EmployeeList.class);
                            startActivity(employeeIntent);
                        case R.id.settingsFragment:
                            break;
                        case R.id.scheduleFragment:
                            break;
                    }
                    return false;
                }

            };

    public MonthModel createMonthObject(int month, int year) {
        return null;
    }

    public void updateMonthObject(LocalDate date) {
        return;
    }

    public DayModel createDayObject(LocalDate date) {
        return null;
    }
}