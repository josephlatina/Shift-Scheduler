package com.example.shiftscheduler.models;

import java.util.Objects;

public class ExportModel {
    private String firstName;
    private String lastName;
    private String shiftType;
    private String date;

    public ExportModel(String firstName, String lastName, String shiftType, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shiftType = shiftType;
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getShiftType() {
        return shiftType;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExportModel that = (ExportModel) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(shiftType, that.shiftType) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, shiftType, date);
    }

    @Override
    public String toString() {
        return "ExportModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", shiftType='" + shiftType + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
