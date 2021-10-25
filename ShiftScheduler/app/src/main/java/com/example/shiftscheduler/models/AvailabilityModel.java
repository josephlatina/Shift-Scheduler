package com.example.shiftscheduler.models;

import java.util.Objects;

public class AvailabilityModel {
    private int availabilityID;
    private int sunShift;
    private int monShift;
    private int tueShift;
    private int wedShift;
    private int thursShift;
    private int friShift;
    private int satShift;

    public AvailabilityModel(int availabilityID, int sunShift, int monShift, int tueShift,
                             int wedShift, int thursShift, int friShift, int satShift) {
        this.availabilityID = availabilityID;
        this.sunShift = sunShift;
        this.monShift = monShift;
        this.tueShift = tueShift;
        this.wedShift = wedShift;
        this.thursShift = thursShift;
        this.friShift = friShift;
        this.satShift = satShift;
    }

    // get methods
    public int getAvailabilityID() { return availabilityID;};

    public int getSunShift() { return sunShift;};

    public int getMonShift() { return monShift;}

    public int getTueShift() { return tueShift;}

    public int getWedShift() { return wedShift;}

    public int getThursShift() { return thursShift;}

    public int getFriShift() { return friShift;}

    public int getSatShift() { return satShift;}

    // set methods
    public void setAvailabilityID(Integer availabilityID) { this.availabilityID = availabilityID;}

    public void setSunShift(Integer sunShift) { this.sunShift = sunShift;}

    public void setMonShift() { this.monShift = monShift;}

    public void setTueShift() { this.tueShift = tueShift;}

    public void setWedShift() { this.wedShift = wedShift;}

    public void setThursShift() { this.thursShift = thursShift;}

    public void setFriShift() { this.friShift = friShift;}

    public void setSatShift() { this.satShift = satShift;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilityModel that = (AvailabilityModel) o;
        return availabilityID == that.availabilityID && sunShift == that.sunShift && monShift == that.monShift && tueShift == that.tueShift && wedShift == that.wedShift && thursShift == that.thursShift && friShift == that.friShift && satShift == that.satShift;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availabilityID, sunShift, monShift, tueShift, wedShift, thursShift, friShift, satShift);
    }

    @Override
    public String toString() {
        return "AvailabilityModel{" +
                "availabilityID=" + availabilityID +
                ", sunShift=" + sunShift +
                ", monShift=" + monShift +
                ", tueShift=" + tueShift +
                ", wedShift=" + wedShift +
                ", thursShift=" + thursShift +
                ", friShift=" + friShift +
                ", satShift=" + satShift +
                '}';
    }
}
