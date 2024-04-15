package com.example.quizapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizapp.R;
import com.example.quizapp.Views.QuestionFragment;
import com.example.quizapp.ultils.FragmentUtils;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final AppCompatButton btnStartQuiz = findViewById(R.id.btnStartQuiz);
        final AppCompatButton btnQuit = findViewById(R.id.btnQuit);

        Intent intent = getIntent();
        String moduleID = intent.getStringExtra("Key");
        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, QuestionActivity.class);
                intent.putExtra("Key", moduleID);
                startActivity(intent);
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(StartActivity.this, ModuleActivity.class);
            }
        });
    }

}