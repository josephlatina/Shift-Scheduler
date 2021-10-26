package com.example.shiftscheduler.models;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * For creating a medium to interact with database and recyclerview for layout.
 * Holds information regarding the date specified.
 */
public class DayModel {
    private final LocalDate date;
    private MorningShift morningShift = null;
    private EveningShift eveningShift = null;
    private FullShift fullShift = null;

    /**
     * Constructor.
     * @param date - LocalDate of day (final)
     */
    public DayModel(LocalDate date) {
        this.date = date;
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
     * Adds a shift to this day (once a shift is added it cannot be removed or overwritten).
     * @param shift - shift of any type
     * @return successful
     */
    public boolean addShift(@NonNull ShiftModel shift) {
        if (shift.getClass() == MorningShift.class && morningShift == null) {
            morningShift = shift.toMorning();
        } else if (shift.getClass() == EveningShift.class && eveningShift == null) {
            eveningShift = shift.toEvening();
        } else if (fullShift == null) {
            fullShift = shift.toFull();
        } else {
            return false;
        }
        return true;
    }

    /**
     * Verifies the schedule for this day.
     * @return whether day is valid
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean verifyDay() {
        // check for proper shift existence based on day of week
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SUNDAY || dow == DayOfWeek.SATURDAY) {
            if (fullShift == null || morningShift != null || eveningShift != null) {
                return false;
            }
        } else if (morningShift == null || eveningShift == null) {
            return false;
        }

        // check shifts themselves
        if (morningShift != null && !morningShift.verifyShift()) {return false;}
        if (eveningShift != null && !eveningShift.verifyShift()) {return false;}
        if (fullShift != null && !fullShift.verifyShift()) {return false;}

        // day has passed verification
        return true;
    }
}
