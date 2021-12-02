package com.example.shiftscheduler.models;

import androidx.annotation.NonNull;

import java.time.LocalDate;

/**
 * Contains the information for a specific error.
 */
public class ErrorModel {
    private LocalDate startDate;
    private LocalDate endDate;
    private String details;
    private String weeklyDetails;

    /**
     * Constructor. (One Date)
     * @param date - LocalDate of error
     * @param details - error details
     */
    public ErrorModel(LocalDate date, String details) {
        this.startDate = date;
        this.endDate = null;
        this.details = details;
    }

    /**
     * Constructor. (Date Range)
     * @param startDate - start of error date range
     * @param endDate - end of error date range
     * @param details - error details
     */
    public ErrorModel(LocalDate startDate, LocalDate endDate, String details) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
    }

    /**
     * Constructor. (for VerifyEmployeesWorkWeekly)
     * @param startDate - start of error date range
     * @param endDate - end of error date range
     * @param details - error details
     */
    public ErrorModel(LocalDate startDate, LocalDate endDate, String details, String weeklyDetails) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
        this.weeklyDetails = weeklyDetails;
    }

    /**
     * @return string representation
     */
    @NonNull
    public String toString() {
        if (endDate == null) return startDate + ": " + details;
        else return startDate + " to " + endDate + ": " + details;
    }

    /**
     * @return startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets a new starting/only date of error.
     * @param startDate - starting/only date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return endDate, or startDate if there's isn't one
     */
    public LocalDate getEndDate() {
        if (endDate == null) return startDate;
        else return endDate;
    }

    /**
     * Sets the end of the error date range
     * @param endDate - end of date range
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * @return error details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @return weekly error details
     */
    public String getWeeklyDetails() {
        return weeklyDetails;
    }

    /**
     * Set new error details.
     * @param details - new details
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
