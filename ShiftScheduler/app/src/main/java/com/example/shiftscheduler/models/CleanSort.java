package com.example.shiftscheduler.models;

import java.util.Comparator;
import java.util.Locale;

public class CleanSort implements Comparator<EmployeeModel> {

    public int compare(EmployeeModel a, EmployeeModel b) {
        if (a.getStatus() && !b.getStatus()) return -1;
        if (!a.getStatus() && b.getStatus()) return 1;
        return a.getLName().toUpperCase(Locale.ROOT).compareTo(b.getLName().toUpperCase(Locale.ROOT));
    }

}
