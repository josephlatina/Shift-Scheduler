package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;

public class EmployeeEditForm extends AppCompatActivity {
    //references to controls on the layout
    Button save_btn;
    EditText fname, lname, street, city, province, postalCode, dob, phoneNum, email;
    Switch activeEmployee;
    CheckBox opening, closing;
    String empID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_editForm);

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
        email = findViewById(R.id.email);
        activeEmployee = findViewById(R.id.isActive);
        opening = findViewById(R.id.openingCheckBox);
        closing = findViewById(R.id.closingCheckBox);

        //Retrieve employee information
        Intent intent = getIntent();
        empID = intent.getStringExtra((EmployeeList.EMPLOYEE_ID));

    }
}
