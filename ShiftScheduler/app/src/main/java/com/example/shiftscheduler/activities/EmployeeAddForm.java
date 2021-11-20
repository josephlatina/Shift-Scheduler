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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.Calendar;

public class EmployeeAddForm extends AppCompatActivity {
    //references to controls on the layout
    Button save_btn;
    EditText fname, lname, street, city, province, postalCode, dob, phoneNum, email;
    Switch activeEmployee;
    CheckBox opening, closing;
    CheckBox AvailMonMorn, AvailTuesMorn, AvailWedMorn, AvailThursMorn, AvailFriMorn, AvailSat, AvailSun;
    CheckBox AvailMonEven, AvailTuesEven, AvailWedEven, AvailThursEven, AvailFriEven;
    int open = 0, close = 0;
    int sunShift =0, monShift=0, tueShift=0, wedShift=0, thursShift=0, friShift=0, satShift=0;
    DatePickerDialog.OnDateSetListener dobListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_form);

        //Link the layout controls
        save_btn = findViewById(R.id.addSave);
        fname = findViewById(R.id.addFirstName);
        lname = findViewById(R.id.addLastName);
        street = findViewById(R.id.addStreetAddress);
        city = findViewById(R.id.addCity);
        province = findViewById(R.id.addProvince);
        postalCode = findViewById(R.id.addPostalCode);
        dob = findViewById(R.id.addDOB);
        phoneNum = findViewById(R.id.addPhoneNumber);
        email = findViewById(R.id.addEmail);
        opening = (CheckBox) findViewById(R.id.addQualOpeningCheckBox);
        closing = (CheckBox) findViewById(R.id.addQualClosingCheckBox);
        AvailMonMorn = (CheckBox) findViewById(R.id.addAvailMonMorn);
        AvailTuesMorn = (CheckBox) findViewById(R.id.addAvailTuesMorn);
        AvailWedMorn = (CheckBox) findViewById(R.id.addAvailWedMorn);
        AvailThursMorn = (CheckBox) findViewById(R.id.addAvailThursMorn);
        AvailFriMorn = (CheckBox) findViewById(R.id.addAvailFriMorn);
        AvailSat = (CheckBox) findViewById(R.id.addAvailSat);
        AvailSun = (CheckBox) findViewById(R.id.addAvailSun);
        AvailMonEven = (CheckBox) findViewById(R.id.addAvailMonEven);
        AvailTuesEven = (CheckBox) findViewById(R.id.addAvailTuesEven);
        AvailWedEven = (CheckBox) findViewById(R.id.addAvailWedEven);
        AvailThursEven = (CheckBox) findViewById(R.id.addAvailThursEven);
        AvailFriEven = (CheckBox) findViewById(R.id.addAvailFriEven);

        //Button listener for back
        ImageButton buttonOpenEmployeeForm = (ImageButton) findViewById(R.id.addBack);
        buttonOpenEmployeeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmployeeAddForm.this, EmployeeList.class);
                startActivity(myIntent);
            }
        });

        //Button listener for save button
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeModel employee;
                //Try creating an employee object
                try {
                    employee = new EmployeeModel(-1,
                            fname.getText().toString(), lname.getText().toString(),
                            city.getText().toString(), street.getText().toString(),
                            province.getText().toString(), postalCode.getText().toString(),
                            dob.getText().toString(), phoneNum.getText().toString(),
                            email.getText().toString(), true);
                }
                catch (Exception e) {
                    //If unsuccessful, generate error message and add in an error entry to know error was made
                    Toast.makeText(EmployeeAddForm.this, "Error creating employee.", Toast.LENGTH_SHORT).show();
                    employee = new EmployeeModel(-1,
                            "error", "error",
                            "error", "error",
                            "error", "error",
                            "error", "error", "error", false);
                }

                //Initialize database helper object
                DatabaseHelper dbHelper = new DatabaseHelper(EmployeeAddForm.this);
                //Use the addEntry method to insert the new employee object into the database
                // and store result in boolean variable
                boolean success = dbHelper.addEmployee(employee);
                if (opening.isChecked()) {
                    open = 1;
                }
                if (closing.isChecked()) {
                    close = 1;
                }
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
                dbHelper.updateQualification(employee.getEmployeeID(), open, close);
                //update Availability Table
                dbHelper.updateAvailability(employee.getEmployeeID(), sunShift, monShift,
                        tueShift, wedShift, thursShift, friShift, satShift);

                //Generate message indicating if insertion was a success or a failure
                Toast.makeText(EmployeeAddForm.this, "Success = " + success,
                        Toast.LENGTH_SHORT).show();


                //Go back to the EmployeeList button
                Intent myIntent = new Intent(EmployeeAddForm.this, EmployeeList.class);
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
