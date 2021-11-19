package com.example.shiftscheduler.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.TimeoffModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class EmployeeTimeOff extends AppCompatActivity {

    public static final String EMPLOYEE_ID = "com.example.shiftscheduler.activities.EDITEMPLOYEE_ID";

    EditText name, dateTo, dateFrom;
    Button addbtn;
    ImageButton backbtn;
    String empID;
    DatePickerDialog.OnDateSetListener fromDateListener, toDateListener;
    //Recycler View Setup:
    private ArrayList<TimeoffModel> timeoffList;
    private RecyclerView recyclerView;
    private TimeoffListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_timeoff);

        //references to layout controls
        name = findViewById(R.id.timeOffEmployee);
        dateTo = findViewById(R.id.editDateTo);
        dateFrom = findViewById(R.id.editDateFrom);
        backbtn = findViewById(R.id.timeOffBack);
        addbtn = findViewById(R.id.addTimeOff);
        recyclerView = findViewById(R.id.timeOffList);

        //Receive Intent
        Intent intent = getIntent();
        name.setText(intent.getStringExtra(EmployeeInfo.EMPLOYEE_NAME));
        empID = intent.getStringExtra(EmployeeInfo.EDITEMPLOYEE_ID);

        //Build recycler view
        updateTimeoffList();
        buildRecyclerView();

        //Implement date listeners
        fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month) + "-";
                if (dayOfMonth < 10) {
                    date += "0" + dayOfMonth; //for formatting purposes
                } else {
                    date += dayOfMonth;
                }

                dateFrom.setText(date);
            }
        };
        toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month) + "-";
                if (dayOfMonth < 10) {
                    date += "0" + dayOfMonth; //for formatting purposes
                } else {
                    date += dayOfMonth;
                }

                dateTo.setText(date);
            }
        };

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmployeeTimeOff.this, EmployeeInfo.class);
                myIntent.putExtra(EMPLOYEE_ID, empID);
                startActivity(myIntent);
            }
        });

        //On click listener for dateFrom
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(fromDateListener);
            }
        });
        //On click listener for dateTo
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(toDateListener);
            }
        });

        //Button listener for add
        addbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(EmployeeTimeOff.this);
                if (dateFrom.getText().toString().isEmpty() == false && dateTo.getText().toString().isEmpty() == false) {
                    LocalDate fromLocalDate = LocalDate.parse(dateFrom.getText().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                    LocalDate toLocalDate = LocalDate.parse(dateTo.getText().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                    dbHelper.addTimeOff(Integer.parseInt(empID), fromLocalDate, toLocalDate);
                }

                updateTimeoffList();
                buildRecyclerView();

                Toast.makeText(EmployeeTimeOff.this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume() {
        super.onResume();
        updateTimeoffList();
        buildRecyclerView();
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener dateListener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TimeoffListAdapter(timeoffList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTimeoffList() {
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeTimeOff.this);
        timeoffList = (ArrayList) dbHelper.getTimeoffs(Integer.parseInt(empID));
    }

}