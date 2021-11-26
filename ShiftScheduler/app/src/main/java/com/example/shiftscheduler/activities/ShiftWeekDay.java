package com.example.shiftscheduler.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.DayModel;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.ErrorModel;
import com.example.shiftscheduler.models.EveningShift;
import com.example.shiftscheduler.models.MorningShift;
import com.example.shiftscheduler.models.ShiftModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ShiftWeekDay extends AppCompatActivity {

    //references to layout controls
    Button backbtn;
    EditText shiftdate;
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
    MorningShift morningShift;
    EveningShift eveningShift;
    AlertDialog.Builder alertDialogBuilder;
    int flag;

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
        alertDialogBuilder = new AlertDialog.Builder(this);

        //receive intent
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        shiftdate.setText(date);
        localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        updateEmployeeList();
        buildAllRecyclerViews();

        //Create shift models
        NavigableSet<EmployeeModel> morningEmployees = new TreeSet<>();
        NavigableSet<EmployeeModel> eveningEmployees = new TreeSet<>();
        morningShift = new MorningShift(0, localDate, morningEmployees, 4);
        eveningShift = new EveningShift(0, localDate, eveningEmployees, 4);

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            //Populate day object
            DayModel day = populateDay(localDate);
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftWeekDay.this, ShiftCalendar.class);
                myIntent.putExtra("date", date);
                myIntent.putExtra("DayObject", day);
                startActivity(myIntent);
            }
        });

        /* Assume that day objects and shift objects are already pre-created */
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
        //**temp: just for testing purposes
        dbHelper.addShift(localDate, "MORNING");
        dbHelper.addShift(localDate, "EVENING");

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
                boolean choice = true;

                //Determine which employee selected by user
                EmployeeModel employee = employeeList.get(position);
                int empID = employee.getEmployeeID();

                //Add employee first to shift model
                if (time.equals("MORNING")) {
                    morningShift.addEmployee(employee);
                } else {
                    eveningShift.addEmployee(employee);
                }
                /**
                 //Call on verifyShift method
                 ArrayList<ErrorModel> errors = morningShift.verifyShift(dbHelper);
                 //If no errors detected during verification process, add to database
                 if (errors.size() == 0) {
                 dbHelper.scheduleEmployee(empID, localDate, "MORNING");
                 }
                 */
                //If there are more than 2 employees assigned, prompt alert message
                if (morningShift.getEmployees().size() > 2 || eveningShift.getEmployees().size() > 2) {
                    promptAlertMessage(1, dbHelper, empID, employee, time);
                } else {
                    //if not, proceed with scheduling the employee
                    dbHelper.scheduleEmployee(empID, localDate, time);
                }
                //update Recycler Views
                updateEmployeeList();
                buildAllRecyclerViews();
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

                //Schedule the given employee to the type of shift provided in the specified day
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);
                if (time.equals("MORNING")) {
                    morningShift.removeEmployee(employee);
                    dbHelper.descheduleEmployee(empID, localDate, "MORNING");
                } else {
                    eveningShift.removeEmployee(employee);
                    dbHelper.descheduleEmployee(empID, localDate, "EVENING");
                }

                //update Recycler Views
                updateEmployeeList();
                buildAllRecyclerViews();

            }
        });
    }

    public DayModel populateDay(LocalDate localDate) {
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftWeekDay.this);

        //Retrieve Shift IDs
        int morningShiftID = dbHelper.getShiftID(localDate, "MORNING");
        int eveningShiftID = dbHelper.getShiftID(localDate, "EVENING");

        //Create set of scheduled employees for each shift
        NavigableSet<EmployeeModel> morningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(localDate, "MORNING"));
        NavigableSet<EmployeeModel> eveningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(localDate, "EVENING"));

        //Create shift objects for each respective shift for the day
        MorningShift morningShift = new MorningShift(morningShiftID, localDate, morningEmployees, 2);
        EveningShift eveningShift = new EveningShift(eveningShiftID, localDate, eveningEmployees, 2);

        //Create day object and populate
        DayModel day = new DayModel(localDate, morningShift, eveningShift);

        return day;
    }

    public void promptAlertMessage(int code, DatabaseHelper dbHelper, int empID, EmployeeModel employee, String time) {
        //Initialization
        String details1 = "There would be more employees assigned than the normal employees needed. Do you want to continue?";
        String details2 = "None of the employees assigned are qualified for the shift. Do you want to continue?";
        flag = 0;

        switch (code) {
            case 1: alertDialogBuilder.setTitle("Too many employees");
                    alertDialogBuilder.setMessage(details1);
                    break;
            case 2: alertDialogBuilder.setTitle("No qualified employees");
                    alertDialogBuilder.setMessage(details2);
                    break;
        }

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //If this button is clicked, close dialog box and do nothing
                        if (time.equals("MORNING")) {
                            morningShift.removeEmployee(employee);
                        } else {
                            eveningShift.removeEmployee(employee);
                        }
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dbHelper.scheduleEmployee(empID, localDate, time);
                        dialog.dismiss();

                        //update Recycler Views
                        updateEmployeeList();
                        buildAllRecyclerViews();
                    }
                }).create().show();

        return;
    }

}