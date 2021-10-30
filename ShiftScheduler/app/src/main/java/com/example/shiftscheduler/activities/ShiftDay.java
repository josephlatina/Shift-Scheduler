package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.ArrayList;

public class ShiftDay extends AppCompatActivity {

    //references to layout controls
    Button backbtn;
    EditText shiftdate;
    //Recycler View Setup:
    private ArrayList<EmployeeModel> employeeList;
    private RecyclerView schedOpenRecyclerView;
    private RecyclerView schedCloseRecyclerView;
    private RecyclerView availOpenRecyclerView;
    private RecyclerView availCloseRecyclerView;
    private EmployeeListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_day);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.dayBack);
        shiftdate = (EditText) findViewById(R.id.shiftDate);
        schedOpenRecyclerView = findViewById(R.id.scheduledOpeningEmployees);
        schedCloseRecyclerView = findViewById(R.id.scheduledClosingEmployees);
        availOpenRecyclerView = findViewById(R.id.availableOpeningEmployees);
        availCloseRecyclerView = findViewById(R.id.availableClosingEmployees);

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

        buildAllRecyclerViews();

    }

    public void updateEmployeeList() {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftDay.this);
        employeeList = (ArrayList) dbHelper.getEmployees();
    }

    public void buildAllRecyclerViews() {
        buildRecyclerView(schedOpenRecyclerView, employeeList);
        buildRecyclerView(schedCloseRecyclerView, employeeList);
        buildRecyclerView(availOpenRecyclerView, employeeList);
        buildRecyclerView(availCloseRecyclerView, employeeList);
    }

    public void buildRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}