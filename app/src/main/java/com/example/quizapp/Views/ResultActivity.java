package com.example.quizapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.TextView;

import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.McqRvAdapter;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<QuestionModel> mQuestions;
    private TextView txtScore, txttotalScore, txtcorrect, txtincorrect;
    private AppCompatButton btnShare, btnreTakeQuiz;
    private McqRvAdapter mcqRVAdapter;
    private int correctAns = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtScore = findViewById(R.id.txtScore);
        txtcorrect = findViewById(R.id.txtcorrect);
        txtincorrect = findViewById(R.id.txtincorrect);
        txttotalScore = findViewById(R.id.txttotalScore);
        btnShare = findViewById(R.id.btnShare);
        btnreTakeQuiz = findViewById(R.id.btnreTakeQuiz);




        txttotalScore.setText("/" + 10);
        txtScore.setText(correctAns  + "");
        txtcorrect.setText(correctAns + "");
        txtincorrect.setText(String.valueOf(10 - correctAns));
    }
}