package com.example.quizapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.quizapp.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RandomQuestionActivity extends AppCompatActivity {

    private TextView txtRandomNumderQuestion, txtRandomTotalQuestion, txtRandomTimer;
    private CountDownTimer countDownTimer;
    private RecyclerView RandomrecyclerNumberQuestion;
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionQuestions;
    private int Test_timer = 600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_question);
    }
}