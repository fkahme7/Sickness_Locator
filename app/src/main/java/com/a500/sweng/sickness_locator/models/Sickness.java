package com.a500.sweng.sickness_locator.models;

public class Sickness {
    public String type;
    public String sickness;
    public String severity;
    public int daysSick;
    public float latitude;
    public float longitude;

    public Sickness(String type, String sickness, String severity, int daysSick) {
        this.type = type;
        this.sickness = sickness;
        this.severity = severity;
        this.daysSick = daysSick;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSickness() {
        return sickness;
    }

    public void setSickness(String sickness) {
        this.sickness = sickness;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getDaysSick() {
        return daysSick;
    }

    public void setDaysSick(int daysSick) {
        this.daysSick = daysSick;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
