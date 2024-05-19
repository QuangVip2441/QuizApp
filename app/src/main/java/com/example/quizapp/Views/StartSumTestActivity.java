package com.example.quizapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizapp.MainActivity;
import com.example.quizapp.R;

public class StartSumTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_sum_test);
        final AppCompatButton btnStartQuiz = findViewById(R.id.btnStartQuiz);
        final AppCompatButton btnQuit = findViewById(R.id.btnQuit);


        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartSumTestActivity.this, RandomQuestionActivity.class);
                startActivity(intent);
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(StartSumTestActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}