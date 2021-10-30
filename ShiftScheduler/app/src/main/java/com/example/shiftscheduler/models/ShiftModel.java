package com.example.shiftscheduler.models;

import androidx.annotation.NonNull;

import com.example.shiftscheduler.R;

import java.time.LocalDate;
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
public abstract class ShiftModel {
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
    public ShiftModel(int shiftID, LocalDate date, NavigableSet<EmployeeModel> employees, int employeesNeeded) {
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
        return employees;
    }

    /**
     * Assigns an employee to this shift
     * @param employee - new EmployeeModel to be added
     * @return successful
     */
    public boolean addEmployee(EmployeeModel employee) {
        if (employees.size() < employeesNeeded) {
            return employees.add(employee);
        } else {
            return false;
        }
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
     * @param employeesNeeded - new maximum employees allowed
     */
    public void setEmployeesNeeded(int employeesNeeded) {
        this.employeesNeeded = employeesNeeded;
        // remove excess employees that no longer fit in this shift
        while (employees.size() > employeesNeeded) {
            employees.pollLast();
        }
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
     * @return verified
     */
    public boolean verifyShift() {
        // check if shift is full
        if (!(employees.size() == employeesNeeded)) {
            return false;
        }

        // check employee availability
        if (!this.verifyEmployeeAvailability()) {
            return false;
        }

        // check employee qualifications
        if (!this.verifyEmployeeQualifications()) {
            return false;
        }

        // shift passes verification
        return true;
    }

    /**
     * Verifies that all employees are available for this shift
     * (set in subclasses)
     * @return verified
     */
    protected abstract boolean verifyEmployeeAvailability();

    /**
     * Verifies employees' qualifications according to the specification
     * (set in subclasses)
     * @return verified
     */
    protected abstract boolean verifyEmployeeQualifications();
}
