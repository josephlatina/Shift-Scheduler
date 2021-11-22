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
    EditText shiftdate;
    //Recycler View Setup:
    private ArrayList<EmployeeModel> availFullDayEmployeeList;
    private ArrayList<EmployeeModel> schedFullDayEmployeeList;
    private RecyclerView schedFullDayRecyclerView;
    private RecyclerView availFullDayRecyclerView;
    private EmployeeListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    LocalDate localDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_weekend);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.weekEndBack);
        shiftdate = (EditText) findViewById(R.id.weekEndShiftDate);
        schedFullDayRecyclerView = findViewById(R.id.scheduledWeekendEmployees);
        availFullDayRecyclerView = findViewById(R.id.availableWeekendEmployees);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);
        localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        //Populate Recycler Views
        updateEmployeeList();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateEmployeeList() {
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
        adapter = new EmployeeListAdapter(employeeList,1);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEmployeeClick(int position) {
                EmployeeModel employee = employeeList.get(position);
                int empID = employee.getEmployeeID();

                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
                dbHelper.scheduleEmployee(empID, localDate, "FULL");

                //update Recycler Views
                updateEmployeeList();
                buildAllRecyclerViews();

            }
        });
    }

    public void buildSchedRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEmployeeClick(int position) {
                EmployeeModel employee = employeeList.get(position);
                int empID = employee.getEmployeeID();

                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
                dbHelper.descheduleEmployee(empID, localDate, "FULL");

                //update Recycler Views
                updateEmployeeList();
                buildAllRecyclerViews();
            }
        });
    }
}