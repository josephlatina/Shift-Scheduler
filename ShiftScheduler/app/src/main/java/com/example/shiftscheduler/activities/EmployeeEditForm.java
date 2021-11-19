package com.example.shiftscheduler.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.AvailabilityModel;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.Calendar;
import java.util.List;

public class EmployeeEditForm extends AppCompatActivity {

    public static final String EMPLOYEE_ID = "com.example.shiftscheduler.activities.EMPLOYEE_ID";

    //references to controls on the layout
    Button save_btn;
    EditText fname, lname, street, city, province, postalCode, dob, phoneNum, email;
    Switch activeEmployee;
    CheckBox opening, closing;
    CheckBox AvailMonMorn, AvailTuesMorn, AvailWedMorn, AvailThursMorn, AvailFriMorn, AvailSat, AvailSun;
    CheckBox AvailMonEven, AvailTuesEven, AvailWedEven, AvailThursEven, AvailFriEven;
    ImageButton backbtn;
    String empID;
    int open = 0, close = 0;
    int sunShift =0, monShift=0, tueShift=0, wedShift=0, thursShift=0, friShift=0, satShift=0;
    DatePickerDialog.OnDateSetListener dobListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_editform);

        //Link the layout controls
        save_btn = (Button) findViewById(R.id.editSave);
        backbtn = (ImageButton) findViewById(R.id.editInfo_back);
        fname = (EditText) findViewById(R.id.editFirstName);
        lname = (EditText) findViewById(R.id.editLastName);
        street = (EditText) findViewById(R.id.editStreetAddress);
        city = (EditText) findViewById(R.id.editCity);
        province = (EditText) findViewById(R.id.editProvince);
        postalCode = (EditText) findViewById(R.id.editPostalCode);
        dob = (EditText) findViewById(R.id.editDOB);
        phoneNum = (EditText) findViewById(R.id.editPhoneNumber);
        email = (EditText) findViewById(R.id.editEmail);
        activeEmployee = (Switch) findViewById(R.id.editIsActive);
        opening = (CheckBox) findViewById(R.id.editQualOpeningCheckBox);
        closing = (CheckBox) findViewById(R.id.editQualClosingCheckBox);
        AvailMonMorn = (CheckBox) findViewById(R.id.editInfoAvailMonMorn);
        AvailTuesMorn = (CheckBox) findViewById(R.id.editInfoAvailTuesMorn);
        AvailWedMorn = (CheckBox) findViewById(R.id.editInfoAvailWedMorn);
        AvailThursMorn = (CheckBox) findViewById(R.id.editInfoAvailThursMorn);
        AvailFriMorn = (CheckBox) findViewById(R.id.editInfoAvailFriMorn);
        AvailSat = (CheckBox) findViewById(R.id.editInfoAvailSat);
        AvailSun = (CheckBox) findViewById(R.id.editInfoAvailSun);
        AvailMonEven = (CheckBox) findViewById(R.id.editInfoAvailMonEven);
        AvailTuesEven = (CheckBox) findViewById(R.id.editInfoAvailTuesEven);
        AvailWedEven = (CheckBox) findViewById(R.id.editInfoAvailWedEven);
        AvailThursEven = (CheckBox) findViewById(R.id.editInfoAvailThursEven);
        AvailFriEven = (CheckBox) findViewById(R.id.editInfoAvailFriEven);

        //Retrieve employee information
        Intent intent = getIntent();
        empID = intent.getStringExtra((EmployeeInfo.EDITEMPLOYEE_ID));
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeEditForm.this);
        EmployeeModel employee = dbHelper.getEmployee(Integer.parseInt(empID));
        List<Boolean> qualifications = dbHelper.getQualifications(Integer.parseInt(empID));
        AvailabilityModel avail = dbHelper.getAvailability(Integer.parseInt(empID));

        //Populate fields with employee information
        fname.setText(employee.getFName());
        lname.setText(employee.getLName());
        street.setText(employee.getStreet());
        city.setText(employee.getCity());
        province.setText(employee.getProvince());
        postalCode.setText(employee.getPostal());
        dob.setText(employee.getDOB());
        phoneNum.setText(employee.getPhoneNum());
        email.setText(employee.getEmail());
        if (employee.getStatus()) {
            activeEmployee.setChecked(true);
        }
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



        //Button listener for save button
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opening.isChecked()) {
                    open = 1;
                }
                if (closing.isChecked()) {
                    close = 1;
                }
                DatabaseHelper dbHelper = new DatabaseHelper(EmployeeEditForm.this);
                //update Qualifications Table
                dbHelper.updateQualification(Integer.parseInt(empID), open, close);

                // check the availability checkBox and set values that contains availability info
                if (AvailMonMorn.isChecked() && !AvailMonEven.isChecked()) monShift = 1;
                if (!AvailMonMorn.isChecked() && AvailMonEven.isChecked()) monShift = 2;
                if (AvailMonMorn.isChecked() && AvailMonEven.isChecked()) monShift = 3;
                if (AvailTuesMorn.isChecked() && !AvailTuesEven.isChecked()) tueShift = 1;
                if (!AvailTuesMorn.isChecked() && AvailTuesEven.isChecked()) tueShift = 2;
                if (AvailTuesMorn.isChecked() && AvailTuesEven.isChecked()) tueShift = 3;
                if (AvailWedMorn.isChecked() && !AvailWedEven.isChecked()) wedShift = 1;
                if (!AvailWedMorn.isChecked() && AvailWedEven.isChecked()) wedShift = 2;
                if (AvailWedMorn.isChecked() && AvailWedEven.isChecked()) wedShift = 3;
                if (AvailThursMorn.isChecked() && !AvailThursEven.isChecked()) thursShift = 1;
                if (!AvailThursMorn.isChecked() && AvailThursEven.isChecked()) thursShift = 2;
                if (AvailThursMorn.isChecked() && AvailThursEven.isChecked()) thursShift = 3;
                if (AvailFriMorn.isChecked() && !AvailFriEven.isChecked()) friShift = 1;
                if (!AvailFriMorn.isChecked() && AvailFriEven.isChecked()) friShift = 2;
                if (AvailFriMorn.isChecked() && AvailFriEven.isChecked()) friShift = 3;
                if (AvailSat.isChecked()) satShift = 1;
                if (AvailSun.isChecked()) sunShift = 1;

                //update Availability Table
                dbHelper.updateAvailability(Integer.parseInt(empID), sunShift, monShift, tueShift,
                        wedShift, thursShift, friShift, satShift);

                //update Employee Table
                boolean status = false;
                if (activeEmployee.isChecked()) {
                    status = true;
                }
                EmployeeModel modifiedEmployee = new EmployeeModel(Integer.parseInt(empID),
                        fname.getText().toString(), lname.getText().toString(),
                        city.getText().toString(), street.getText().toString(),
                        province.getText().toString(), postalCode.getText().toString(),
                        dob.getText().toString(), phoneNum.getText().toString(),
                        email.getText().toString(), status);
                dbHelper.updateEmployee(modifiedEmployee);

                Intent myIntent = new Intent(EmployeeEditForm.this, EmployeeInfo.class);
                myIntent.putExtra(EMPLOYEE_ID, empID);
                startActivity(myIntent);
            }
        });

        //Button listener for back
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmployeeEditForm.this, EmployeeInfo.class);
                myIntent.putExtra(EMPLOYEE_ID, empID);
                startActivity(myIntent);
            }
        });

        //Implement date listener
        dobListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month) + "-";
                if (dayOfMonth < 10) {
                    date += "0" + dayOfMonth; //for formatting purposes
                } else {
                    date += dayOfMonth;
                }

                dob.setText(date);
            }
        };

        //On click listener for dob
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dobListener);
            }
        });
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener dateListener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
