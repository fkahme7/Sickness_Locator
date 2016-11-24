package com.a500.sweng.sickness_locator;

import com.a500.sweng.sickness_locator.models.SicknessEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.junit.Test;

import static org.junit.Assert.*;

public class SicknessEntryUnitTest {
    @Test
    public void test_latitude() throws Exception {
        double latitude = -38;
        SicknessEntry entry = new SicknessEntry();
        entry.setLatitude(latitude);

        assertEquals(entry.getLatitude(), latitude, 0.001 );
    }
    @Test
    public void test_longitude() throws Exception {
        double longitude = 150;
        SicknessEntry entry = new SicknessEntry();
        entry.setLongitude(longitude);

        assertEquals(entry.getLongitude(), longitude, 0.001 );
    }
}
