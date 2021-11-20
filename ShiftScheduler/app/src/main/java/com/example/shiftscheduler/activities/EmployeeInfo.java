package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.AvailabilityModel;
import com.example.shiftscheduler.models.EmployeeModel;

import java.nio.file.attribute.AclFileAttributeView;
import java.util.List;

public class EmployeeInfo extends AppCompatActivity {

    public static final String EDITEMPLOYEE_ID = "com.example.shiftscheduler.activities.EDITEMPLOYEE_ID";
    public static final String EMPLOYEE_NAME = "com.example.shiftscheduler.activities.EMPLOYEE_NAME";

    EditText name, phoneNumber, email, streetAddress, dateOfBirth;
    Button editbtn;
    Button timeoffbtn;
    ImageButton backbtn;
    String empID, fullName;
    CheckBox opening, closing;
    CheckBox AvailMonMorn, AvailTuesMorn, AvailWedMorn, AvailThursMorn, AvailFriMorn, AvailSat, AvailSun;
    CheckBox AvailMonEven, AvailTuesEven, AvailWedEven, AvailThursEven, AvailFriEven;

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
        backbtn = (ImageButton) findViewById(R.id.info_back);
        opening = (CheckBox) findViewById(R.id.infoQualOpeningCheckBox);
        closing = (CheckBox) findViewById(R.id.infoQualClosingCheckBox);
        opening.setEnabled(false);
        closing.setEnabled(false);
        AvailMonMorn = (CheckBox) findViewById(R.id.infoAvailMonMorn);
        AvailTuesMorn = (CheckBox) findViewById(R.id.infoAvailTuesMorn);
        AvailWedMorn = (CheckBox) findViewById(R.id.infoAvailWedMorn);
        AvailThursMorn = (CheckBox) findViewById(R.id.infoAvailThursMorn);
        AvailFriMorn = (CheckBox) findViewById(R.id.infoAvailFriMorn);
        AvailSat = (CheckBox) findViewById(R.id.infoAvailSat);
        AvailSun = (CheckBox) findViewById(R.id.infoAvailSun);
        AvailMonEven = (CheckBox) findViewById(R.id.infoAvailMonEven);
        AvailTuesEven = (CheckBox) findViewById(R.id.infoAvailTuesEven);
        AvailWedEven = (CheckBox) findViewById(R.id.infoAvailWedEven);
        AvailThursEven = (CheckBox) findViewById(R.id.infoAvailThursEven);
        AvailFriEven = (CheckBox) findViewById(R.id.infoAvailFriEven);
        AvailMonMorn.setEnabled(false);
        AvailTuesMorn.setEnabled(false);
        AvailWedMorn.setEnabled(false);
        AvailThursMorn.setEnabled(false);
        AvailFriMorn.setEnabled(false);
        AvailMonEven.setEnabled(false);
        AvailTuesEven.setEnabled(false);
        AvailWedEven.setEnabled(false);
        AvailThursEven.setEnabled(false);
        AvailFriEven.setEnabled(false);
        AvailSat.setEnabled(false);
        AvailSun.setEnabled(false);
        timeoffbtn = (Button) findViewById(R.id.timeOffBtn);

