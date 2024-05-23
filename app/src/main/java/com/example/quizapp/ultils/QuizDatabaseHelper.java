package com.example.quizapp.ultils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizapp.Models.QuizModel;

import java.util.ArrayList;

public class QuizDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_QUIZ = "quiz";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUESTION_CONTENT = "question_content";
    private static final String COLUMN_ID_ANSWER = "id_answer";
    private static final String COLUMN_ID_CORRECT = "id_correct";
    private static final String COLUMN_STATE = "state";

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_QUESTION_CONTENT + " TEXT,"
                + COLUMN_ID_ANSWER + " TEXT,"
                + COLUMN_ID_CORRECT + " TEXT,"
                + COLUMN_STATE + " INTEGER" + ")";
        db.execSQL(CREATE_QUIZ_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
    }

    // Thêm một quiz mới
    public void addQuiz(QuizModel quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, quiz.getId());
        values.put(COLUMN_QUESTION_CONTENT, quiz.getQuestioncontent());
        values.put(COLUMN_ID_ANSWER, quiz.getIdanswer());
        values.put(COLUMN_ID_CORRECT, quiz.getIdcorrect());
        values.put(COLUMN_STATE, quiz.isState() ? 1 : 0);

        db.insert(TABLE_QUIZ, null, values);
        db.close();
    }

    // Lấy tất cả các quiz
    public ArrayList<QuizModel> getAllQuizzes() {
        ArrayList<QuizModel> quizList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUIZ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                QuizModel quiz = new QuizModel(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID_ANSWER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID_CORRECT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATE)) == 1
                );
                quizList.add(quiz);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return quizList;
    }

    // Xóa tất cả các quiz
    public void deleteAllQuizzes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_QUIZ);
        db.close();
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
        db.close();
    }
}
