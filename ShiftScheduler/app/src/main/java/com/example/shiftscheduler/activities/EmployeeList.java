package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.ArrayList;

public class EmployeeList extends AppCompatActivity{
    //for passing info to employeeInfo
    public static final String EMPLOYEE_ID = "com.example.shiftscheduler.activities.EMPLOYEE_NAME";
    public static final String EMPLOYEE_NAME = "com.example.shiftscheduler.activities.EMPLOYEE_NAME";
    public static final String EMPLOYEE_PHONE_NUMBER = "com.example.shiftscheduler.activities.PHONE_NUMBER";
    public static final String EMPLOYEE_EMAIL = "com.example.shiftscheduler.activities.EMPLOYEE_EMAIL";
    public static final String EMPLOYEE_ADDRESS = "com.example.shiftscheduler.activities.EMPLOYEE_ADDRESS";
    public static final String EMPLOYEE_DOB = "com.example.shiftscheduler.activities.EMPLOYEE_DOB";


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
    }

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
        recyclerView = findViewById(R.id.employeeList_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnEmployeeClickListener(new EmployeeListAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(int position) {
                EmployeeModel employee = employeeList.get(position);
                //TBD: fill in logic to open Employee Info page & populate it.

                Intent myIntent = new Intent(EmployeeList.this, EmployeeInfo.class);
                myIntent.putExtra(EMPLOYEE_NAME, employee.getFName() + " " + employee.getLName());
                myIntent.putExtra(EMPLOYEE_PHONE_NUMBER, employee.getPhoneNum());
                myIntent.putExtra(EMPLOYEE_EMAIL, employee.getEmail());
                myIntent.putExtra(EMPLOYEE_ADDRESS, employee.getStreet());
                myIntent.putExtra(EMPLOYEE_DOB, employee.getDOB());
                startActivity(myIntent);

                //temp:
//                Toast.makeText(EmployeeList.this, employee.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
