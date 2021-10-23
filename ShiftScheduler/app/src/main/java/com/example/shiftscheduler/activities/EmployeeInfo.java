package com.example.shiftscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shiftscheduler.R;

public class EmployeeInfo extends AppCompatActivity {

    public static final String EDITEMPLOYEE_ID = "com.example.shiftscheduler.activities.EDITEMPLOYEE_ID";

    EditText name, phoneNumber, email, streetAddress, dateOfBirth;
    Button editbtn;
    String empID;

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

        Intent intent = getIntent();
        name.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_NAME));
        phoneNumber.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_PHONE_NUMBER));
        email.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_EMAIL));
        streetAddress.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_ADDRESS));
        dateOfBirth.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_DOB));
        empID = intent.getStringExtra((EmployeeList.EMPLOYEE_ID));

        //Button listener for edit button
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmployeeInfo.this, EmployeeEditForm.class);
                myIntent.putExtra(EDITEMPLOYEE_ID, empID);
                startActivity(myIntent);
            }
        });
    }
}