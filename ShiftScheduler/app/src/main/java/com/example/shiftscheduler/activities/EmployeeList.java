package com.example.shiftscheduler.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.ArrayList;

public class EmployeeList extends AppCompatActivity{
    //references to controls on the layout
    //Recycler View Setup:
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list);

        ArrayList<EmployeeModel> employeeList = new ArrayList<>(); //to be filled from db

        //example data:
        employeeList.add(new EmployeeModel(1,0,0,"John","Smith",
                "","","","","","", true));


        //Recycler View Setup:
        recyclerView = findViewById(R.id.employeeList_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
