package com.example.shiftscheduler.models;

import java.time.LocalDate;

public class TimeoffModel {
    private int timeoffID;
    private int empID;
    private String fName;
    private String lName;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public TimeoffModel (int timeoffID, int empID, String fName, String lName, LocalDate dateFrom, LocalDate dateTo) {
        this.timeoffID = timeoffID;
        this.empID = empID;
        this.fName = fName;
        this.lName = lName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public int getTimeoffID() { return timeoffID; }

    public int getEmpID() { return empID; }

    public LocalDate getDateFrom() { return dateFrom; }

    public LocalDate getDateTo() { return dateTo; }

    public String getFName() { return fName; }

    public String getLName() { return lName; }
}
