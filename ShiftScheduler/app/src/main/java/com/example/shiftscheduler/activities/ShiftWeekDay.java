package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.DayModel;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.EveningShift;
import com.example.shiftscheduler.models.MorningShift;
import com.example.shiftscheduler.models.ShiftModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ShiftWeekDay extends AppCompatActivity {

    //references to layout controls
    Button backbtn;
    ImageButton addopenbtn, addclosebtn, removeopenbtn, removeclosebtn;
    EditText shiftdate, selectedavailemp, selectedschedemp;
    //Recycler View Setup:
    private ArrayList<EmployeeModel> employeeList;
    private ArrayList<EmployeeModel> availOpenEmployeeList;
    private ArrayList<EmployeeModel> availCloseEmployeeList;
    private ArrayList<EmployeeModel> schedOpenEmployeeList;
    private ArrayList<EmployeeModel> schedCloseEmployeeList;
    private RecyclerView schedOpenRecyclerView;
    private RecyclerView schedCloseRecyclerView;
    private RecyclerView availOpenRecyclerView;
    private RecyclerView availCloseRecyclerView;
    private EmployeeListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_weekday);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.dayBack);
        addopenbtn = (ImageButton) findViewById(R.id.addOpening);
        addclosebtn = (ImageButton) findViewById(R.id.addClosing);
        removeopenbtn = (ImageButton) findViewById(R.id.removeOpening);
        removeclosebtn = (ImageButton) findViewById(R.id.removeClosing);
        shiftdate = (EditText) findViewById(R.id.shiftDate);
        selectedavailemp = (EditText) findViewById(R.id.selectedEmployee);
        selectedschedemp = (EditText) findViewById(R.id.selectedEmployee2);
        schedOpenRecyclerView = findViewById(R.id.scheduledOpeningEmployees);
        schedCloseRecyclerView = findViewById(R.id.scheduledClosingEmployees);
        availOpenRecyclerView = findViewById(R.id.availableOpeningEmployees);
        availCloseRecyclerView = findViewById(R.id.availableClosingEmployees);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        updateEmployeeList(localDate);
        buildAllRecyclerViews();

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            //Populate day object
            DayModel day = populateDay(localDate);
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftWeekDay.this, ShiftCalendar.class);
                myIntent.putExtra("date", date);
                myIntent.putExtra("DayObject", day);
                startActivity(myIntent);
            }
        });

        /* Assume that day objects and shift objects are already pre-created */
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
        //**temp: just for testing purposes
        dbHelper.addShift(localDate, "MORNING");
        dbHelper.addShift(localDate, "EVENING");

        //Button listener for Adding Opening Employees
        addopenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedule an employee for that shift (scheduleEmployee) will update the database
                int employeeID = Integer.parseInt(selectedavailemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                dbHelper.scheduleEmployee(employeeID, localDate, "MORNING");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
                selectedavailemp.getText().clear();
            }
        });
        //Button listener for Adding Closing Employees
        addclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedule an employee for that shift (scheduleEmployee) will update the database
                int employeeID = Integer.parseInt(selectedavailemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                dbHelper.scheduleEmployee(employeeID, localDate, "EVENING");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
                selectedavailemp.getText().clear();
            }
        });
        //Button listener for Removing Opening Employees
        removeopenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedule an employee for that shift (scheduleEmployee) will update the database
                int employeeID = Integer.parseInt(selectedschedemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                dbHelper.descheduleEmployee(employeeID, localDate, "MORNING");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
                selectedschedemp.getText().clear();
            }
        });
        //Button listener for Removing Closing Employees
        removeclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedule an employee for that shift (scheduleEmployee) will update the database
                int employeeID = Integer.parseInt(selectedschedemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                dbHelper.descheduleEmployee(employeeID, localDate, "EVENING");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
                selectedschedemp.getText().clear();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateEmployeeList(LocalDate localDate) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
        availOpenEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "MORNING");
        availCloseEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "EVENING");
        schedOpenEmployeeList = (ArrayList) dbHelper.getScheduledEmployees(localDate, "MORNING");
        schedCloseEmployeeList = (ArrayList) dbHelper.getScheduledEmployees(localDate, "EVENING");
    }

    public void buildAllRecyclerViews() {
        buildAvailRecyclerView(availOpenRecyclerView, availOpenEmployeeList);
        buildAvailRecyclerView(availCloseRecyclerView, availCloseEmployeeList);
        buildSchedRecyclerView(schedOpenRecyclerView, schedOpenEmployeeList);
        buildSchedRecyclerView(schedCloseRecyclerView, schedCloseEmployeeList);
    }

    public void buildAvailRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(int position) {
                EmployeeModel employee = employeeList.get(position);
                int empID = employee.getEmployeeID();
                selectedavailemp.setText(String.valueOf(empID));

            }
        });
    }

    public void buildSchedRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(int position) {
                EmployeeModel employee = employeeList.get(position);
                int empID = employee.getEmployeeID();
                selectedschedemp.setText(String.valueOf(empID));

            }
        });
    }

    public DayModel populateDay(LocalDate localDate) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);

        //Retrieve Shift IDs
        int morningShiftID = dbHelper.getShiftID(localDate, "MORNING");
        int eveningShiftID = dbHelper.getShiftID(localDate, "EVENING");

        //Create set of scheduled employees for each shift
        NavigableSet<EmployeeModel> morningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(localDate, "MORNING"));
        NavigableSet<EmployeeModel> eveningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(localDate, "EVENING"));

        //Create shift objects for each respective shift for the day
        MorningShift morningShift = new MorningShift(morningShiftID, localDate, morningEmployees, 2);
        EveningShift eveningShift = new EveningShift(eveningShiftID, localDate, eveningEmployees, 2);

        //Create day object and populate
        DayModel day = new DayModel(localDate, morningShift, eveningShift);

        return day;
    }
}