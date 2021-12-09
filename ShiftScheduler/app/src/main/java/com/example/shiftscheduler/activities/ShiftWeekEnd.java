package com.example.shiftscheduler.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.DayModel;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.FullShift;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ShiftWeekEnd extends AppCompatActivity {

    public static final String SHIFT_DATE = "com.example.shiftscheduler.activities.SHIFT_DATE";

    //references to layout controls
    Button backbtn;
    EditText shiftdate;
    Switch repeat;
    //Recycler View Setup:
    private ArrayList<EmployeeModel> availFullDayEmployeeList;
    private ArrayList<EmployeeModel> schedFullDayEmployeeList;
    private RecyclerView schedFullDayRecyclerView;
    private RecyclerView availFullDayRecyclerView;
    private EmployeeListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    LocalDate localDate;
    FullShift fullShift;
    List<EmployeeModel> scheduledEmployees;
    AlertDialog.Builder alertDialogBuilder;

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
        repeat = findViewById(R.id.weekEndSwitch);
        alertDialogBuilder = new AlertDialog.Builder(this);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);
        localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        /* Assume that day objects and shift objects are already pre-created */
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
        //**temp: just for testing purposes
        dbHelper.addShift(localDate, "FULL");

        //Create shift model
        NavigableSet<EmployeeModel> fullEmployees = new TreeSet<>();
        int fullShiftID = dbHelper.getShiftID(localDate, "FULL");
        fullShift = new FullShift(fullShiftID, localDate, fullEmployees, 2);

        //Populate Recycler Views
        updateEmployeeList();
        buildAllRecyclerViews();

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            //Populate day object
            DayModel day = populateDay(localDate);
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftWeekEnd.this, ShiftCalendar.class);
                myIntent.putExtra(SHIFT_DATE, date);
                myIntent.putExtra("DayObject", day);
                startActivity(myIntent);
            }
        });

        //Switch listener for Repeat switch
        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //get the date from 7 days ago
                LocalDate lastWeekDate = localDate.minusDays(7);

                //if toggle is switch to "on", retrieve employees from last week
                if (b) {
                    //clear current scheduled employees
                    clearAssignedEmployees(dbHelper);
                    updateEmployeeList();
                    //get the scheduled employees from last week
                    ArrayList<EmployeeModel> scheduledWorkers = (ArrayList) dbHelper.getScheduledEmployees(lastWeekDate, "FULL");
                    //schedule them to the current date
                    for (EmployeeModel employee : scheduledWorkers) {
                        if (availFullDayEmployeeList.contains(employee)) {
                            fullShift.addEmployee(employee);
                            dbHelper.scheduleEmployee(employee.getEmployeeID(), localDate, "FULL");
                        }
                    }
                }
                //otherwise, clear the scheduled lists
                else {
                    clearAssignedEmployees(dbHelper);
                }

                //update Recycler Views
                updateEmployeeList();
                buildAllRecyclerViews();
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
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);
                int shiftEmp, count = 0;

                //Determine which employee selected by user
                EmployeeModel employee = employeeList.get(position);
                int empID = employee.getEmployeeID();

                //Add employee to shift model
                fullShift.addEmployee(employee);
                /**
                 //Call on verifyShift method
                 ArrayList<ErrorModel> errors = morningShift.verifyShift(dbHelper);
                 */
                //If there are more than 2 employees assigned, prompt alert message
                if (fullShift.getEmployees().size() > 2) {
                    promptAlertMessage(1, dbHelper, empID, employee);
                    return;
                }
                //If there are 2 employees assigned, check for qualified employees
                else if (fullShift.getEmployees().size() == 2){
                    //loop to check if there any employees qualified
                    for (int i = 0; i < 2; i++) {
                        shiftEmp = fullShift.getEmployees().stream().collect(Collectors.toList()).get(i).getEmployeeID();
                        List<Boolean> qualifications = dbHelper.getQualifications(shiftEmp);
                        if (qualifications.get(0) && qualifications.get(1)) {
                            count += 1;
                        }
                    }
                    //if none are, then prompt alert message
                    if (count == 0) {
                        promptAlertMessage(2, dbHelper, empID, employee);
                        return;
                    }
                }
                //Otherwise, proceed with scheduling the employee
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
                int shiftEmp = 0, count = 0;
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);

                fullShift.removeEmployee(employee);

                //check for qualifications
                if (fullShift.getEmployees().size() >= 2){
                    //loop to check if there any employees qualified
                    for (int i = 0; i < 2; i++) {
                        shiftEmp = fullShift.getEmployees().stream().collect(Collectors.toList()).get(i).getEmployeeID();
                        List<Boolean> qualifications = dbHelper.getQualifications(shiftEmp);
                        if (qualifications.get(0) && qualifications.get(1)) {
                            count += 1;
                        }
                    }
                    //if none are, then prompt alert message
                    if (count == 0) {
                        promptAlertMessage(3, dbHelper, empID, employee);
                        return;
                    }
                }
                //Otherwise, proceed with descheduling the employee
                dbHelper.descheduleEmployee(empID, localDate, "FULL");

                //update Recycler Views
                updateEmployeeList();
                buildAllRecyclerViews();
            }
        });
    }

    public void promptAlertMessage(int code, DatabaseHelper dbHelper, int empID, EmployeeModel employee) {
        //Initialization
        String details1 = "There would be more employees assigned than the normal employees needed. Do you want to continue?";
        String details2 = "None of the employees assigned are qualified for the shift. Do you want to continue?";
        String details3 = "Removing the employee would leave the shift with no qualified employees. Do you want to continue?";

        switch (code) {
            case 1: alertDialogBuilder.setTitle("Too many employees");
                alertDialogBuilder.setMessage(details1);
                break;
            case 2: alertDialogBuilder.setTitle("No qualified employees");
                alertDialogBuilder.setMessage(details2);
                break;
            case 3: alertDialogBuilder.setTitle("No qualified employees");
                alertDialogBuilder.setMessage(details3);
                break;
        }

        if (code == 1 || code == 2) {
            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            fullShift.removeEmployee(employee);

                            //update Recycler Views
                            updateEmployeeList();
                            buildAllRecyclerViews();

                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dbHelper.scheduleEmployee(empID, localDate, "FULL");

                            //update Recycler Views
                            updateEmployeeList();
                            buildAllRecyclerViews();

                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            fullShift.addEmployee(employee);

                            //update Recycler Views
                            updateEmployeeList();
                            buildAllRecyclerViews();

                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dbHelper.descheduleEmployee(empID, localDate, "FULL");

                            //update Recycler Views
                            updateEmployeeList();
                            buildAllRecyclerViews();

                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }

    public DayModel populateDay(LocalDate localDate) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekEnd.this);

        //Create day object and populate
        DayModel day = new DayModel(localDate, fullShift);

        return day;
    }

    public void clearAssignedEmployees(DatabaseHelper dbHelper) {
        //clear current scheduled employees
        dbHelper.clearScheduledEmployees(localDate, "FULL");
        //Remove employees
        scheduledEmployees = new ArrayList<EmployeeModel>(fullShift.getEmployees());
        for (int i = 0; i < scheduledEmployees.size(); i++) {
            fullShift.removeEmployee(scheduledEmployees.get(i));
        }
    }
}