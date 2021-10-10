package com.example.shiftscheduler.activities;

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
    Button submit_btn;
    EditText fname, lname, street, city, province, postalCode, dob, phoneNum, qualifications;
    Switch activeEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.employee_form);

        //Link the layout controls
//        add_btn = findViewById(R.id.add_btn);
//        submit_btn = findViewById(R.id.submit_btn);
//        fname = findViewById(R.id.fname);
//        lname = findViewById(R.id.lname);
//        street = findViewById(R.id.street);
//        city = findViewById(R.id.city);
//        province = findViewById(R.id.province);
//        postalCode = findViewById(R.id.postalCode);
//        dob = findViewById(R.id.dob);
//        phoneNum = findViewById(R.id.phoneNum);
//        qualifications = findViewById(R.id.qualifications);

        //Button listener for submit button
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeModel employee;
                //Try creating an employee object
                try {
                    employee = new EmployeeModel(-1, -1,-1,
                            fname.getText().toString(), lname.getText().toString(),
                            city.getText().toString(), street.getText().toString(),
                            province.getText().toString(), postalCode.getText().toString(),
                            dob.getText().toString(), phoneNum.getText().toString());
                }
                catch (Exception e) {
                    //If unsuccessful, generate error message and add in an error entry to know error was made
                    Toast.makeText(EmployeeForm.this, "Error creating employee.", Toast.LENGTH_SHORT).show();
                    employee = new EmployeeModel(-1, -1,-1,
                            "error", "error",
                            "error", "error",
                            "error", "error",
                            "error", "error");
                }

                //Initialize database helper object
                DatabaseHelper dbHelper = new DatabaseHelper(EmployeeForm.this, null, null, 0);
                //Use the addEntry method to insert the new employee object into the database and store result in boolean variable
                boolean success = dbHelper.addEntry(employee);
                //Generate message indicating if insertion was a success or a failure
                Toast.makeText(EmployeeForm.this, "Success = " + success, Toast.LENGTH_SHORT).show();
            }
        });


    }


}
