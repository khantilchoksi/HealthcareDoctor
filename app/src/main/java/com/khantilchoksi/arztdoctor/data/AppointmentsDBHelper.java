package com.khantilchoksi.arztdoctor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by khantilchoksi on 25/07/17.
 */

public class AppointmentsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appointments.db";

    private static final int DATABAS_VERSION = 1;

    public AppointmentsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABAS_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_APPOINTMENTS_TABLE = "CREATE TABLE "+
                AppointmentsContract.AppointmentsEntry.TABLE_NAME + " (" +
                AppointmentsContract.AppointmentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AppointmentsContract.AppointmentsEntry.COLUMN_APPOINTMENT_ID + " TEXT NOT NULL, "+
                AppointmentsContract.AppointmentsEntry.COLUMN_PATIENT_NAME + " TEXT NOT NULL, "+
                AppointmentsContract.AppointmentsEntry.COLUMN_CLINIC_NAME + " TEXT NOT NULL, "+
                AppointmentsContract.AppointmentsEntry.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, "+
                AppointmentsContract.AppointmentsEntry.COLUMN_APPOINTMENT_DAY + " TEXT NOT NULL, "+
                AppointmentsContract.AppointmentsEntry.COLUMN_APPOINTMENT_START_TIME + " TEXT NOT NULL, "+
                AppointmentsContract.AppointmentsEntry.COLUMN_APPOINTMENT_END_TIME + " TEXT NOT NULL "+
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_APPOINTMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ AppointmentsContract.AppointmentsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
