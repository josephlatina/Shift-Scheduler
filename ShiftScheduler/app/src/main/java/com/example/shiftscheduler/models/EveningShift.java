package com.example.shiftscheduler.models;

import com.example.shiftscheduler.R;

import java.time.LocalDate;

public class EveningShift extends ShiftModel {
    private final ShiftTime time = ShiftTime.EVENING;
    private final int icon = R.drawable.ic_evening;

    /**
     * Constructor.
     *
     * @param shiftID    - will probably be auto-generated by future logic
     * @param date       - date of shift
     * @param employeeID - ID of the assigned employee
     */
    public EveningShift(int shiftID, LocalDate date, int employeeID) {
        super(shiftID, date, employeeID);
    }

    /**
     * @return itself
     */
    @Override
    public EveningShift toEvening() {
        return this;
    }
}
