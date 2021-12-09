package com.example.shiftscheduler.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.shiftscheduler.models.FullShift;
import com.example.shiftscheduler.models.MonthModel;
import com.example.shiftscheduler.models.MorningShift;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import com.whiteelephant.monthpicker.MonthPickerDialog;

public class ShiftCalendar extends AppCompatActivity {
    public static final String SHIFT_DATE = "com.example.shiftscheduler.activities.SHIFT_DATE";

    //references to layout controls
    CalendarView calendar;
    private BottomNavigationView bottomNavigationView;
    Button editSelectedDayBtn;
    Button exportBtn;
    Button errorBtn;
    EditText errorLabel;
    int selectedYear, selectedMonth, selectedDayOfMonth;
    AlertDialog.Builder alertDialogBuilder;
    //Recycler View Setup:
    private ArrayList<EmployeeModel> assignedEmployees;
    private ArrayList<ErrorModel> errorList;
    private RecyclerView employeeRecyclerView;
    private RecyclerView errorRecyclerView;
    private EmployeeListAdapter employeeListAdapter;
    private ErrorListAdapter errorListAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_calendar);

        //Link the layout controls
        employeeRecyclerView = findViewById(R.id.calSelectedEmployeeRecyclerView);
        errorRecyclerView = findViewById(R.id.calErrorRecyclerView);
        errorBtn = findViewById(R.id.errorButton);
        alertDialogBuilder = new AlertDialog.Builder(this);
        errorLabel = findViewById(R.id.errorLabel);

        //Update month model
//        updateErrorList();

        //editSelectedDay button
        editSelectedDayBtn = (Button) findViewById(R.id.calEditDay);
        LocalDate localDate = LocalDate.now();
        selectedYear = localDate.getYear();
        selectedMonth = localDate.getMonthValue();
        selectedDayOfMonth = localDate.getDayOfMonth();
        updateEditLabel(selectedYear, selectedMonth , selectedDayOfMonth);
        editSelectedDayBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //Determine what day of the week and send to its respective activity

                //Concatenate to convert date into string format
                String date = selectedYear + "-" + (selectedMonth) + "-";
                if (selectedDayOfMonth < 10) {
                    date += "0" + selectedDayOfMonth; //for formatting purposes
                } else {
                    date += selectedDayOfMonth;
                }
                LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

                int dayOfWeek = localDate.getDayOfWeek().getValue();

                //If It's a weekend, switch to the ShiftWeekEnd Activity. Otherwise, switch to ShiftWeekDay
                if (dayOfWeek == 6 || dayOfWeek == 7) {
                    Intent myIntent = new Intent(ShiftCalendar.this, ShiftWeekEnd.class);
                    myIntent.putExtra("date", date);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(ShiftCalendar.this, ShiftWeekDay.class);
                    myIntent.putExtra(SHIFT_DATE, date);
                    startActivity(myIntent);
                }
            }




        });




        //export button listener
        exportBtn = findViewById(R.id.calExport);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ShiftCalendar.this, ScheduleExport.class);
                startActivity(myIntent);
            }
        });
          
          
        //Calendar Logic
        calendar = (CalendarView) findViewById(R.id.calendarView);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month,
                                            int dayOfMonth) {

                DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);
                //update selected day
                selectedYear = year;
                selectedMonth = month+1;
                selectedDayOfMonth = dayOfMonth;
                LocalDate localDate = makeDate(year, month+1, dayOfMonth);

                //update edit button label
                updateEditLabel(year, month+1, dayOfMonth);

                //set button to be current selected date
                editSelectedDayBtn = (Button) findViewById(R.id.calEditDay);

                editSelectedDayBtn.setOnClickListener(new View.OnClickListener() {

                  @RequiresApi(api = Build.VERSION_CODES.O)
                  @Override
                  public void onClick(View v) {
                      //Determine what day of the week and send to its respective activity

                      //Concatenate to convert date into string format

                      int dayOfWeek = localDate.getDayOfWeek().getValue();

                      //If It's a weekend, switch to the ShiftWeekEnd Activity. Otherwise, switch to ShiftWeekDay
                      if (dayOfWeek == 6 || dayOfWeek == 7) {
                          Intent myIntent = new Intent(ShiftCalendar.this, ShiftWeekEnd.class);
                          myIntent.putExtra("date", localDate.toString());
                          startActivity(myIntent);
                      } else {
                          Intent myIntent = new Intent(ShiftCalendar.this, ShiftWeekDay.class);
                          myIntent.putExtra(SHIFT_DATE, localDate.toString());
                          startActivity(myIntent);
                      }
                  }
              });


                //update employees
                updateAssignedEmployeeList();
                buildEmployeeRecyclerView(employeeRecyclerView, localDate);
                //update errors
