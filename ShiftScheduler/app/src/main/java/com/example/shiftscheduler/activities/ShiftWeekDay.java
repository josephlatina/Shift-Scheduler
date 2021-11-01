package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.MorningShift;
import com.example.shiftscheduler.models.ShiftModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ShiftWeekDay extends AppCompatActivity {

    //references to layout controls
    Button backbtn, addopenbtn, addclosebtn;
    EditText shiftdate, selectedemp;
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
        addopenbtn = (Button) findViewById(R.id.addOpening);
        addclosebtn = (Button) findViewById(R.id.addClosing);
        shiftdate = (EditText) findViewById(R.id.shiftDate);
        selectedemp = (EditText) findViewById(R.id.selectedEmployee);
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
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftWeekDay.this, ShiftCalendar.class);
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
                int employeeID = Integer.parseInt(selectedemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                dbHelper.scheduleEmployee(employeeID, localDate, "MORNING");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
            }
        });
        //Button listener for Adding Closing Employees
        addclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedule an employee for that shift (scheduleEmployee) will update the database
                int employeeID = Integer.parseInt(selectedemp.getText().toString());
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                dbHelper.scheduleEmployee(employeeID, localDate, "MORNING");

                //update Recycler Views
                updateEmployeeList(localDate);
                buildAllRecyclerViews();
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
                selectedemp.setText(String.valueOf(empID));

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
//                EmployeeModel employee = employeeList.get(position);
//                String fullName = employee.getFName() + " " + employee.getLName();
//                selectedemp.setText(fullName);

            }
        });
    }
}