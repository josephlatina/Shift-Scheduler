package com.example.shiftscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.shiftscheduler.R;

public class EmployeeInfo extends AppCompatActivity {

    EditText name, phoneNumber, email, streetAddress, dateOfBirth;
    Button editbtn, deletebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_info);

        name = (EditText) findViewById(R.id.infoName);
        phoneNumber = (EditText) findViewById(R.id.infoPhone);
        email = (EditText) findViewById(R.id.infoEmail);
        streetAddress = (EditText) findViewById(R.id.infoAddress);
        dateOfBirth = (EditText) findViewById(R.id.infoDOB);
        editbtn = (Button) findViewById(R.id.infoEditButton);
        deletebtn = (Button) findViewById(R.id.infoDeleteButton);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_NAME));
        phoneNumber.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_PHONE_NUMBER));
        email.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_EMAIL));
        streetAddress.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_ADDRESS));
        dateOfBirth.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_DOB));

    }
}