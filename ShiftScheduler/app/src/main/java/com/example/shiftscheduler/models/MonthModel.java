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

    /**
     * Constructor.
     * @param startDate - LocalDate of the first day of month
     * @param days - DayModels for each date in month
     */
    public MonthModel(LocalDate startDate, ArrayList<DayModel> days) {
        this.startDate = startDate;
        this.days = days;
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
            if (day.getDate().isEqual(date)) {
                return day;
            }
        }
        return null;
    }

    /**
     * Verifies this month according to specifications.
     * @param database - DatabaseHelper object for the current session
     * @param employees - ArrayList of all current working EmployeeModels
     * @return verified
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for LocalDate
    public boolean verifyMonth(DatabaseHelper database, List<EmployeeModel> employees) {
        // verify all days individually
        for (DayModel day : days) {
            if (!day.verifyDay(database)) {
                return false;
            }
        }

        // verify all employees work every week
        if (!verifyEmployeesWorkWeekly(database, employees)) {
            return false;
        }

        // month has passed verification
        return true;
    }

    /**
     * Checks every employee to ensure each one works every week involved in this month.
     * @param database - DatabaseHelper object for the current session
     * @param employees - ArrayList of all current (active) employees
     * @return verified
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for LocalDate
    private boolean verifyEmployeesWorkWeekly(DatabaseHelper database, List<EmployeeModel> employees){
        // 1. find the Sunday at/before startDate
        LocalDate firstSunday = startDate;
        while (firstSunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            firstSunday = firstSunday.minusDays(1);
        }

        // 2. find the Saturday at/after the last day of the month
        LocalDate lastSaturday = startDate.plusMonths(1).minusDays(1);
        while (lastSaturday.getDayOfWeek() != DayOfWeek.SATURDAY) {
            lastSaturday = lastSaturday.plusDays(1);
        }

        // 3. ensure every available employee works at least one shift on every week between them
        for (EmployeeModel currentEmployee : employees) {
            LocalDate cursor = firstSunday;
            boolean employeeWorks = false;

            while (!cursor.isEqual(lastSaturday.plusDays(1))) {
                if (!employeeWorks) { //locks employeeWorks as true if it is ever true
                    employeeWorks = this.getDay(cursor).containsEmployee(currentEmployee);
                }

                if (cursor.getDayOfWeek() == DayOfWeek.SATURDAY) { //reaches end of week
                    if (!employeeWorks) { //employee hasn't worked this week
                        boolean employeeAvailable = false;
                        for (int i = 6; i >= 0; i--) { //check for availability this week
                            if (!employeeAvailable) {
                                employeeAvailable =
                                        database.getAvailableEmployees(cursor.minusDays(i), "MORNING").contains(currentEmployee) ||
                                                database.getAvailableEmployees(cursor.minusDays(i), "EVENING").contains(currentEmployee) ||
                                                database.getAvailableEmployees(cursor.minusDays(i), "FULL").contains(currentEmployee);
                            }
                        }
                        if (employeeAvailable) { //employee is available and hasn't worked this week
                            return false;
                        }
                    }
                    //employee has worked (passes for this week)
                    employeeWorks = false; //reset employeeWorks for the next week
                }

                cursor = cursor.plusDays(1); //point cursor to the next day
            }
        }
        return true;
    }
}
