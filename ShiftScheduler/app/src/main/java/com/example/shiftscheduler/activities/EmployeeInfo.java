package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.List;

public class EmployeeInfo extends AppCompatActivity {

    public static final String EDITEMPLOYEE_ID = "com.example.shiftscheduler.activities.EDITEMPLOYEE_ID";

    EditText name, phoneNumber, email, streetAddress, dateOfBirth;
    Button editbtn, backbtn;
    String empID;
    CheckBox opening, closing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_info);

        //Link the layout controls
        name = (EditText) findViewById(R.id.infoName);
        phoneNumber = (EditText) findViewById(R.id.infoPhone);
        email = (EditText) findViewById(R.id.infoEmail);
        streetAddress = (EditText) findViewById(R.id.infoAddress);
        dateOfBirth = (EditText) findViewById(R.id.infoDOB);
        editbtn = (Button) findViewById(R.id.infoEditButton);
        backbtn = (Button) findViewById(R.id.info_back);
        opening = (CheckBox) findViewById(R.id.openingCheckBox);
        closing = (CheckBox) findViewById(R.id.closingCheckBox);
        opening.setEnabled(false);
        closing.setEnabled(false);

        //Populate fields with employee information
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

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmployeeInfo.this, EmployeeList.class);
                startActivity(myIntent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        empID = intent.getStringExtra((EmployeeEditForm.EMPLOYEE_ID));
        updateProfile(empID);
        updateQualifications(empID);
    }

    public void updateProfile(String empID) {
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeInfo.this);
        EmployeeModel employee = dbHelper.getEmployee(Integer.parseInt(empID));

        //Populate fields with employee information
        name.setText(employee.getFName() + " " + employee.getLName());
        phoneNumber.setText(employee.getPhoneNum());
        email.setText(employee.getEmail());
        streetAddress.setText(employee.getStreet());
        dateOfBirth.setText(employee.getDOB());
    }

    public void updateQualifications(String empID) {
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeInfo.this);
        List<Boolean> qualifications = dbHelper.getQualifications(Integer.parseInt(empID));

        for (int i=0; i < 2; i++) {
            if (i==0) {
                if (qualifications.get(i) == true) {
                    opening.setChecked(true);
                } else { opening.setChecked(false); }
            }
            if (i==1) {
                if (qualifications.get(i) == true) {
                    closing.setChecked(true);
                } else { closing.setChecked(false); }
            }
        }
    }
}