package com.example.shiftscheduler.models;

import android.os.Build;
import android.provider.ContactsContract;

import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;

public class FullShift extends ShiftModel implements Serializable {
    private final ShiftTime time = ShiftTime.FULL;
    private final int icon = R.drawable.ic_full;

    /**
     * Constructor.
     * @param shiftID    - will probably be auto-generated by future logic
     * @param date       - date of shift
     * @param employees  - set of assigned employees
     * @param employeesNeeded - maximum employees allowed
     */
    public FullShift(int shiftID, LocalDate date, NavigableSet<EmployeeModel> employees,
                     int employeesNeeded) {
        super(shiftID, date, employees, employeesNeeded);
    }

    /**
     * @return itself
     */
    @Override
    public FullShift toFull() {
        return this;
    }

    /**
     * Verifies there are enough employees in the shift
     * @param errors - existing list of errors
     * @return errors found
     */
    public ArrayList<ErrorModel> verifyShiftSize(ArrayList<ErrorModel> errors) {
        // check if shift is full
        if (getEmployees().size() < getEmployeesNeeded()) {
            errors.add(new ErrorModel(getDate(),"FULL SHIFT: Not enough employees assigned."));
        }

        return errors;
    }

    /**
     * Verifies that all employees are available for this shift
     * @param database - DatabaseHelper object for the current session
     * @param errors - existing list of errors
     * @return errors found
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ErrorModel> verifyEmployeeAvailability(DatabaseHelper database,
                                                            ArrayList<ErrorModel> errors) {
        List<EmployeeModel> availableEmployees = database.getCurrentAvailableEmployees(getDate(), "FULL");
        for (EmployeeModel employee : getEmployees()) {
            if (!availableEmployees.contains(employee)) { //employee is not available
                errors.add(new ErrorModel(getDate(), "FULL SHIFT: "+employee.getFName()+" "+
                        employee.getLName()+" is scheduled but not available."));
            }
            else if (database.hasTimeOff(employee, getDate())) {
                errors.add(new ErrorModel(getDate(), "FULL SHIFT: "+employee.getFName()+" "+
                        employee.getLName()+" is scheduled but has a timeoff request for this day."));
            }
        }
        return errors;
    };

    /**
     * Verifies employees' qualifications.
     * @param database - DatabaseHelper object for the current session
     * @param errors - existing list of errors
     * @return errors found
     */
    public ArrayList<ErrorModel> verifyEmployeeQualifications(DatabaseHelper database,
                                                                 ArrayList<ErrorModel> errors) {
        boolean openQualified = false;
        boolean closeQualified = false;
        List<Boolean> employeeQualifications;

        for (EmployeeModel employee : getEmployees()) {
            employeeQualifications = database.getQualifications(employee.getEmployeeID());
            if (!openQualified) { //opening qualification check and lock
                openQualified = employeeQualifications.get(0);
            }
            if (!closeQualified) { //closing qualification check and lock
                closeQualified = employeeQualifications.get(1);
            }
        }

        if (!openQualified) {
            errors.add(new ErrorModel(getDate(),
                    "FULL SHIFT: No employees are qualified to open."));
        }
        if (!closeQualified) {
            errors.add(new ErrorModel(getDate(),
                    "FULL SHIFT: No employees are qualified to close."));
        }

        return errors;
    }
}
