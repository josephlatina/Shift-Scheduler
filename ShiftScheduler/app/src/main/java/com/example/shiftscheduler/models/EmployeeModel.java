package com.example.shiftscheduler.models;

import java.util.Objects;

public class EmployeeModel {
    private int employeeID;
    private int qualificationID;
    private int avaID;
    private String fName;
    private String lName;
    private String city;
    private String street;
    private String province;
    private String postal;
    private String DOB;
    private String phoneNum;
    boolean isActive;


    public EmployeeModel(int employeeID, int qualificationID, int avaID, String fName, String lName,
                         String city, String street, String province, String postal, String dob,
                         String phoneNum) {
        this.employeeID = employeeID;
        this.qualificationID = qualificationID;
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

    public int getQualificationID() {
        return qualificationID;
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

    public void setQualificationID(Integer qualificationID) {
        this.qualificationID = qualificationID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeModel that = (EmployeeModel) o;
        return employeeID == that.employeeID && qualificationID == that.qualificationID && avaID == that.avaID && isActive == that.isActive && fName.equals(that.fName) && lName.equals(that.lName) && city.equals(that.city) && street.equals(that.street) && province.equals(that.province) && postal.equals(that.postal) && DOB.equals(that.DOB) && phoneNum.equals(that.phoneNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeID, qualificationID, avaID, fName, lName, city, street, province, postal, DOB, phoneNum, isActive);
    }

    @Override
    public String toString() {
        return "EmployeeModel{" +
                "employeeID=" + employeeID +
                ", qualificationID=" + qualificationID +
                ", avaID=" + avaID +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", province='" + province + '\'' +
                ", postal='" + postal + '\'' +
                ", DOB='" + DOB + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
