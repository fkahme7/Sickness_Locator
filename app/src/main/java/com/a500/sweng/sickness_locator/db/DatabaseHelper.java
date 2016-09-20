package com.a500.sweng.sickness_locator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    /*
     * Table and column information
     */
    public static final String TABLE_SICKNESS = "SICKNESS";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_LATITUDE = "LATITUDE";
    public static final String COLUMN_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_TIME = "TIME";


    /*
     * Database information
     */
    private static final String DB_NAME = "sickness_locator.db";
    private static final int DB_VERSION = 1; // Must increment to trigger an upgrade
    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_SICKNESS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LATITUDE + " FLOAT, " +
                    COLUMN_LONGITUDE + " FLOAT)";

    private static final String DB_ALTER =
            "ALTER TABLE " + TABLE_SICKNESS + " ADD COLUMN " + COLUMN_TIME + " INTEGER";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    /*
     * This is triggered by incrementing DB_VERSION
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_ALTER);
    }
}