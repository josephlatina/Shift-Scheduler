package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        employeeList.add(new EmployeeModel(1,0,0,"Julius","Caesar",
                "","","","","",""));



        //Recycler View Setup:
        recyclerView = findViewById(R.id.employeeList_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EmployeeListAdapter(employeeList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


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


}
