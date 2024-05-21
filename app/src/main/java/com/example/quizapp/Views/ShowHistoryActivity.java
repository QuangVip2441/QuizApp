package com.example.quizapp.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.HistoryAdapter;
import com.example.quizapp.adapters.QuestionAdapter;
import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShowHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerShowHistoryNumberQuestion;
    private HistoryAdapter ShowHistoryquestionAdapter;
    private ExamModel examModel;
    private FirebaseUser user;
    private ArrayList<QuestionModel> mQuestions;
    private ArrayList<ExamModel> mExams;
    private FirebaseFirestore mFirestore;
    private String userID;
    private CollectionReference mRefCollectionQuestions, mRefCollectionExam;
    private DocumentReference mRefDocumentExam;
    private Float marks =  0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        recyclerShowHistoryNumberQuestion = findViewById(R.id.recyclerShowHistoryNumberQuestion);


    }
}