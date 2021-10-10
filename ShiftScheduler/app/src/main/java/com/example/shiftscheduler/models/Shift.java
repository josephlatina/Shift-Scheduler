package com.example.shiftscheduler.models;

//import java.util.Set;
//import java.time;
//
///**
// * Different shift times
// */
//public enum ShiftTime {
//    MORNING,
//    EVENING,
//    FULL
//} //if a change is made here, make sure to reflect it in the setTime() method.
//
///**
// * Represents a unit of work for a specific date/time
// * @author Alex Cairns
// */
//public class Shift {
//    private final int shiftID;
//    private Day day; //TBD?
//    private LocalDate date;
//    private ShiftTime time;
//    private Set<int> employees = new Set<>(); //Set<> doesn't allow duplicates
//    private int maxEmployeeCount = 2; //edit this to change the default
//
//    /**
//     * Constructor.
//     * @param ID - will probably be auto-generated by future logic
//     * @param day - TBD
//     * @param time - ShiftTime
//     */
//    public Shift(int shiftID, Day day, LocalDate date, String time) {
//        this.shiftID = shiftID;
//        this.day = day; //?
//        this.date = date;
//        setTime(time);
//    }
//
//    /**
//     * Compares two Shift objects by their IDs
//     * @param otherShift
//     * @return equality
//     */
//    @Override
//    public boolean equals(Shift otherShift) {
//        return this.shiftID == otherShift.getShiftID();
//    }
//
//
//    /**
//     * @return shift ID
//     */
//    public int getShiftID() {
//        return shiftID;
//    }
//
//    /**
//     * @return day object containing this shift
//     */
//    public Day getDay() { //?
//        return day;
//    }
//
//    /**
//     * @return date of shift
//     */
//    public LocalDate getDate() {
//        return date;
//    }
//
//    /**
//     * @return time of shift (see ShiftTime enum above)
//     */
//    public ShiftTime getTime() {
//        return time;
//    }
//
//    /**
//     * @return set of assigned employees' IDs
//     */
//    public Set<int> getEmployees() {
//        return employees;
//    }
//
//    /**
//     * @return maximum number of assigned employees (2 by default)
//     */
//    public int getMaxEmployeeCount() {
//        return maxEmployeeCount;
//    }
//
//
//    /**
//     * Changes Day variable associated with the shift
//     * @param day - Day object containing this shift
//     */
//    public void setDay(Day day) { //?
//        this.day = day;
//    }
//
//    /**
//     * Changes date
//     * @param date - LocalDate object the shift occurs on
//     */
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
//
//    /**
//     * Changes shift time
//     * @param time - String corresponding to the ShiftTime enum (see above)
//     */
//    public static void setTime(String time) {
//        switch (time.toUpperCase().strip()) {
//            case "MORNING" -> this.time = ShiftTime.MORNING;
//            case "EVENING" -> this.time = ShiftTime.EVENING;
//            case "FULL" -> this.time = ShiftTime.FULL;
//            default -> throw new IllegalArgumentException("Invalid shift time.");
//        }
//    }
//
//    /**
//     * Changes shift time
//     * @param time - ShiftTime object (see enum above)
//     */
//    public static void setTime(ShiftTime time) {
//        this.time = time;
//    }
//
//    /**
//     * Changes the maximum employees that can be assigned to the shift
//     * @param newMax - new maximum
//     */
//    public void setMaxEmployeeCount(int newMax) {
//        if (newMax > employees.size()) {
//            throw new Exception("Shift is too full.");
//        }
//        this.maxEmployeeCount = newMax;
//    }
//
//
//    /**
//     * Adds a new employee to the set
//     * @param employeeID - ID of the employee to be added
//     * @return successfully added employee or not
//     */
//    public boolean addEmployee(int employeeID) {
//        boolean successful = false;
//        if (employees.size() >= maxEmployeeCount) {
//            throw new Exception("Shift is full.");
//        }
//        else {
//            successful = employees.add(employeeID);
//        }
//        return successful == true;
//    }
//
//    /**
//     * Removes an employee from the set
//     * @param employeeID - ID of the employee to be removed
//     * @return successfully removed employee or not
//     */
//    public boolean removeEmployee(int employeeID) {
//        if (employees.contains(employeeID)) {
//            boolean successful = employees.remove(employeeID);
//        }
//        return successful == true;
//    }
//
//    /**
//     * Evaluates whether the shift meets the specified criteria
//     * (possibly don't even end up needing this here)
//     * @return shift is valid or not
//     */
//    public boolean isValid() {
//        //TBD
//        return true; //for now
//    }
//}
