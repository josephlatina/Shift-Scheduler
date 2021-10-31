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

import java.time.LocalDate;
import java.util.ArrayList;

public class ShiftWeekDay extends AppCompatActivity {

    //references to layout controls
    Button backbtn, addopenbtn, addclosebtn;
    EditText shiftdate;
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
                Intent myIntent = new Intent(ShiftWeekDay.this, ShiftCalendar.class);
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


                //update Recycler Views
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
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
        availOpenEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "MORNING");
        availCloseEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "EVENING");
        schedOpenEmployeeList = (ArrayList) dbHelper.getScheduledEmployees(localDate, "MORNING");
        schedCloseEmployeeList = (ArrayList) dbHelper.getScheduledEmployees(localDate, "EVENING");
    }

    public void buildAllRecyclerViews() {
        buildRecyclerView(availOpenRecyclerView, availOpenEmployeeList);
        buildRecyclerView(availCloseRecyclerView, availCloseEmployeeList);
        buildRecyclerView(schedOpenRecyclerView, schedOpenEmployeeList);
        buildRecyclerView(schedCloseRecyclerView, schedCloseEmployeeList);
    }

    public void buildRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(int position) {
                EmployeeModel employee = employeeList.get(position);
//                empID = String.valueOf(employee.getEmployeeID());
            }
        });
    }
}