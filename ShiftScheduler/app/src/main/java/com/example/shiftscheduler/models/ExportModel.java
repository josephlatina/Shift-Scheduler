package com.example.shiftscheduler.models;

import java.util.Objects;

public class ExportModel {

    private int empID;
    private String fName;
    private String lName;
    private String city;
    private String street;
    private String province;
    private String postal;
    private String DOB;
    private String phoneNum;
    private String email;
    private boolean isActive;
    private int shiftID;
    private String shiftType;
    private String date;

    public ExportModel(int empID, String fName, String lName, String city, String street, String province, String postal, String DOB, String phoneNum, String email, boolean isActive, int shiftID, String shiftType, String date) {
        this.empID = empID;
        this.fName = fName;
        this.lName = lName;
        this.city = city;
        this.street = street;
        this.province = province;
        this.postal = postal;
        this.DOB = DOB;
        this.phoneNum = phoneNum;
        this.email = email;
        this.isActive = isActive;
        this.shiftID = shiftID;
        this.shiftType = shiftType;
        this.date = date;
    }

    public int getEmpID() {
        return empID;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
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

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getShiftID() {
        return shiftID;
    }

    public String getShiftType() {
        return shiftType;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ExportModel{" +
                "empID=" + empID +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", province='" + province + '\'' +
                ", postal='" + postal + '\'' +
                ", DOB='" + DOB + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", shiftID=" + shiftID +
                ", shiftType='" + shiftType + '\'' +
                ", date='" + date + '\'' +
                '}';
    }


}
