package com.example.shiftscheduler.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.database.DatabaseHelper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for all the DayModels associated with a given month.
 */
public class MonthModel {
    private final LocalDate startDate;
    private final ArrayList<DayModel> days;
    private LocalDate firstSundayBefore;
    private LocalDate lastSaturdayAfter;

    /**
     * Constructor.
     * @param startDate - LocalDate of the first day of month
     * @param days - DayModels for each date in month
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public MonthModel(LocalDate startDate, ArrayList<DayModel> days) {
        this.startDate = startDate;
        this.days = days;

        this.firstSundayBefore = startDate;
        while (this.firstSundayBefore.getDayOfWeek() != DayOfWeek.SUNDAY) {
            this.firstSundayBefore = this.firstSundayBefore.minusDays(1);
        }

        this.lastSaturdayAfter = startDate.plusMonths(1).minusDays(1);
        while (this.lastSaturdayAfter.getDayOfWeek() != DayOfWeek.SATURDAY) {
            this.lastSaturdayAfter = this.lastSaturdayAfter.plusDays(1);
        }
    }

    /**
     * @return - LocalDate of the first day of month
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @return - ArrayList of DayModels in the month
     */
    public ArrayList<DayModel> getDays() {
        return days;
    }

    /**
     * @param date - LocalDate of requested DayModel
     * @return requested DayModel (null if not present)
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for .isEqual()
    public DayModel getDay(LocalDate date) {
        for (DayModel day : days) {
            if (day.getDate().isEqual(date)) return day;
        }
        return null;
    }

    /**
     * Update a day in the list with an updated version
     * @param newDay - new DayModel object with a date within this month
     * @return successful
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for getDay()
    public boolean updateDay(DayModel newDay) {
        DayModel oldDay = getDay(newDay.getDate());
        if (oldDay == null) return false; //non-existent day check

        days.set(days.indexOf(oldDay), newDay); //replace oldDay with newDay

        return true;
    }

    /**
     * Verifies this month according to specifications.
     * @param database - DatabaseHelper object for the current session
     * @param employees - ArrayList of all current working EmployeeModels
     * @return verified
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for LocalDate
    public ArrayList<ErrorModel> verifyMonth(DatabaseHelper database, List<EmployeeModel> employees) {
        ArrayList<ErrorModel> errors = new ArrayList<>();

        // verify all days individually
        for (DayModel day : days) {
            errors = day.verifyDay(database, errors);
        }

        // verify all employees work every week
        errors = verifyEmployeesWorkWeekly(database, employees, errors);

        // return all found errors
        return errors;
    }

    /**
     * Checks every employee to ensure each one works every week involved in this month.
     * @param database - DatabaseHelper object for the current session
     * @param employees - ArrayList of all current (active) employees
     * @param errors - existing list of errors
     * @return errors found
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for LocalDate
    private ArrayList<ErrorModel> verifyEmployeesWorkWeekly(DatabaseHelper database,
                                                            List<EmployeeModel> employees,
                                                            ArrayList<ErrorModel> errors) {

        //ensure every available employee works at least one shift on every week between
        //firstSundayBefore and lastSaturdayAfter
        for (EmployeeModel currentEmployee : employees) {
            LocalDate cursor = firstSundayBefore;
            boolean employeeWorks = false;

            while (!cursor.isEqual(lastSaturdayAfter.plusDays(1))) {
                //lock employeeWorks as true if it is ever true
                if (!employeeWorks) {
                    employeeWorks = database.isScheduled(currentEmployee, cursor);
                }

                if (cursor.getDayOfWeek() == DayOfWeek.SATURDAY) { //reaches end of week
                    if (!employeeWorks) { //employee hasn't worked this week
                        //check if employee is available:
                        boolean employeeAvailable = false;
                        for (int i = 6; i >= 0; i--) { //check for availability this week
                            //lock employeeAvailable as true if it is ever true
                            if (!employeeAvailable) {

                                //check if employee is available to work
                                if (database.getCurrentAvailableEmployees(cursor.minusDays(i),
                                                        "MORNING").contains(currentEmployee) ||
                                        database.getCurrentAvailableEmployees(cursor.minusDays(i),
                                                        "EVENING").contains(currentEmployee) ||
                                                database.getCurrentAvailableEmployees(cursor.minusDays(i),
                                                        "FULL").contains(currentEmployee)) {

                                    //check if employee doesn't have time off requested
                                    if (!database.hasTimeOff(currentEmployee, cursor.minusDays(i))) {
                                        employeeAvailable = true;
                                    }

                                }
                            }
                        }
                        //if employee is available and hasn't worked this week, create an error
                        if (employeeAvailable) {
                            String details = currentEmployee.getFName() +" "+
                                    currentEmployee.getLName() + " does not work this week.";
                            errors.add(new ErrorModel(cursor.minusDays(6), cursor, details));
                        }
                    }
                    //employee has worked (passes for this week)
                    employeeWorks = false; //reset employeeWorks for the next week
                }
                cursor = cursor.plusDays(1); //point cursor to the next day
            }
        }
        return errors;
    }
}
