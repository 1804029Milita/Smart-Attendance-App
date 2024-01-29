package com.example.smartattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String S_ID = "_SID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY = "ID";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME +
                    "(" +
                    S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    STUDENT_ROLL_KEY + " INTEGER, " +
                    STUDENT_NAME_KEY + " TEXT NOT NULL" +
                    ");";


    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM " + STUDENT_TABLE_NAME;

    private static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "_STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";
    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME +
                    "(" +
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    S_ID + " INTEGER NOT NULL, " +
                    DATE_KEY + " DATE NOT NULL, " +
                    STATUS_KEY + " TEXT NOT NULL, " +
                    " UNIQUE (" + S_ID + "," + DATE_KEY + "), " +
                    " FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + "(" + S_ID + ")" +
                    ");";

    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM " + STATUS_TABLE_NAME;

    public DbHelper(@Nullable Context context) {
        super(context, "Attendance.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    long addStudent(int studentId, String studentName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_ROLL_KEY, studentId);
        values.put(STUDENT_NAME_KEY, studentName);
        return database.insert(STUDENT_TABLE_NAME, null, values);
    }

    Cursor getStudentTable() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STUDENT_TABLE_NAME, null, null, null, null, null, STUDENT_ROLL_KEY);
    }

    int deleteStudent(long sid) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(STUDENT_TABLE_NAME, S_ID + "=?", new String[]{String.valueOf(sid)});
    }

    long addStatus(long sid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID, sid);
        values.put(DATE_KEY, date);
        values.put(STATUS_KEY, status);
        return database.insert(STATUS_TABLE_NAME, null, values);
    }
    long updateStatus(long sid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY, status);
        String whereClause = DATE_KEY + "=? AND " + S_ID + "=?";
        return database.update(STATUS_TABLE_NAME, values, whereClause, new String[]{date, String.valueOf(sid)});
    }

    String getStatus(long sid, String date) {
        String status = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE_KEY + "=? AND " + S_ID + "=?";
        String[] whereArgs = {date, String.valueOf(sid)};

        Cursor cursor = database.query(
                STATUS_TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int statusIndex = cursor.getColumnIndex(STATUS_KEY);
            if (statusIndex != -1) {
                status = cursor.getString(statusIndex);
            }
        }
        cursor.close();
        return status;
    }

    Cursor getDistinctMonths() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = {DATE_KEY};
        String groupBy = "substr(" + DATE_KEY + ", 4, 7)";
        return database.query(true, STATUS_TABLE_NAME, columns, null, null, groupBy, null, null, null);
    }
}

