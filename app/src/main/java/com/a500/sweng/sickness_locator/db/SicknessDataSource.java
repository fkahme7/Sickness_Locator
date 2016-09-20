package com.a500.sweng.sickness_locator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SicknessDataSource {
    private SQLiteDatabase mDatabase;       // The actual DB!
    private DatabaseHelper mDatabaseHelper; // Helper class for creating and opening the DB
    private Context mContext;

    public SicknessDataSource(Context context) {
        mContext = context;
        mDatabaseHelper = new DatabaseHelper(mContext);
    }

    /*
     * Open the db. Will create if it doesn't exist
     */
    public void open() throws SQLException {
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    /*
     * We always need to close our db connections
     */
    public void close() {
        mDatabase.close();
    }

    /*
     * CRUD operations!
     */

    /*
     * INSERT
     */
    public void insertSickness() {
        this.open();
        mDatabase.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(mDatabaseHelper.COLUMN_LATITUDE, -34.0);
            values.put(mDatabaseHelper.COLUMN_LONGITUDE, 151.0);
            mDatabase.insert(mDatabaseHelper.TABLE_SICKNESS, null, values);

            ContentValues values1 = new ContentValues();
            values1.put(mDatabaseHelper.COLUMN_LATITUDE, 39.0458);
            values1.put(mDatabaseHelper.COLUMN_LONGITUDE, -76.6413);
            mDatabase.insert(mDatabaseHelper.TABLE_SICKNESS, null, values1);

            ContentValues values2 = new ContentValues();
            values2.put(mDatabaseHelper.COLUMN_LATITUDE, 41.2033);
            values2.put(mDatabaseHelper.COLUMN_LONGITUDE, -77.1945);
            mDatabase.insert(mDatabaseHelper.TABLE_SICKNESS, null, values2);

            ContentValues values3 = new ContentValues();
            values3.put(mDatabaseHelper.COLUMN_LATITUDE, 40.7128);
            values3.put(mDatabaseHelper.COLUMN_LONGITUDE, -74.0059);
            mDatabase.insert(mDatabaseHelper.TABLE_SICKNESS, null, values3);

            ContentValues values4 = new ContentValues();
            values4.put(mDatabaseHelper.COLUMN_LATITUDE, 38.9108);
            values4.put(mDatabaseHelper.COLUMN_LONGITUDE, -75.5277);
            mDatabase.insert(mDatabaseHelper.TABLE_SICKNESS, null, values4);

            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }

    public Cursor selectAllSicknesses() {
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.TABLE_SICKNESS, // table
                new String[] { mDatabaseHelper.COLUMN_LATITUDE, mDatabaseHelper.COLUMN_LONGITUDE }, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null  // orderby
        );

        return cursor;
    }
}
