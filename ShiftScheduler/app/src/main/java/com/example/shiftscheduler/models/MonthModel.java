package com.example.shiftscheduler.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.database.DatabaseHelper;

import java.lang.reflect.Array;
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
    private ArrayList<LocalDate> sundays = new ArrayList<>();

    /**
     * Constructor.
     * @param startDate - LocalDate of the first day of month
     * @param days - DayModels for each date in month
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public MonthModel(LocalDate startDate, ArrayList<DayModel> days) {
        this.startDate = startDate;
        this.days = days;

        LocalDate firstSundayBefore = startDate;
        switch (startDate.getDayOfWeek()) {
            case SUNDAY:
                break;
            case MONDAY:
                firstSundayBefore = startDate.minusDays(1); break;
            case TUESDAY:
                firstSundayBefore = startDate.minusDays(2); break;
            case WEDNESDAY:
                firstSundayBefore = startDate.minusDays(3); break;
            case THURSDAY:
                firstSundayBefore = startDate.minusDays(4); break;
            case FRIDAY:
                firstSundayBefore = startDate.minusDays(5); break;
            case SATURDAY:
                firstSundayBefore = startDate.minusDays(6); break;
        }

        sundays.add(firstSundayBefore);
        int weekCount = 1;
        int monthValue = startDate.getMonthValue();
        while (firstSundayBefore.plusWeeks(weekCount).getMonthValue() == monthValue) {
            sundays.add(firstSundayBefore.plusWeeks(weekCount));
            weekCount++;
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
     * @return verified
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for LocalDate
    public ArrayList<ErrorModel> verifyMonth(DatabaseHelper database) {
        ArrayList<ErrorModel> errors = new ArrayList<>();

        // verify all days individually
        for (DayModel day : days) {
            errors = day.verifyDay(database, errors);
        }

        // verify all employees work every week
        errors = verifyEmployeesWorkWeekly(database, errors);

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
//    @RequiresApi(api = Build.VERSION_CODES.O) //for LocalDate
//    private ArrayList<ErrorModel> verifyEmployeesWorkWeekly(DatabaseHelper database,
//                                                            List<EmployeeModel> employees,
//                                                            ArrayList<ErrorModel> errors) {
//
//        //ensure every available employee works at least one shift on every week between
//        //firstSundayBefore and lastSaturdayAfter
//        for (EmployeeModel currentEmployee : employees) {
//            LocalDate cursor = firstSundayBefore;
//            boolean employeeWorks = false;
//
//            while (!cursor.isEqual(lastSaturdayAfter.plusDays(1))) {
//                //lock employeeWorks as true if it is ever true
//                if (!employeeWorks) {
//                    employeeWorks = database.isScheduled(currentEmployee, cursor);
//                }
//
//                if (cursor.getDayOfWeek() == DayOfWeek.SATURDAY) { //reaches end of week
//                    if (!employeeWorks) { //employee hasn't worked this week
//                        //check if employee is available:
//                        boolean employeeAvailable = false;
//                        for (int i = 6; i >= 0; i--) { //check for availability this week
//                            //lock employeeAvailable as true if it is ever true
//                            if (!employeeAvailable) {
//
//                                //check if employee is available to work
//                                if (database.getCurrentAvailableEmployees(cursor.minusDays(i),
//                                                        "MORNING").contains(currentEmployee) ||
//                                        database.getCurrentAvailableEmployees(cursor.minusDays(i),
//                                                        "EVENING").contains(currentEmployee) ||
//                                                database.getCurrentAvailableEmployees(cursor.minusDays(i),
//                                                        "FULL").contains(currentEmployee)) {
//
//                                    //check if employee doesn't have time off requested
//                                    if (!database.hasTimeOff(currentEmployee, cursor.minusDays(i))) {
//                                        employeeAvailable = true;
//                                    }
//
//                                }
//                            }
//                        }
//                        //if employee is available and hasn't worked this week, create an error
//                        if (employeeAvailable) {
//                            String details = currentEmployee.getFName() +" "+
//                                    currentEmployee.getLName() + " does not work this week.";
//                            errors.add(new ErrorModel(cursor.minusDays(6), cursor, details));
//                        }
//                    }
//                    //employee has worked (passes for this week)
//                    employeeWorks = false; //reset employeeWorks for the next week
//                }
//                cursor = cursor.plusDays(1); //point cursor to the next day
//            }
//       }
//        return errors;
//    }

    /**
     * Checks every employee to ensure each one works every week involved in this month.
     * @param database - DatabaseHelper object for the current session
     * @param errors - existing list of errors
     * @return errors found
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for LocalDate
    private ArrayList<ErrorModel> verifyEmployeesWorkWeekly(DatabaseHelper database,
                                                             ArrayList<ErrorModel> errors) {
        //get list of employees from db
        ArrayList<EmployeeModel> employees = (ArrayList<EmployeeModel>) database.getEmployees();

        //iterate though each sunday
        for (LocalDate sunday : sundays) {

            //iterate though employees
            for (EmployeeModel employee : employees) {

                //check for any scheduled shifts from sunday to saturday
                if (!database.employeeIsScheduledBetween(employee, sunday, sunday.plusDays(6))) {

                    //check if they have time off all week
                    boolean employeeHasTimeOff = true;
                    for (int i = 0; i < 7; i++) {
                        if (!database.hasTimeOff(employee, sunday.plusDays(i))) {
                            employeeHasTimeOff = false;
                            break;
                        }
                    }

                    //make an error
                    if (!employeeHasTimeOff) {
                        String details = employee.getFName() +" "+ employee.getLName() +
                                " does not work this week.";
                        errors.add(new ErrorModel(sunday, sunday.plusDays(6), "", details));
                    }
                }
            }
        }

        return errors;
    }

}