        //Populate fields with employee information
        Intent intent = getIntent();
        fullName = intent.getStringExtra(EmployeeList.EMPLOYEE_NAME);
        name.setText(fullName);
        phoneNumber.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_PHONE_NUMBER));
        email.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_EMAIL));
        streetAddress.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_ADDRESS));
        dateOfBirth.setText(intent.getStringExtra(EmployeeList.EMPLOYEE_DOB));
        empID = intent.getStringExtra((EmployeeList.EMPLOYEE_ID));
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeInfo.this);
        AvailabilityModel avail = dbHelper.getAvailability(Integer.parseInt(empID));

        if (avail.getMonShift() == 1) {
            AvailMonMorn.setChecked(true);
            AvailMonEven.setChecked(false);
        }
        if (avail.getMonShift() == 2) {
            AvailMonMorn.setChecked(false);
            AvailMonEven.setChecked(true);
        }
        if (avail.getMonShift() == 3) {
            AvailMonMorn.setChecked(true);
            AvailMonEven.setChecked(true);
        }

        if (avail.getMonShift() < 1 || avail.getMonShift() > 3){
            AvailMonMorn.setChecked(false);
            AvailMonEven.setChecked(false);
        }

        // tue availability
        if (avail.getTueShift() == 1) {
            AvailTuesMorn.setChecked(true);
            AvailTuesEven.setChecked(false);
        }
        if (avail.getTueShift() == 2) {
            AvailTuesMorn.setChecked(false);
            AvailTuesEven.setChecked(true);
        }
        if (avail.getTueShift() == 3) {
            AvailTuesMorn.setChecked(true);
            AvailTuesEven.setChecked(true);
        }

        if (avail.getTueShift() < 1 || avail.getTueShift() > 3){
            AvailTuesMorn.setChecked(false);
            AvailTuesEven.setChecked(false);
        }

        //wed availability
        if (avail.getWedShift() == 1) {
            AvailWedMorn.setChecked(true);
            AvailWedEven.setChecked(false);
        }
        if (avail.getWedShift() == 2) {
            AvailWedMorn.setChecked(false);
            AvailWedEven.setChecked(true);
        }
        if (avail.getWedShift() == 3) {
            AvailWedMorn.setChecked(true);
            AvailWedEven.setChecked(true);
        }
        if (avail.getWedShift() < 1 || avail.getWedShift() > 3){
            AvailWedMorn.setChecked(false);
            AvailWedEven.setChecked(false);
        }

        // thurs availability
        if (avail.getThursShift() == 1) {
            AvailThursMorn.setChecked(true);
            AvailThursEven.setChecked(false);
        }
        if (avail.getThursShift() == 2) {
            AvailThursMorn.setChecked(false);
            AvailThursEven.setChecked(true);
        }
        if (avail.getThursShift() == 3) {
            AvailThursMorn.setChecked(true);
            AvailThursEven.setChecked(true);
        }
        if (avail.getThursShift() < 1 || avail.getThursShift() > 3){
            AvailThursMorn.setChecked(false);
            AvailThursEven.setChecked(false);
        }

        // fri availability
        if (avail.getFriShift() == 1) {
            AvailFriMorn.setChecked(true);
            AvailFriEven.setChecked(false);
        }
        if (avail.getFriShift() == 2) {
            AvailFriMorn.setChecked(false);
            AvailFriEven.setChecked(true);
        }
        if (avail.getFriShift() == 3) {
            AvailFriMorn.setChecked(true);
            AvailFriEven.setChecked(true);
        }
        if (avail.getFriShift() < 1 || avail.getFriShift() > 3){
            AvailFriMorn.setChecked(false);
            AvailFriEven.setChecked(false);
        }

        // weekend's availability
        if (avail.getSatShift() == 1) {
            AvailSat.setChecked(true);
        } else AvailSat.setChecked(false);

        if (avail.getSunShift() == 1) {
            AvailSun.setChecked(true);
        } else AvailSun.setChecked(false);

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

        //Button listener for time off button
        timeoffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmployeeInfo.this, EmployeeTimeOff.class);
                myIntent.putExtra(EMPLOYEE_NAME, fullName);
                myIntent.putExtra(EDITEMPLOYEE_ID, empID);
                startActivity(myIntent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        empID = intent.getStringExtra((EmployeeEditForm.EMPLOYEE_ID));
        //if activity it returned from is not EmployeeEditForm, then retrieve from EmployeeTimeOff
        if (empID == null) {
            empID = intent.getStringExtra(EmployeeTimeOff.EMPLOYEE_ID);
            fullName = intent.getStringExtra(EmployeeTimeOff.EMPLOYEE_NAME);
        }
        updateProfile(empID);
        updateQualifications(empID);
        updateAvailability(empID);
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

    public void updateAvailability(String empID) {
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeInfo.this);
        AvailabilityModel avail = dbHelper.getAvailability(Integer.parseInt(empID));

        // update each day's availability in a week
        // mon availability
        if (avail.getMonShift() == 1) {
            AvailMonMorn.setChecked(true);
            AvailMonEven.setChecked(false);
        }
        if (avail.getMonShift() == 2) {
            AvailMonMorn.setChecked(false);
            AvailMonEven.setChecked(true);
        }
        if (avail.getMonShift() == 3) {
            AvailMonMorn.setChecked(true);
            AvailMonEven.setChecked(true);
        }

        if (avail.getMonShift() < 1 || avail.getMonShift() > 3){
            AvailMonMorn.setChecked(false);
            AvailMonEven.setChecked(false);
        }

        // tue availability
        if (avail.getTueShift() == 1) {
            AvailTuesMorn.setChecked(true);
            AvailTuesEven.setChecked(false);
        }
        if (avail.getTueShift() == 2) {
            AvailTuesMorn.setChecked(false);
            AvailTuesEven.setChecked(true);
        }
        if (avail.getTueShift() == 3) {
            AvailTuesMorn.setChecked(true);
            AvailTuesEven.setChecked(true);
        }

        if (avail.getTueShift() < 1 || avail.getTueShift() > 3){
            AvailTuesMorn.setChecked(false);
            AvailTuesEven.setChecked(false);
        }

        //wed availability
        if (avail.getWedShift() == 1) {
            AvailWedMorn.setChecked(true);
            AvailWedEven.setChecked(false);
        }
        if (avail.getWedShift() == 2) {
            AvailWedMorn.setChecked(false);
            AvailWedEven.setChecked(true);
        }
        if (avail.getWedShift() == 3) {
            AvailWedMorn.setChecked(true);
            AvailWedEven.setChecked(true);
        }
        if (avail.getWedShift() < 1 || avail.getWedShift() > 3){
            AvailWedMorn.setChecked(false);
            AvailWedEven.setChecked(false);
        }

        // thurs availability
        if (avail.getThursShift() == 1) {
            AvailThursMorn.setChecked(true);
            AvailThursEven.setChecked(false);
        }
        if (avail.getThursShift() == 2) {
            AvailThursMorn.setChecked(false);
            AvailThursEven.setChecked(true);
        }
        if (avail.getThursShift() == 3) {
            AvailThursMorn.setChecked(true);
            AvailThursEven.setChecked(true);
        }
        if (avail.getThursShift() < 1 || avail.getThursShift() > 3){
            AvailThursMorn.setChecked(false);
            AvailThursEven.setChecked(false);
        }

        // fri availability
        if (avail.getFriShift() == 1) {
            AvailFriMorn.setChecked(true);
            AvailFriEven.setChecked(false);
        }
        if (avail.getFriShift() == 2) {
            AvailFriMorn.setChecked(false);
            AvailFriEven.setChecked(true);
        }
        if (avail.getFriShift() == 3) {
            AvailFriMorn.setChecked(true);
            AvailFriEven.setChecked(true);
        }
        if (avail.getFriShift() < 1 || avail.getFriShift() > 3){
            AvailFriMorn.setChecked(false);
            AvailFriEven.setChecked(false);
        }

        // weekend's availability
        if (avail.getSatShift() == 1) {
            AvailSat.setChecked(true);
        } else AvailSat.setChecked(false);

        if (avail.getSunShift() == 1) {
            AvailSun.setChecked(true);
        } else AvailSun.setChecked(false);


    }
}