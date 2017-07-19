package com.example.android.toothbrushtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by danok on 7/17/2017.
 */

public class dbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = dbHelper.class.getSimpleName();

    //  name of the db file
    private static final String DATABASE_NAME = "brushes.db";
    //  no wories we wont uprade the db
    private static final int DATABASE_VERSION = 1;


    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //  gets called when the db is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("CREATEDDB1123", "Whatever");
        //entering Thor mode: creating the string to create the db
        String SQL_CREATE_BRUSHES_TABLE =  "CREATE TABLE " + BrushContract.BrushEntry.TABLE_NAME + " ("
                + BrushContract.BrushEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BrushContract.BrushEntry.COLUMN_NAME +  " TEXT NOT NULL, "
                + BrushContract.BrushEntry.COLUMN_BRAND + " TEXT NOT NULL, "
                + BrushContract.BrushEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_BRUSHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        NOTHING TO DO HERE
    }
}
