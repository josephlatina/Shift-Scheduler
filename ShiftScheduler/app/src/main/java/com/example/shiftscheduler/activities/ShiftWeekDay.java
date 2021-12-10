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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ShiftWeekDay extends AppCompatActivity {
    //for passing info to employeeInfo
    public static final String EMPLOYEE_ID = "com.example.shiftscheduler.activities.EMPLOYEE_ID";
    public static final String EMPLOYEE_NAME = "com.example.shiftscheduler.activities.EMPLOYEE_NAME";
    public static final String ACTIVITY_PAGE = "com.example.shiftscheduler.activities.ACTIVITY_PAGE";
    public static final String SHIFT_DATE = "com.example.shiftscheduler.activities.SHIFT_DATE";

    //references to layout controls
    Button backbtn;
    EditText shiftdate;
    Switch repeat;
    //Recycler View Setup:
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
    LocalDate localDate;
    String date;
    MorningShift morningShift;
    EveningShift eveningShift;
    List<EmployeeModel> scheduledMorningEmployees;
    List<EmployeeModel> scheduledEveningEmployees;
    AlertDialog.Builder alertDialogBuilder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_weekday);

        //Link the layout controls
        backbtn = (Button) findViewById(R.id.dayBack);
        shiftdate = (EditText) findViewById(R.id.shiftDate);
        schedOpenRecyclerView = findViewById(R.id.scheduledOpeningEmployees);
        schedCloseRecyclerView = findViewById(R.id.scheduledClosingEmployees);
        availOpenRecyclerView = findViewById(R.id.availableOpeningEmployees);
        availCloseRecyclerView = findViewById(R.id.availableClosingEmployees);
        repeat = findViewById(R.id.weekDaySwitch);
        alertDialogBuilder = new AlertDialog.Builder(this);

        //receive intent
        Intent incomingIntent = getIntent();
        date = incomingIntent.getStringExtra(ShiftCalendar.SHIFT_DATE);
        shiftdate.setText(date);
        localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        /* Assume that day objects and shift objects are already pre-created */
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
        //**temp: just for testing purposes
        dbHelper.addShift(localDate, "MORNING");
        dbHelper.addShift(localDate, "EVENING");

        //Create shift models
        NavigableSet<EmployeeModel> morningEmployees = new TreeSet<>();                                         //create empty employee list
        NavigableSet<EmployeeModel> eveningEmployees = new TreeSet<>();
        int morningShiftID = dbHelper.getShiftID(localDate, "MORNING");                                    //create shift IDs
        int eveningShiftID = dbHelper.getShiftID(localDate, "EVENING");
        morningShift = new MorningShift(morningShiftID, localDate, morningEmployees, 2);          //create shift models
        eveningShift = new EveningShift(eveningShiftID, localDate, eveningEmployees, 2);

        //Build Recycler Views
        updateEmployeeList();
        buildAllRecyclerViews();

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            //Populate day object
            DayModel day = populateDay(localDate);
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftWeekDay.this, ShiftCalendar.class);
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
                    ArrayList<EmployeeModel> scheduledOpeners = (ArrayList) dbHelper.getScheduledEmployees(lastWeekDate, "MORNING");
                    ArrayList<EmployeeModel> scheduledClosers = (ArrayList) dbHelper.getScheduledEmployees(lastWeekDate, "EVENING");
                    //schedule them to the current date
                    for (EmployeeModel employee : scheduledOpeners) {
                        if (availOpenEmployeeList.contains(employee)) {
                            morningShift.addEmployee(employee);
                            dbHelper.scheduleEmployee(employee.getEmployeeID(), localDate, "MORNING");
                        }
                    }
                    for (EmployeeModel employee: scheduledClosers) {
                        if (availCloseEmployeeList.contains(employee)) {
                            eveningShift.addEmployee(employee);
                            dbHelper.scheduleEmployee(employee.getEmployeeID(), localDate, "EVENING");
                        }
                    }
                }
                //otherwise, clear the scheduled lists
                else {
                    //clear current scheduled employees
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
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
        availOpenEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "MORNING");
        availCloseEmployeeList = (ArrayList) dbHelper.getAvailableEmployees(localDate, "EVENING");
        schedOpenEmployeeList = (ArrayList) dbHelper.getScheduledEmployees(localDate, "MORNING");
        schedCloseEmployeeList = (ArrayList) dbHelper.getScheduledEmployees(localDate, "EVENING");
    }

    public void buildAllRecyclerViews() {
        buildAvailRecyclerView(availOpenRecyclerView, availOpenEmployeeList, "MORNING");
        buildAvailRecyclerView(availCloseRecyclerView, availCloseEmployeeList, "EVENING");
        buildSchedRecyclerView(schedOpenRecyclerView, schedOpenEmployeeList, "MORNING");
        buildSchedRecyclerView(schedCloseRecyclerView, schedCloseEmployeeList, "EVENING");
    }

    public void buildAvailRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList, String time) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList, 1);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEmployeeClick(int position) {
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                int count = 0;
                int shiftEmp;

                //Determine which employee selected by user
                EmployeeModel employee = employeeList.get(position);
                int empID = employee.getEmployeeID();

                //Add employee first to shift model
                if (time.equals("MORNING")) {
                    assignEmployee(empID, employee, morningShift, "MORNING");
                } else {
                    assignEmployee(empID, employee, eveningShift, "EVENING");
                }

                return;
            }
        });
    }

    public void buildSchedRecyclerView(RecyclerView recyclerView, ArrayList<EmployeeModel> employeeList, String time) {
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

                //call on the unassignEmployee method
                if (time.equals("MORNING")) {
                    unassignEmployee(empID, employee, morningShift, time);
                } else {
                    unassignEmployee(empID, employee, eveningShift, time);
                }

                return;
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void assignEmployee(int empID, EmployeeModel employee, ShiftModel givenShift, String time) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
        int count = 0, shiftEmp;

        //Add employee first to shift model
        givenShift.addEmployee(employee);
        /**
         //Call on verifyShift method
         ArrayList<ErrorModel> errors = morningShift.verifyShift(dbHelper);
         */
        //If there are more than 2 employees assigned, prompt alert message
        if (givenShift.getEmployees().size() > 2) {
            promptAlertMessage(1, dbHelper, empID, employee, time);
            return;
        }
        //If there are 2 employees assigned, check for qualified employees
        else if (givenShift.getEmployees().size() >= 2){
            //loop to check if there any employees qualified
            for (int i = 0; i < 2; i++) {
                shiftEmp = givenShift.getEmployees().stream().collect(Collectors.toList()).get(i).getEmployeeID();
                List<Boolean> qualifications = dbHelper.getQualifications(shiftEmp);
                if (time.equals("MORNING") && qualifications.get(0)) {
                    count += 1;
                }
                else if (time.equals("EVENING") && qualifications.get(1)) {
                    count += 1;
                }
            }
            //if none are, then prompt alert message
            if (count == 0) {
                promptAlertMessage(2, dbHelper, empID, employee, time);
                return;
            }
        }
        //Otherwise, proceed with scheduling the employee
        dbHelper.scheduleEmployee(empID, localDate, time);

        //update Recycler Views
        updateEmployeeList();
        buildAllRecyclerViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void unassignEmployee(int empID, EmployeeModel employee, ShiftModel givenShift, String time) {
        //Initialize variables
        int shiftEmp, count = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);

        givenShift.removeEmployee(employee);

        //check for qualifications
        if (givenShift.getEmployees().size() >= 2){
            //loop to check if there any employees qualified
            for (int i = 0; i < givenShift.getEmployees().size(); i++) {
                shiftEmp = givenShift.getEmployees().stream().collect(Collectors.toList()).get(i).getEmployeeID();
                List<Boolean> qualifications = dbHelper.getQualifications(shiftEmp);
                if (qualifications.get(0) && qualifications.get(1)) {
                    count += 1;
                }
            }
            //if none are, then prompt alert message
            if (count == 0) {
                promptAlertMessage(3, dbHelper, empID, employee, time);
                return;
            }
        }

        //Otherwise, proceed with descheduling employee
        dbHelper.descheduleEmployee(empID, localDate, time);

        //update Recycler Views
        updateEmployeeList();
        buildAllRecyclerViews();
    }

    public DayModel populateDay(LocalDate localDate) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);

        //Create day object and populate
        DayModel day = new DayModel(localDate, morningShift, eveningShift);

        return day;
    }

    public void promptAlertMessage(int code, DatabaseHelper dbHelper, int empID, EmployeeModel employee, String time) {
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
                            if (time.equals("MORNING")) {
                                morningShift.removeEmployee(employee);
                            } else {
                                eveningShift.removeEmployee(employee);
                            }

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
                            dbHelper.scheduleEmployee(empID, localDate, time);

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
                            if (time.equals("MORNING")) {
                                morningShift.addEmployee(employee);
                            } else {
                                eveningShift.addEmployee(employee);
                            }

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
                            dbHelper.descheduleEmployee(empID, localDate, time);

                            //update Recycler Views
                            updateEmployeeList();
                            buildAllRecyclerViews();

                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }

    public void clearAssignedEmployees(DatabaseHelper dbHelper) {
        //clear current scheduled employees
        dbHelper.clearScheduledEmployees(localDate, "MORNING");
        dbHelper.clearScheduledEmployees(localDate, "EVENING");
        //Remove employees from morning shift model
        scheduledMorningEmployees = new ArrayList<EmployeeModel>(morningShift.getEmployees());
        for (int i = 0; i < scheduledMorningEmployees.size(); i++) {
            morningShift.removeEmployee(scheduledMorningEmployees.get(i));
        }
        //Remove employees from evening shift model
        scheduledEveningEmployees = new ArrayList<EmployeeModel>(eveningShift.getEmployees());
        for (int i = 0; i < scheduledEveningEmployees.size(); i++) {
            eveningShift.removeEmployee(scheduledEveningEmployees.get(i));
        }
    }

}