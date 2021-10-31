package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.EveningShift;
import com.example.shiftscheduler.models.MorningShift;
import com.example.shiftscheduler.models.ShiftModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class ShiftDay extends AppCompatActivity {

    //references to layout controls
    Button backbtn, addopenbtn, addclosebtn;
    EditText shiftdate;
    //Recycler View Setup:
    private ArrayList<EmployeeModel> employeeList;
    private ArrayList<EmployeeModel> availOpenEmployeeList;
    private ArrayList<EmployeeModel> availCloseEmployeeList;
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
        setContentView(R.layout.shift_day);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.dayBack);
        addopenbtn = (Button) findViewById(R.id.addOpening);
        addclosebtn = (Button) findViewById(R.id.addClosing);
        shiftdate = (EditText) findViewById(R.id.shiftDate);
        schedOpenRecyclerView = findViewById(R.id.scheduledOpeningEmployees);
        schedCloseRecyclerView = findViewById(R.id.scheduledClosingEmployees);
        availOpenRecyclerView = findViewById(R.id.availableOpeningEmployees);
        availCloseRecyclerView = findViewById(R.id.availableClosingEmployees);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);
        LocalDate localDate = LocalDate.parse(date);

        updateEmployeeList(localDate);
        buildAllRecyclerViews();

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftDay.this, ShiftCalendar.class);
                startActivity(myIntent);
            }
        });

        /* Assume that day objects and shift objects are already pre-created */
        //Button listener for Adding Opening Employees
        addopenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for the shift objects that have the date and shift type. (addShift) return the shift object
                //schedule an employee for that shift (scheduleEmployee) will update the database

                updateEmployeeList(localDate);
                buildAllRecyclerViews();
            }
        });
        //Button listener for Adding Closing Employees
        addclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateEmployeeList(localDate);
                buildAllRecyclerViews();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateEmployeeList(LocalDate localDate) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftDay.this);
        //dummy shift objects. It just needs the shift type and local date to get the query
        ShiftModel morningShift = new MorningShift(0, localDate, null, 0);
        ShiftModel eveningShift = new EveningShift(0, localDate, null, 0);
        availOpenEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "MORNING");
        availCloseEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "EVENING");
    }

    public void buildAllRecyclerViews() {
        buildRecyclerView(schedOpenRecyclerView, employeeList);
        buildRecyclerView(schedCloseRecyclerView, employeeList);
        buildRecyclerView(availOpenRecyclerView, availOpenEmployeeList);
        buildRecyclerView(availCloseRecyclerView, availCloseEmployeeList);
    }

    public void buildRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}