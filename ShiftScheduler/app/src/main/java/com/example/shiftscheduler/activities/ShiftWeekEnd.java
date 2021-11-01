package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ShiftWeekEnd extends AppCompatActivity {

    //references to layout controls
    Button backbtn;
    ImageButton addbtn, removebtn;
    EditText shiftdate, selectedschedemp, selectedavailemp;
    //Recycler View Setup:
    private ArrayList<EmployeeModel> employeeList;
    private ArrayList<EmployeeModel> availFullDayEmployeeList;
    private ArrayList<EmployeeModel> schedFullDayEmployeeList;
    private RecyclerView schedFullDayRecyclerView;
    private RecyclerView availFullDayRecyclerView;
    private EmployeeListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_weekend);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.weekEndBack);
        addbtn = (ImageButton) findViewById(R.id.weekEndAdd);
        removebtn = (ImageButton) findViewById(R.id.weekEndRemove);
        shiftdate = (EditText) findViewById(R.id.weekEndShiftDate);
        selectedschedemp = (EditText) findViewById(R.id.weekEndSchedSelectedEmployee);
        selectedavailemp = (EditText) findViewById(R.id.weekEndAvailSelectedEmployee);
        schedFullDayRecyclerView = findViewById(R.id.scheduledWeekendEmployees);
        availFullDayRecyclerView = findViewById(R.id.availableWeekendEmployees);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        //Populate Recycler Views
        updateEmployeeList(localDate);
        buildAllRecyclerViews();
        /* Assume that day objects and shift objects are already pre-created */
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
        //**temp: just for testing purposes
        dbHelper.addShift(localDate, "FULL");

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftWeekEnd.this, ShiftCalendar.class);
                startActivity(myIntent);
            }
        });

        //Button listener for Adding FullDay Employees
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedule an employee for that shift (scheduleEmployee) will update the database
                int employeeID = Integer.parseInt(selectedavailemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
                dbHelper.scheduleEmployee(employeeID, localDate, "FULL");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
                selectedavailemp.getText().clear();
            }
        });
        //Button listener for Removing FullDay Employees
        removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedule an employee for that shift (scheduleEmployee) will update the database
                int employeeID = Integer.parseInt(selectedschedemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
                dbHelper.descheduleEmployee(employeeID, localDate, "FULL");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
                selectedschedemp.getText().clear();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateEmployeeList(LocalDate localDate) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
        availFullDayEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "FULL");
        schedFullDayEmployeeList = (ArrayList) dbHelper.getScheduledEmployees(localDate, "FULL");
    }

    public void buildAllRecyclerViews() {
        buildAvailRecyclerView(availFullDayRecyclerView, availFullDayEmployeeList);
        buildSchedRecyclerView(schedFullDayRecyclerView, schedFullDayEmployeeList);
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
}