//                updateErrorList();
                DayModel currentDay = createDayObject(localDate);
                errorList = currentDay.verifyDay(dbHelper);
                buildErrorRecyclerView(errorRecyclerView, localDate);

            }
        });

        //Error Button Listener
        errorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Build Month Model and its respective errorlist
                DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);
                List<String> chosenMonthYear = new ArrayList<String>(Arrays.asList(errorLabel.getText().toString().split("-")));
                MonthModel currentMonth = createMonthObject(Integer.parseInt(chosenMonthYear.get(0)),Integer.parseInt(chosenMonthYear.get(1)));

                //Set up alert dialog for errors
                errorList = currentMonth.verifyMonth(dbHelper);
                String errorDays = "Days To Fix: \n";
                List<LocalDate> uniqueErrorList = errorList.stream().map(ErrorModel::getStartDate).distinct().collect(Collectors.toList());
                for (int i = 0; i < uniqueErrorList.size(); i++) {
                    errorDays += uniqueErrorList.get(i).toString();
                    errorDays += "\n";
                }
                errorDays += "\nEmployee Schedules To Fix: \n";
                List<String> uniqueErrorList2 = errorList.stream().map(ErrorModel::toWeeklyString).distinct().collect(Collectors.toList());
                for (int i = 0; i < uniqueErrorList2.size(); i++) {
                    if (uniqueErrorList2.get(i) != null) {
                        errorDays += uniqueErrorList2.get(i);
                        errorDays += "\n";
                    }
                }

                alertDialogBuilder.setTitle("Existing Errors");
                alertDialogBuilder.setMessage(errorDays);

                alertDialogBuilder.setCancelable(false)
                        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        }).create().show();

            }

        });

        //Select month to verify
        errorLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMonth(view);
            }
        });

        //if error list is empty, make error button invisible, otherwise the opposite.
