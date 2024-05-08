package com.example.quizapp.ultils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnswerDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "answer_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ANSWERS = "answers";
    private static final String COLUMN_QUESTION_ID = "question_id";
    private static final String COLUMN_SELECTED_CHOICE_ID = "selected_choice_id";

    // Phương thức khởi tạo
    public AnswerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Phương thức tạo bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_ANSWERS + " (" +
                COLUMN_QUESTION_ID + " TEXT PRIMARY KEY," +
                COLUMN_SELECTED_CHOICE_ID + " TEXT)";
        db.execSQL(createTableQuery);
    }

    // Phương thức cập nhật cơ sở dữ liệu
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        onCreate(db);
    }

    // Phương thức để lưu câu trả lời đã chọn vào cơ sở dữ liệu
    public void saveAnswer(String questionId, String selectedChoiceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_ID, questionId);
        values.put(COLUMN_SELECTED_CHOICE_ID, selectedChoiceId);
        db.insertWithOnConflict(TABLE_ANSWERS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // Phương thức để lấy câu trả lời đã lưu từ cơ sở dữ liệu
    // Phương thức để lấy câu trả lời đã lưu từ cơ sở dữ liệu
    public String getSelectedChoiceId(String questionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANSWERS, new String[]{COLUMN_SELECTED_CHOICE_ID},
                COLUMN_QUESTION_ID + "=?", new String[]{questionId}, null, null, null);
        String selectedChoiceId = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_SELECTED_CHOICE_ID);
                if (columnIndex != -1) {
                    selectedChoiceId = cursor.getString(columnIndex);
                }
            }
            cursor.close();
        }
        db.close();
        return selectedChoiceId;
    }

}
