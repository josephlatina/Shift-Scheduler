package com.example.shiftscheduler.models;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;

/**
 * Different shift times (unnecessary?)
 */
enum ShiftTime {
    MORNING,
    EVENING,
    FULL
}

/**
 * Represents a unit of work for a specific employee for a specific date/time.
 * @author Alex Cairns
 */
public abstract class ShiftModel implements Serializable {
    private final int shiftID;
    private final LocalDate date;
    private NavigableSet<EmployeeModel> employees;
    private int employeesNeeded;
    private final ShiftTime time = null;
    private final int icon = 0;

    /**
     * Constructor. (custom maxEmployeeCount)
     * @param shiftID - will probably be auto-generated by future logic
     * @param date - date of shift
     * @param employees - set of the assigned employee (set prevents duplicates)
     */
    public ShiftModel(int shiftID, LocalDate date, NavigableSet<EmployeeModel> employees,
                      int employeesNeeded) {
        this.shiftID = shiftID;
        this.date = date;
        this.employees = employees;
        this.employeesNeeded = employeesNeeded;
    }

    /**
     * @return String representation
     */
    @Override
    public String toString() {
        return "ShiftModel{" +
                "shiftID=" + shiftID +
                ", date=" + date +
                ", employees=" + employees +
                ", time=" + time +
                '}';
    }

    /**
     * Compares two Shift objects by their IDs
     * @param otherShift - shift being compared to
     * @return equality
     */
    public boolean equals(@NonNull ShiftModel otherShift) {
        return this.shiftID == otherShift.getShiftID();
    }

    /**
     * @return shift ID
     */
    public int getShiftID() {
        return shiftID;
    }

    /**
     * @return date of shift
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return set of assigned employees
     */
    public Set<EmployeeModel> getEmployees() {
        //return defensive copy
        Set<EmployeeModel> resultEmployees = new HashSet<>(employees);
        return employees;
    }

    /**
     * @return size of employee list
     */
    public int getEmployeeCount() {
        return employees.size();
    }

    /**
     * Assigns an employee to this shift
     * @param employee - new EmployeeModel to be added
     * @return successful
     */
    public boolean addEmployee(EmployeeModel employee) {
        return employees.add(employee);
    }

    /**
     * Removes an employee from this shift
     * @param employee - current EmployeeModel to be removed
     * @return successful
     */
    public boolean removeEmployee(EmployeeModel employee) {
        return employees.remove(employee);
    }

    /**
     * @return maximum number of employees allowed on this shift
     */
    public int getEmployeesNeeded() {
        return employeesNeeded;
    }

    /**
     * Changes maximum employee count
     * @param employeesNeeded - new amount of employees needed
     */
    public void setEmployeesNeeded(int employeesNeeded) {
        this.employeesNeeded = employeesNeeded;
    }

    /**
     * @return shift time (set by subclass)
     */
    public String getTime() {
        return time.toString();
    }

    /**
     * @return shift icon
     */
    public int getIcon() {
        return icon;
    }

    /**
     * @return current shift as a morning shift
     */
    public MorningShift toMorning() {
        return new MorningShift(shiftID, date, employees, employeesNeeded);
    }

    /**
     * @return current shift as an evening shift
     */
    public EveningShift toEvening() {
        return new EveningShift(shiftID, date, employees, employeesNeeded);
    }

    /**
     * @return current shift as a full day shift
     */
    public FullShift toFull() {
        return new FullShift(shiftID, date, employees, employeesNeeded);
    }

    /**
     * Evaluates whether the shift meets the specified criteria
     * @param database - DatabaseHelper object for the current session
     * @param errors - existing list of errors
     * @return verified
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ErrorModel> verifyShift(DatabaseHelper database, ArrayList<ErrorModel> errors) {
        // check if shift is full
        errors = verifyShiftSize(errors);

        // check employee availability
        errors = verifyEmployeeAvailability(database, errors);

        // check employee qualifications
        errors = verifyEmployeeQualifications(database, errors);

        // return all found errors
        return errors;
    }

    /**
     * Evaluates whether the shift meets the specified criteria
     * (without an existing list of errors)
     * @param database - DatabaseHelper object for the current session
     * @return errors found
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ErrorModel> verifyShift(DatabaseHelper database) {
        return verifyShift(database, new ArrayList<>());
    }

    /**
     * Verifies there are enough employees in the shift
     * @param errors - existing list of errors
     * @return errors found
     */
    public ArrayList<ErrorModel> verifyShiftSize(ArrayList<ErrorModel> errors) {
        // check if shift is full
        if (employees.size() < employeesNeeded) {
            errors.add(new ErrorModel(date,
                    getTime()+" SHIFT: Not enough employees assigned."));
        }

        return errors;
    }

    /**
     * Verifies there are enough employees in the shift
     * (without an existing list of errors)
     * @return errors found
     */
    public ArrayList<ErrorModel> verifyShiftSize() {
        return verifyShiftSize(new ArrayList<>());
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
        List<EmployeeModel> availableEmployees = database.getAvailableEmployees(date, getTime());
        for (EmployeeModel employee : getEmployees()) {
            if (!availableEmployees.contains(employee)) { //employee is not available
                errors.add(new ErrorModel(date, getTime()+" SHIFT - "+employee.getFName()+" "+
                        employee.getLName()+" is scheduled but not available."));
            }
        }
        return errors;
    };

    /**
     * Verifies that all employees are available for this shift
     * (without an existing list of errors)
     * @param database - DatabaseHelper object for the current session
     * @return errors found
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ErrorModel> verifyEmployeeAvailability(DatabaseHelper database) {
        return verifyEmployeeAvailability(database, new ArrayList<>());
    }

    /**
     * Verifies employees' qualifications according to the specification
     * (set in subclasses)
     * @param database - DatabaseHelper object for the current session
     * @param errors - existing list of errors
     * @return errors found
     */
    public abstract ArrayList<ErrorModel>
    verifyEmployeeQualifications(DatabaseHelper database, ArrayList<ErrorModel> errors);

    /**
     * Verifies employees' qualifications according to the specification
     * (without an existing list of errors)
     * @param database - DatabaseHelper object for the current session
     * @return errors found
     */
    public ArrayList<ErrorModel> verifyEmployeeQualifications(DatabaseHelper database) {
        return verifyEmployeeQualifications(database, new ArrayList<>());
    }
}