//        if(errorList.size() == 0){
//            errorBtn.setEnabled(false);
//            errorBtn.setVisibility(View.INVISIBLE);
//            TextView errorLabel = findViewById(R.id.errorLabel);
//            errorLabel.setEnabled(false);
//            errorLabel.setVisibility(View.INVISIBLE);
//        }
//        else{
//            errorBtn.setEnabled(true);
//            errorBtn.setVisibility(View.VISIBLE);
//            TextView errorLabel = findViewById(R.id.errorLabel);
//            errorLabel.setEnabled(true);
//            errorLabel.setVisibility(View.VISIBLE);
//
//        }


        //navigationbar stuff
        bottomNavigationView = findViewById(R.id.cal_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume() {
        super.onResume();
        //receive intent
        Intent incomingIntent = getIntent();
        DayModel day = (DayModel) incomingIntent.getSerializableExtra("DayObject");

        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);

        //set by default the current selected day to current date
        LocalDate localDate = LocalDate.now();
        selectedYear = localDate.getYear();
        selectedMonth = localDate.getMonthValue();
        selectedDayOfMonth = localDate.getDayOfMonth();
        updateEditLabel(selectedYear, selectedMonth , selectedDayOfMonth);

        //set button to be current selected date
        editSelectedDayBtn = (Button) findViewById(R.id.calEditDay);

        //update employees
        updateAssignedEmployeeList();
        buildEmployeeRecyclerView(employeeRecyclerView, selectedLocalDate());
        //update errors
//        updateErrorList();
        DayModel currentDay = createDayObject(localDate);
        errorList = currentDay.verifyDay(dbHelper);
        buildErrorRecyclerView(errorRecyclerView, selectedLocalDate());
        //if error list is empty, make error button invisible, otherwise the opposite.
//        if(errorList.size() == 0){
//            errorBtn.setEnabled(false);
//            errorBtn.setVisibility(View.INVISIBLE);
//            TextView errorLabel = findViewById(R.id.errorLabel);
//            errorLabel.setEnabled(false);
//            errorLabel.setVisibility(View.INVISIBLE);
//        }
//        else{
//            errorBtn.setEnabled(true);
//            errorBtn.setVisibility(View.VISIBLE);
//            TextView errorLabel = findViewById(R.id.errorLabel);
//            errorLabel.setEnabled(true);
//            errorLabel.setVisibility(View.VISIBLE);
//
//        }

    }

    public void selectMonth(View view) {
        Calendar today = Calendar.getInstance();

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ShiftCalendar.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        // on date set
                        errorLabel.setText(Integer.toString(selectedMonth+1) + "-" + Integer.toString(selectedYear));
                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.NOVEMBER)
                .setMinYear(2020)
                .setActivatedYear(2021)
                .setMaxYear(2030)
                .setTitle("Select month year")
                .build().show();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateAssignedEmployeeList() {
        LocalDate selectedLocalDate = selectedLocalDate();
        int dayOfWeek = selectedLocalDate.getDayOfWeek().getValue();
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);

        //Check if weekday or weekend
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            assignedEmployees = (ArrayList<EmployeeModel>) dbHelper.getScheduledEmployees(selectedLocalDate, "FULL");
        } else {
            assignedEmployees = (ArrayList) dbHelper.getScheduledEmployees(selectedLocalDate,
                    "MORNING");
            ArrayList<EmployeeModel> scheduledClosers;
            scheduledClosers = (ArrayList) dbHelper.getScheduledEmployees(selectedLocalDate,
                    "EVENING");
            assignedEmployees.addAll(scheduledClosers);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateErrorList() {

        LocalDate localDate = LocalDate.now();
        selectedMonth = localDate.getMonthValue();
        selectedYear = localDate.getYear();

//        testing for whole month
        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);
        MonthModel curMonth = createMonthObject(selectedMonth, selectedYear);
        errorList = curMonth.verifyMonth(dbHelper);

        //testing for single day
//        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);
//        DayModel selectedDay = createDayObject(selectedLocalDate());
//        errorList = selectedDay.verifyDay(dbHelper);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateEditLabel(int year, int month, int dayOfMonth){
        //Concatenate to convert date into string format
        LocalDate localDate = makeDate(year, month, dayOfMonth);


        String newLabelDayOfWeek = localDate.getDayOfWeek().toString();
        newLabelDayOfWeek = newLabelDayOfWeek.substring(0,1).toUpperCase() +
                newLabelDayOfWeek.substring(1).toLowerCase();

        String newLabelMonth = localDate.getMonth().toString();
        newLabelMonth = newLabelMonth.substring(0,1).toUpperCase() +
                newLabelMonth.substring(1).toLowerCase();
        int newLabelDayOfMonth = localDate.getDayOfMonth();

        String newEditLabel = "Edit " + newLabelDayOfWeek +
                ", " + newLabelMonth + "." + newLabelDayOfMonth + " Schedule";

        editSelectedDayBtn.setText(newEditLabel);
    }

    /**
     * @param employeeRecyclerView
     * @param date
     * For building the employee Recycler view everytime a day is selected on the calendar view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildEmployeeRecyclerView(RecyclerView employeeRecyclerView, LocalDate date) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        updateAssignedEmployeeList();
        employeeRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        employeeListAdapter = new EmployeeListAdapter(assignedEmployees, dbHelper, date, 3);

        employeeRecyclerView.setLayoutManager(layoutManager);
        employeeRecyclerView.setAdapter(employeeListAdapter);
    }

    /**
     * @param errorRecyclerView
     * @param date
     * For building the error Recycler view everytime a day is selected on the calendar view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildErrorRecyclerView(RecyclerView errorRecyclerView, LocalDate date) {

        //update current error list
//        updateErrorList();
//
//        //filter out errors which apply to particular date
//        ArrayList<ErrorModel> curDateErrors;
//
//        curDateErrors = (ArrayList<ErrorModel>) errorList.stream()
//                .filter(error -> ((error.getEndDate()).compareTo(date)) >= 0)
//                .filter(error -> (error.getStartDate().compareTo(date)) <= 0)
//                .collect(Collectors.toList());

        //populate recyclerview
        errorRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        errorListAdapter = new ErrorListAdapter(errorList);
        errorRecyclerView.setLayoutManager(layoutManager);
        errorRecyclerView.setAdapter(errorListAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate makeDate(int year, int month, int dayOfMonth) {
        String date = year + "-";
        if (month < 10) {
            date += "0" + month + "-";
        } else {
            date += month + "-";
        }
        if (dayOfMonth < 10) {
            date += "0" + dayOfMonth; //for formatting purposes
        }
        else {
            date += dayOfMonth;
        }
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate selectedLocalDate() {
        return makeDate(selectedYear, selectedMonth, selectedDayOfMonth);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.employeeFragment:
                            Intent employeeIntent = new Intent(ShiftCalendar.this, EmployeeList.class);
                            startActivity(employeeIntent);
//                        case R.id.settingsFragment:
//                            break;
                        case R.id.scheduleFragment:
                            break;
                    }
                    return false;
                }

            };

    /**
     * Builds a specified MonthModel
     * @param month - value of month
     * @param year - specific year
     * @return MonthModel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public MonthModel createMonthObject(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        ArrayList<DayModel> days = new ArrayList<>();

        //iterate through month and fill in days
        LocalDate cursor = startDate;
        while (cursor.getMonthValue() == month) {
            days.add(createDayObject(cursor));
            cursor = cursor.plusDays(1);
        }

        return new MonthModel(startDate, days);
    }

    /**
     * Builds a specified DayModel
     * @param date - LocalDate of day
     * @return DayModel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public DayModel createDayObject(LocalDate date) {
        boolean isWeekend = (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                            date.getDayOfWeek() == DayOfWeek.SUNDAY);

        if (isWeekend) {
            return buildWeekEnd(date);
        } else {
            return buildWeekDay(date);
        }
    }

    /**
     * Builds a weekend DayModel
     * @param date - LocalDate of day
     * @return DayModel
     */
    public DayModel buildWeekEnd(LocalDate date) {
        //*** DON'T KNOW IF THIS IS CORRECT ***

        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);

        //Retrieve Shift IDs
        int fullShiftID = dbHelper.getShiftID(date, "FULL");

        //Create set of scheduled employees
        NavigableSet<EmployeeModel> employees = new TreeSet<>(dbHelper.getScheduledEmployees(date, "FULL"));

        //Create shift object
        FullShift fullShift = new FullShift(fullShiftID, date, employees, 2);

        //Create day object and populate
        return new DayModel(date, fullShift);
    }

    /**
     * Builds a weekday DayModel
     * @param date - LocalDate of day
     * @return DayModel
     */
    public DayModel buildWeekDay(LocalDate date) {
        //*** DON'T KNOW IF THIS IS CORRECT ***

        DatabaseHelper dbHelper = new DatabaseHelper(ShiftCalendar.this);

        //Retrieve Shift IDs
        int morningShiftID = dbHelper.getShiftID(date, "MORNING");
        int eveningShiftID = dbHelper.getShiftID(date, "EVENING");

        //Create set of scheduled employees for each shift
        NavigableSet<EmployeeModel> morningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(date, "MORNING"));
        NavigableSet<EmployeeModel> eveningEmployees = new TreeSet<>(dbHelper.getScheduledEmployees(date, "EVENING"));

        //Create shift objects for each respective shift for the day
        MorningShift morningShift = new MorningShift(morningShiftID, date, morningEmployees, 2);
        EveningShift eveningShift = new EveningShift(eveningShiftID, date, eveningEmployees, 2);

        //Create day object and populate
        return new DayModel(date, morningShift, eveningShift);
    }

    /**
     * Updates an existing MonthModel with a new DayModel
     * @param month - existing MonthModel
     * @param date - LocalDate of new day
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateMonthObject(MonthModel month, LocalDate date) {
        //build the new day object
        boolean isWeekend = (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                            date.getDayOfWeek() == DayOfWeek.SUNDAY);

        DayModel newDay;
        if (isWeekend) {
            newDay = buildWeekEnd(date);
        } else {
            newDay = buildWeekDay(date);
        }

        //update the month object
        month.updateDay(newDay);
    }
}