package com.example.shiftscheduler.models;

import java.time.LocalDate;

/**
 * For creating a medium to interact with database and recyclerview for layout.
 * Holds information regarding the date specified.
 */
public class DayModel {
    private LocalDate date;
    private MorningShift morningShift;
    private EveningShift eveningShift;
    private FullShift fullShift;
}
