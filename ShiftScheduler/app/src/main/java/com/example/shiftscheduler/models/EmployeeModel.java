package com.example.shiftscheduler.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class EmployeeModel implements Comparable<EmployeeModel>, Serializable {
    private final int employeeID;
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
    private List<Boolean> qualifications;

    public EmployeeModel(int employeeID, String fName, String lName,
                         String city, String street, String province, String postal, String dob,
                         String phoneNum, String email, boolean isActive) {
        this.employeeID = employeeID;
        this.fName = fName;
        this.lName = lName;
        this.city = city;
        this.street = street;
        this.province = province;
        this.postal = postal;
        this.DOB = dob;
        this.phoneNum = phoneNum;
        this.email = email;
        this.isActive = isActive;
    }

    //For constructing with qualifications information
    public EmployeeModel(int employeeID, String fName, String lName,
                         String city, String street, String province, String postal, String dob,
                         String phoneNum, String email, boolean isActive, List<Boolean> qualifications) {
        this.employeeID = employeeID;
        this.fName = fName;
        this.lName = lName;
        this.city = city;
        this.street = street;
        this.province = province;
        this.postal = postal;
        this.DOB = dob;
        this.phoneNum = phoneNum;
        this.email = email;
        this.isActive = isActive;
        this.qualifications = qualifications;
    }

    public int getEmployeeID() {
        return employeeID;
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

    public String getEmail() { return email; }

    public boolean getStatus() { return isActive; }

    public List<Boolean> getQualifications() { return qualifications; }

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

    public void setEmail(String email) { this.email = email; }

    public void setisActive(boolean status) { this.isActive = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeModel that = (EmployeeModel) o;
        return employeeID == that.employeeID && isActive == that.isActive && fName.equals(that.fName) && lName.equals(that.lName) && city.equals(that.city) && street.equals(that.street) && province.equals(that.province) && postal.equals(that.postal) && DOB.equals(that.DOB) && phoneNum.equals(that.phoneNum);
    }

    @Override
    public int compareTo(EmployeeModel employee) {
        return Double.compare(employeeID, employee.employeeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeID, fName, lName, city, street, province, postal, DOB, phoneNum, isActive);
    }

    @Override
    public String toString() {
        return "EmployeeModel{" +
                "employeeID=" + employeeID +
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
                '}';
    }
}
