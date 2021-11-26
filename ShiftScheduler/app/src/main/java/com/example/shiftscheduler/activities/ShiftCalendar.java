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
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.DayModel;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.EveningShift;
import com.example.shiftscheduler.models.FullShift;
import com.example.shiftscheduler.models.MonthModel;
import com.example.shiftscheduler.models.MorningShift;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NavigableSet;
import java.util.TreeSet;

public class ShiftCalendar extends AppCompatActivity {
    public static final String SHIFT_DATE = "com.example.shiftscheduler.activities.SHIFT_DATE";

    //references to layout controls
    CalendarView calendar;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_calendar);

        //Link the layout controls
        calendar = (CalendarView) findViewById(R.id.calendarView);

        //export button listener


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
                    myIntent.putExtra(SHIFT_DATE, date);
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

    /**
     * Builds a specified MonthModel
     * @param month - value of month
     * @param year - specific year
     * @return MonthModel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public MonthModel createMonthObject(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        ArrayList<DayModel> days = new ArrayList<>();

        //iterate through month and fill in days
        LocalDate cursor = startDate;
        while (cursor.getMonthValue() == month) {
            days.add(createDayObject(cursor));
            cursor = cursor.plusDays(1);
        }

        return new MonthModel(startDate, days);
    }

    /**
     * Builds a specified DayModel
     * @param date - LocalDate of day
     * @return DayModel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public DayModel createDayObject(LocalDate date) {
        boolean isWeekend = (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                            date.getDayOfWeek() == DayOfWeek.SUNDAY);

        if (isWeekend) {
            return buildWeekEnd(date);
        } else {
            return buildWeekDay(date);
        }
    }

    /**
     * Builds a weekend DayModel
     * @param date - LocalDate of day
     * @return DayModel
     */
    public DayModel buildWeekEnd(LocalDate date) {
        //*** DON'T KNOW IF THIS IS CORRECT ***

        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);

        //Retrieve Shift IDs
        int fullShiftID = dbHelper.getShiftID(date, "FULL");

        //Create set of scheduled employees
        NavigableSet<EmployeeModel> morningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(date, "FULL"));

        //Create shift object
        FullShift fullShift = new FullShift(fullShiftID, date, morningEmployees, 2);

        //Create day object and populate
        return new DayModel(date, fullShift);
    }

    /**
     * Builds a weekday DayModel
     * @param date - LocalDate of day
     * @return DayModel
     */
    public DayModel buildWeekDay(LocalDate date) {
        //*** DON'T KNOW IF THIS IS CORRECT ***

        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);

        //Retrieve Shift IDs
        int morningShiftID = dbHelper.getShiftID(date, "MORNING");
        int eveningShiftID = dbHelper.getShiftID(date, "EVENING");

        //Create set of scheduled employees for each shift
        NavigableSet<EmployeeModel> morningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(date, "MORNING"));
        NavigableSet<EmployeeModel> eveningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(date, "EVENING"));

        //Create shift objects for each respective shift for the day
        MorningShift morningShift = new MorningShift(morningShiftID, date, morningEmployees, 2);
        EveningShift eveningShift = new EveningShift(eveningShiftID, date, eveningEmployees, 2);

        //Create day object and populate
        return new DayModel(date, morningShift, eveningShift);
    }

    /**
     * Updates an existing MonthModel with a new DayModel
     * @param month - existing MonthModel
     * @param date - LocalDate of new day
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateMonthObject(MonthModel month, LocalDate date) {
        //build the new day object
        boolean isWeekend = (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                            date.getDayOfWeek() == DayOfWeek.SUNDAY);

        DayModel newDay;
        if (isWeekend) {
            newDay = buildWeekEnd(date);
        } else {
            newDay = buildWeekDay(date);
        }

        //update the month object
        month.updateDay(newDay);
    }
}