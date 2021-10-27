package com.example.shiftscheduler.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Container for all the DayModels associated with a given month.
 */
public class MonthModel {
    private final LocalDate startDate;
    private ArrayList<DayModel> days;

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
     * @param employees - ArrayList of all current working EmployeeModels
     * @return verified
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean verifyMonth(ArrayList<EmployeeModel> employees) {
        // verify all days individually
        for (DayModel day : days) {
            if (!day.verifyDay()) {
                return false;
            }
        }

        // verify all employees work every week (TBD)
            // find the Sunday at/before startDate
            // find the Saturday at/after the last day of the month
            // ensure every available employee works one shift on every week

        // month has passed verification
        return true;
    }
}
