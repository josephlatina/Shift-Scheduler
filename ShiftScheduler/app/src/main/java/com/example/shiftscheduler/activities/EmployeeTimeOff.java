package com.example.shiftscheduler.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

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
    public static final String EMPLOYEE_NAME = "com.example.shiftscheduler.activities.EMPLOYEE_NAME";
    public static final String ACTIVITY_PAGE = "com.example.shiftscheduler.activities.ACTIVITY_PAGE";

    EditText name, dateTo, dateFrom;
    Button addbtn, calendarbtn;
    ImageButton backbtn;
    String empID, fullName;
    DatePickerDialog.OnDateSetListener fromDateListener, toDateListener;
    AlertDialog.Builder alertDialogBuilder;
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
        calendarbtn = findViewById(R.id.calendarButton);
        recyclerView = findViewById(R.id.timeOffList);
        alertDialogBuilder = new AlertDialog.Builder(this);

        //Receive Intent
        Intent intent = getIntent();
        fullName = intent.getStringExtra(EmployeeInfo.EMPLOYEE_NAME);
        name.setText(fullName);
        empID = intent.getStringExtra(EmployeeInfo.EDITEMPLOYEE_ID);

        //Build recycler view
        updateTimeoffList();
        buildRecyclerView();

        //Implement date listeners
        fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month+1) + "-";
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
                String date = year + "-" + (month+1) + "-";
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
                myIntent.putExtra(EMPLOYEE_NAME, fullName);
                myIntent.putExtra(ACTIVITY_PAGE, "EMPLOYEE_TIME_OFF");
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
                if (dateFrom.getText().toString().isEmpty() == true || dateTo.getText().toString().isEmpty() == true) {
                    promptErrorMessage(2);
                    return;
                }
                if (LocalDate.parse(dateFrom.getText().toString()).compareTo(LocalDate.parse(dateTo.getText().toString())) > 0) {
                    promptErrorMessage(1);
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(EmployeeTimeOff.this);
                if (dateFrom.getText().toString().isEmpty() == false && dateTo.getText().toString().isEmpty() == false) {
                    LocalDate fromLocalDate = LocalDate.parse(dateFrom.getText().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                    LocalDate toLocalDate = LocalDate.parse(dateTo.getText().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                    dbHelper.addTimeOff(Integer.parseInt(empID), fromLocalDate, toLocalDate);
                }

                updateTimeoffList();
                buildRecyclerView();
            }
        });

        //button listener for calendar
        calendarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(EmployeeTimeOff.this, ShiftCalendar.class);
                startActivity(myIntent);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume() {
        super.onResume();
        updateTimeoffList();
        buildRecyclerView();
    }

    private void promptErrorMessage(int code) {
        String details1 = "Starting date is greater than the Ending date.";
        String details2 = "Please populate empty fields.";
        String details3 = "This request has already been processed.";

        switch (code) {
            case 1: alertDialogBuilder.setTitle("Invalid dates");
                    alertDialogBuilder.setMessage(details1);
                    break;
            case 2: alertDialogBuilder.setTitle("Empty field");
                    alertDialogBuilder.setMessage(details2);
                    break;
            case 3: alertDialogBuilder.setTitle("Duplicate entry");
                    alertDialogBuilder.setMessage(details3);
                    break;
        }

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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

        //When you click on the delete button inside the recycler view
        adapter.setOnTimeoffClickListener(new TimeoffListAdapter.OnTimeoffClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeoffClick(int position) {
                TimeoffModel timeoff = timeoffList.get(position);
                int timeoffID = timeoff.getTimeoffID();

                DatabaseHelper dbHelper = new DatabaseHelper(EmployeeTimeOff.this);
                dbHelper.removeTimeOff(timeoffID);

                updateTimeoffList();
                buildRecyclerView();
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTimeoffList() {
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeTimeOff.this);
        timeoffList = (ArrayList) dbHelper.getTimeoffs(Integer.parseInt(empID));
    }

}