package com.example.shiftscheduler.activities;

import com.example.shiftscheduler.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

public class EmployeeForm extends AppCompatActivity {
    //references to controls on the layout
    Button save_btn;
    EditText fname, lname, street, city, province, postalCode, dob, phoneNum;
    Switch activeEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_form);

        //Link the layout controls
        save_btn = findViewById(R.id.save);
        fname = findViewById(R.id.firstName);
        lname = findViewById(R.id.lastName);
        street = findViewById(R.id.streetAddress);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        postalCode = findViewById(R.id.postalCode);
        dob = findViewById(R.id.DOB);
        phoneNum = findViewById(R.id.phoneNumber);

        //Button listener for save button
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeModel employee;
                //Try creating an employee object
                try {
                    employee = new EmployeeModel(-1, -1,-1,
                            fname.getText().toString(), lname.getText().toString(),
                            city.getText().toString(), street.getText().toString(),
                            province.getText().toString(), postalCode.getText().toString(),
                            dob.getText().toString(), phoneNum.getText().toString(), true);
                }
                catch (Exception e) {
                    //If unsuccessful, generate error message and add in an error entry to know error was made
                    Toast.makeText(EmployeeForm.this, "Error creating employee.", Toast.LENGTH_SHORT).show();
                    employee = new EmployeeModel(-1, -1,-1,
                            "error", "error",
                            "error", "error",
                            "error", "error",
                            "error", "error", false);
                }

                //Initialize database helper object
                DatabaseHelper dbHelper = new DatabaseHelper(EmployeeForm.this);
                //Use the addEntry method to insert the new employee object into the database and store result in boolean variable
                boolean success = dbHelper.addEntry(employee);
                //Generate message indicating if insertion was a success or a failure
                Toast.makeText(EmployeeForm.this, "Success = " + success, Toast.LENGTH_SHORT).show();

                //Go back to the EmployeeList button
                Intent myIntent = new Intent(EmployeeForm.this, EmployeeList.class);
                startActivity(myIntent);
            }
        });


    }


}
