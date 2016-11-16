package com.a500.sweng.sickness_locator.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.util.Log;

import java.util.Date;

public class SicknessEntry {
    public String type;
    public String sickness;
    public String severity;
    public int daysSick;
    public double latitude;
    public double longitude;
    public String userId;
    public String entryDate;

    public SicknessEntry() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public float getMarkerColor() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        // Set Market Blue if marker is for current user
        if (fUser.getUid().equals(this.userId)) {
            return BitmapDescriptorFactory.HUE_AZURE;
        }

        // Otherwise set the marker color based on severity
       /* if (this.severity.toLowerCase().equals("high")) {
            return BitmapDescriptorFactory.HUE_RED;
        } else if (this.severity.toLowerCase().equals("medium")) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else {*/
            return BitmapDescriptorFactory.HUE_YELLOW;
        //}
    }
}
