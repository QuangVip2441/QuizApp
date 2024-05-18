package com.example.quizapp.ultils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu và phiên bản
    private static final String DATABASE_NAME = "quiz_app.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // Tên bảng và các cột trong bảng
    public static final String TABLE_NAME = "user_answers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_M_POSITION = "_position";
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_ANSWER_ID = "answer_id";

    // Câu lệnh tạo bảng
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_M_POSITION + " INTEGER," +
                    COLUMN_QUESTION_ID + " TEXT," +
                    COLUMN_ANSWER_ID + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Thực thi câu lệnh tạo bảng khi cơ sở dữ liệu được tạo ra lần đầu tiên
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Định nghĩa các hành động khi cơ sở dữ liệu được nâng cấp (ví dụ: thêm hoặc loại bỏ cột)
        // Trong trường hợp này, chúng ta chỉ xóa và tạo lại bảng
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUserAnswer(String questionId, String answerId, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_M_POSITION, position);
        values.put(COLUMN_QUESTION_ID, questionId);
        values.put(COLUMN_ANSWER_ID, answerId);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getUserAnswersForQuestion(String questionId) {
        ArrayList<String> userAnswers = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ANSWER_ID
        };

        String selection = COLUMN_QUESTION_ID + " = ?";
        String[] selectionArgs = { questionId };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String answerId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER_ID));
            userAnswers.add(answerId);
        }

        cursor.close();

        return userAnswers;
    }

    public ArrayList<Integer> getPositionsForQuestion(String questionId) {
        ArrayList<Integer> positions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_M_POSITION
        };

        String selection = COLUMN_QUESTION_ID + " = ?";
        String[] selectionArgs = { questionId };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int position = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_M_POSITION));
            positions.add(position);
        }

        cursor.close();

        return positions;
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}