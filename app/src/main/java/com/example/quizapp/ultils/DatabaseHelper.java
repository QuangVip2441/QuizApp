package com.example.quizapp.ultils;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuestionResponses.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESPONSES = "responses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ORDER = "question_order";
    public static final String COLUMN_RESPONSE = "response";

    // SQL statement to create the responses table
    private static final String SQL_CREATE_TABLE_RESPONSES =
            "CREATE TABLE " + TABLE_RESPONSES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ORDER + " INTEGER," +
                    COLUMN_RESPONSE + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_RESPONSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
        onCreate(db);
    }
    public void resetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RESPONSES);
        db.close();
    }
    public void updateResponse(int order, int response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("response", response);
        String[] whereArgs = { String.valueOf(order) };
        db.update("YourTableName", values, "mOrder=?", whereArgs);
        db.close();
    }
}
