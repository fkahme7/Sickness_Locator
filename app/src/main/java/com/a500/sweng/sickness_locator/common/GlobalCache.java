package com.a500.sweng.sickness_locator.common;

import android.support.v7.widget.Toolbar;

import com.a500.sweng.sickness_locator.models.SicknessEntry;


public class GlobalCache {
    // handler for singleton instance
    private static GlobalCache instance = null;
    private Toolbar toolbar;
    //LinearLayout homeImageLayout, toolbarLogoLayout;
    SicknessEntry sicknessEntry;



    public static GlobalCache getInstance() {
        if (instance == null) {
            instance = new GlobalCache();
        }
        return instance;
    }


    public void setSickEntry(SicknessEntry sicknessEntry) {
        this.sicknessEntry = sicknessEntry;
    }

    public SicknessEntry getSickEntry() {
        return sicknessEntry;
    }


}
