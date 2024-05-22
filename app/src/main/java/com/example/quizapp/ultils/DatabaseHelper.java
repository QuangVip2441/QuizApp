package com.example.quizapp.ultils;


import android.content.Context;
import android.content.SharedPreferences;

import java.security.AccessControlContext;

public class DatabaseHelper {
    private static final String DATABASE_NAME = "MyDatabase";
    private static final String KEY_PREFIX_ORDER = "order_";
    private static final String KEY_PREFIX_RESPONSE = "response_";
    private SharedPreferences sharedPreferences;

    public DatabaseHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
    }

    public void saveResponse(int order, int response) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PREFIX_RESPONSE + order, response);
        editor.apply();
    }

    public int getResponse(int order) {
        return sharedPreferences.getInt(KEY_PREFIX_RESPONSE + order, -1);
    }

    public void resetDatabase() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

