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
        setContentView(R.layout.employee_editform);

        //Link the layout controls
        save_btn = findViewById(R.id.saveEdit);
        fname = findViewById(R.id.firstNameEdit);
        lname = findViewById(R.id.lastNameEdit);
        street = findViewById(R.id.streetAddressEdit);
        city = findViewById(R.id.cityEdit);
        province = findViewById(R.id.provinceEdit);
        postalCode = findViewById(R.id.postalCodeEdit);
        dob = findViewById(R.id.DOBEdit);
        phoneNum = findViewById(R.id.phoneNumberEdit);
        email = findViewById(R.id.emailEdit);
        activeEmployee = findViewById(R.id.isActiveEdit);
        opening = findViewById(R.id.openingCheckBoxEdit);
        closing = findViewById(R.id.closingCheckBoxEdit);

        //Retrieve employee information
        Intent intent = getIntent();
        empID = intent.getStringExtra((EmployeeInfo.EMPLOYEE_ID));

    }
}
