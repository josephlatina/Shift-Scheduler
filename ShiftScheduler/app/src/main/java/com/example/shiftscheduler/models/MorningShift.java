package com.example.shiftscheduler.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;

public class MorningShift extends ShiftModel implements Serializable {
    private final ShiftTime time = ShiftTime.MORNING;
    private final int icon = R.drawable.ic_morning;

    /**
     * Constructor.
     * @param shiftID    - will probably be auto-generated by future logic
     * @param date       - date of shift
     * @param employees  - set of assigned employees
     * @param employeesNeeded - maximum employees allowed
     */
    public MorningShift(int shiftID, LocalDate date, NavigableSet<EmployeeModel> employees,
                        int employeesNeeded) {
        super(shiftID, date, employees, employeesNeeded);
    }

    /**
     * @return itself
     */
    @Override
    public MorningShift toMorning() {
        return this;
    }

    /**
     * Verifies this morning shift's employees' qualifications according to specifications
     * @param database - DatabaseHelper object for the current session
     * @return verified
     */
    @Override
    protected ArrayList<ErrorModel> verifyEmployeeQualifications(DatabaseHelper database,
                                                                 ArrayList<ErrorModel> errors) {
        List<Boolean> employeeQualifications;
        boolean qualified = false;

        for (EmployeeModel employee : getEmployees()) {
            employeeQualifications = database.getQualifications(employee.getEmployeeID());
            if (!qualified && employeeQualifications.get(0)) { //employee is qualified to open
                qualified = true;
            }
        }

        if (!qualified) {
            errors.add(new ErrorModel(getDate(),
                    "MORNING SHIFT - No employees are qualified to open."));
        }

        return errors;
    }
}
