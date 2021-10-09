package com.example.shiftscheduler;

public class EmployeeModel {
    private int employeeID;
    private int shiftTypeID;
    private int avaID;
    private String fName;
    private String lName;
    private String city;
    private String street;
    private String province;
    private String postal;
    private String DOB;
    private String phoneNum;

    public EmployeeModel(Integer employeeID, Integer shiftTypeID, Integer avaID, String fName, String lName, String city, String street, String province, String postal, String dob, String phoneNum) {
        this.employeeID = employeeID;
        this.shiftTypeID = shiftTypeID;
        this.avaID = avaID;
        this.fName = fName;
        this.lName = lName;
        this.city = city;
        this.street = street;
        this.province = province;
        this.postal = postal;
        this.DOB = dob;
        this.phoneNum = phoneNum;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public int getShiftTypeID() {
        return shiftTypeID;
    }

    public int getAvaID() {
        return avaID;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getProvince() {
        return province;
    }

    public String getPostal() {
        return postal;
    }

    public String getDOB() {
        return DOB;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public void setShiftTypeID(Integer shiftTypeID) {
        this.shiftTypeID = shiftTypeID;
    }

    public void setAvaID(Integer avaID) {
        this.avaID = avaID;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
