package com.example.shiftscheduler.models;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;

public class EveningShift extends ShiftModel {
    private final ShiftTime time = ShiftTime.EVENING;
    private final int icon = R.drawable.ic_evening;

    /**
     * Constructor.
     * @param shiftID    - will probably be auto-generated by future logic
     * @param date       - date of shift
     * @param employees  - set of assigned employees
     * @param employeesNeeded - maximum employees allowed
     */
    public EveningShift(int shiftID, LocalDate date, NavigableSet<EmployeeModel> employees, int employeesNeeded) {
        super(shiftID, date, employees, employeesNeeded);
    }

    /**
     * @return itself
     */
    @Override
    public EveningShift toEvening() {
        return this;
    }

    /**
     * Verifies this evening shift's employees' qualifications according to specifications
     * @param database - DatabaseHelper object for the current session
     * @return verified
     */
    @Override
    protected boolean verifyEmployeeQualifications(DatabaseHelper database) {
        List<Boolean> employeeQualifications;
        for (EmployeeModel employee : getEmployees()) {
            employeeQualifications = database.getQualifications(employee.getEmployeeID());
            if (employeeQualifications.get(1)) { //employee is qualified to close
                return true;
            }
        }
        return false;
    }
}
