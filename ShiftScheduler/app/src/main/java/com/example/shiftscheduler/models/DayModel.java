package com.example.shiftscheduler.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.database.DatabaseHelper;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Container for all the ShiftModels associated with a specific day.
 */
public class DayModel implements Serializable {
    private final LocalDate date;
    private final MorningShift morningShift;
    private final EveningShift eveningShift;
    private final FullShift fullShift;

    /**
     * Constructor. 2 shifts (weekday)
     * @param date - LocalDate of day (final)
     * @param morningShift - MorningShift for date
     * @param eveningShift - EveningShift for date
     */
    public DayModel(LocalDate date, MorningShift morningShift, EveningShift eveningShift) {
        this.date = date;
        this.morningShift = morningShift;
        this.eveningShift = eveningShift;
        this.fullShift = null;
    }

    /**
     * Constructor. 1 shift (weekend)
     * @param date - LocalDate of day (final)
     * @param fullShift - FullShift for date
     */
    public DayModel(LocalDate date, FullShift fullShift) {
        this.date = date;
        this.morningShift = null;
        this.eveningShift = null;
        this.fullShift = fullShift;
    }

    /**
     * @param otherDay - other DayModel object
     * @return equality
     */
    @RequiresApi(api = Build.VERSION_CODES.O) //for .isEqual()
    public boolean equals(DayModel otherDay) {
        return (date.isEqual(otherDay.getDate()));
    }

    /**
     * @return LocalDate of day
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return morning shift (or null if there isn't one)
     */
    public MorningShift getMorningShift() {
        return morningShift;
    }

    /**
     * @return evening shift (or null if there isn't one)
     */
    public EveningShift getEveningShift() {
        return eveningShift;
    }

    /**
     * @return full shift (or null if there isn't one)
     */
    public FullShift getFullShift() {
        return fullShift;
    }

    /**
     * Checks this day's shifts for a specific employee.
     * @param employee - desired EmployeeModel
     * @return found employee
     */
    public boolean containsEmployee(EmployeeModel employee) {
        if (fullShift != null) {
            return fullShift.getEmployees().contains(employee);
        } else {
            return (morningShift.getEmployees().contains(employee) ||
                    getEveningShift().getEmployees().contains(employee));
        }
    }

    /**
     * Verifies the schedule for this day.
     * @param database - DatabaseHelper object for the current session
     * @return whether day is valid
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ErrorModel> verifyDay(DatabaseHelper database, ArrayList<ErrorModel> errors) {
        // check for proper shift existence based on day of week (should be guaranteed by other logic)
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SUNDAY || dow == DayOfWeek.SATURDAY) {
            if (fullShift == null || morningShift != null || eveningShift != null) {
                errors.add(new ErrorModel(date, "CRITICAL. Improper shift existence."));
            }
        } else if (morningShift == null || eveningShift == null) {
            errors.add(new ErrorModel(date, "CRITICAL. Improper shift existence."));
        }

        // check shifts themselves
        if (morningShift != null) {
            errors = morningShift.verifyShift(database, errors);
        }
        if (eveningShift != null) {
            errors = eveningShift.verifyShift(database, errors);
        }
        if (fullShift != null) {
            errors = fullShift.verifyShift(database, errors);
        }

        // return all found errors
        return errors;
    }
}
