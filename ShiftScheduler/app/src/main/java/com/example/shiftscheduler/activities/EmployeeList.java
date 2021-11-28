package com.example.shiftscheduler.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.CleanSort;
import com.example.shiftscheduler.models.EmployeeModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EmployeeList extends AppCompatActivity{
    //for passing info to employeeInfo
    public static final String EMPLOYEE_ID = "com.example.shiftscheduler.activities.EMPLOYEE_ID";
    public static final String EMPLOYEE_NAME = "com.example.shiftscheduler.activities.EMPLOYEE_NAME";
    public static final String EMPLOYEE_PHONE_NUMBER = "com.example.shiftscheduler.activities.PHONE_NUMBER";
    public static final String EMPLOYEE_EMAIL = "com.example.shiftscheduler.activities.EMPLOYEE_EMAIL";
    public static final String EMPLOYEE_ADDRESS = "com.example.shiftscheduler.activities.EMPLOYEE_ADDRESS";
    public static final String EMPLOYEE_DOB = "com.example.shiftscheduler.activities.EMPLOYEE_DOB";
    String empID;


    //references to controls on the layout
    //Recycler View Setup:
    private ArrayList<EmployeeModel> employeeList;
    private RecyclerView recyclerView;
    private EmployeeListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list);

        employeeList = new ArrayList<>();
        updateEmployeeList();
        buildRecyclerView();

        //Add button functionality
        Button buttonOpenEmployeeForm = (Button) findViewById(R.id.add_btn);
        buttonOpenEmployeeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmployeeList.this, EmployeeAddForm.class);
                startActivity(myIntent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        //sample data
        if(employeeList.size() == 0){
            EmployeeModel sample1 = new EmployeeModel(1, "Dan",
                    "Povemire", "New York", "99st", "Ontario","T8A8I9",
                    "june 19 1992", "815-223-9981","dan@povemire@gmail.com",
                    true);
            EmployeeModel sample2 = new EmployeeModel(2, "Orion",
                    "Sehn", "New York", "99st", "Ontario","T8A8I9",
                    "june 19 1996", "815-223-9981","orion.sehn@gmail.com",
                    true);
            EmployeeModel sample3 = new EmployeeModel(3, "Mr",
                    "Skeletal", "New York", "99st", "Ontario","T8A8I9",
                    "june 19 1992", "815-223-2341","dan@povemire@gmail.com",
                    true);
            EmployeeModel sample4 = new EmployeeModel(4, "John",
                    "Dorian", "New York", "99st", "Ontario","T8A8I9",
                    "june 19 1992", "815-223-9981","dan@povemire@gmail.com",
                    true);
            EmployeeModel sample5 = new EmployeeModel(5, "Jules",
                    "Verne", "New York", "99st", "Ontario","T8A8I9",
                    "june 19 1992", "815-223-9981","dan@povemire@gmail.com",
                    true);
            DatabaseHelper dbHelper = new DatabaseHelper(EmployeeList.this);
            dbHelper.addEmployee(sample1);
            dbHelper.addEmployee(sample2);
            dbHelper.addEmployee(sample3);
            dbHelper.addEmployee(sample4);
            dbHelper.addEmployee(sample5);
            updateEmployeeList();


            //sample data
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.employeeFragment:
                            break;
                        case R.id.settingsFragment:
                            break;
                        case R.id.scheduleFragment:
                            Intent scheudleIntent = new Intent(EmployeeList.this, ShiftCalendar.class);
                            startActivity(scheudleIntent);
                            break;
                    }
                    return false;
                }

            };

    public void onResume() {
        super.onResume();
        updateEmployeeList();
        buildRecyclerView();
    }

    public void updateEmployeeList() {
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeList.this);
        employeeList = (ArrayList) dbHelper.getEmployees();
    }

    public void buildRecyclerView() {
        Collections.sort(employeeList, new CleanSort());
        recyclerView = findViewById(R.id.employeeList_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList,0);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(int position) {
                EmployeeModel employee = employeeList.get(position);
                empID = String.valueOf(employee.getEmployeeID());
                String activityPage = "EMPLOYEE_LIST";
                //TBD: fill in logic to open Employee Info page & populate it.

                Intent myIntent = new Intent(EmployeeList.this, EmployeeInfo.class);
                myIntent.putExtra(EMPLOYEE_NAME, employee.getFName() + " " + employee.getLName());
                myIntent.putExtra(EMPLOYEE_PHONE_NUMBER, employee.getPhoneNum());
                myIntent.putExtra(EMPLOYEE_EMAIL, employee.getEmail());
                myIntent.putExtra(EMPLOYEE_ADDRESS, employee.getStreet());
                myIntent.putExtra(EMPLOYEE_DOB, employee.getDOB());
                myIntent.putExtra(EMPLOYEE_ID, empID);
                startActivity(myIntent);

                //temp:
//                Toast.makeText(EmployeeList.this, employee.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
