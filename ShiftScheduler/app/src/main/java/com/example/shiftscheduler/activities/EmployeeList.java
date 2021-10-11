package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.ArrayList;

public class EmployeeList extends AppCompatActivity{
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
                Intent myIntent = new Intent(EmployeeList.this, EmployeeForm.class);
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
        employeeList = (ArrayList) dbHelper.getEveryone();
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

                //temp:
                Toast.makeText(EmployeeList.this, employee.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
