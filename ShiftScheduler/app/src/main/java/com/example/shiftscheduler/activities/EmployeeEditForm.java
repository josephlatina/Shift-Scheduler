package com.example.shiftscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.List;

public class EmployeeEditForm extends AppCompatActivity {

    public static final String EMPLOYEE_ID = "com.example.shiftscheduler.activities.EMPLOYEE_ID";

    //references to controls on the layout
    Button save_btn;
    EditText fname, lname, street, city, province, postalCode, dob, phoneNum, email;
    Switch activeEmployee;
    CheckBox opening, closing;
    String empID;
    int open = 0, close = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_editform);

        //Link the layout controls
        save_btn = (Button) findViewById(R.id.saveEdit);
        fname = (EditText) findViewById(R.id.firstNameEdit);
        lname = (EditText) findViewById(R.id.lastNameEdit);
        street = (EditText) findViewById(R.id.streetAddressEdit);
        city = (EditText) findViewById(R.id.cityEdit);
        province = (EditText) findViewById(R.id.provinceEdit);
        postalCode = (EditText) findViewById(R.id.postalCodeEdit);
        dob = (EditText) findViewById(R.id.DOBEdit);
        phoneNum = (EditText) findViewById(R.id.phoneNumberEdit);
        email = (EditText) findViewById(R.id.emailEdit);
        activeEmployee = (Switch) findViewById(R.id.isActiveEdit);
        opening = (CheckBox) findViewById(R.id.openingCheckBoxEdit);
        closing = (CheckBox) findViewById(R.id.closingCheckBoxEdit);

        //Retrieve employee information
        Intent intent = getIntent();
        empID = intent.getStringExtra((EmployeeInfo.EDITEMPLOYEE_ID));
        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeEditForm.this);
        EmployeeModel employee = dbHelper.getEmployee(Integer.parseInt(empID));
        List<Boolean> qualifications = dbHelper.getQualifications(Integer.parseInt(empID));

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

    }
}
