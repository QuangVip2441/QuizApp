package com.example.quizapp.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.HistoryAdapter;
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

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerCategory;
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
    private ContentLoadingProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerCategory = findViewById(R.id.recyclerCategory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar.show();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mQuestions = new ArrayList<>();
        mExams = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        mRefCollectionExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM);
        mRefCollectionExam.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mExams.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<QuizModel> quizModels = new ArrayList<>();

                        ArrayList<HashMap<String, Object>> temp = (ArrayList<HashMap<String, Object>>) data.get(Constant.Database.Exam.QUESTION);
                        for (HashMap<String, Object> i : temp) {
                            quizModels.add(new QuizModel(
                                    (String) i.get("id"),
                                    (String) i.get("questioncontent"),
                                    (String) i.get("idanswer"),
                                    (String) i.get("idcorrect"),
                                    (boolean) i.get("state")
                            ));
                        }

                        Timestamp startDateTimeTimestamp = (Timestamp) data.get(Constant.Database.Exam.STARTDATETIME);
                        Timestamp endDateTimeTimestamp = (Timestamp) data.get(Constant.Database.Exam.ENDDATETIME);
                        Date startDateTime = startDateTimeTimestamp != null ? startDateTimeTimestamp.toDate() : null;
                        Date endDateTime = endDateTimeTimestamp != null ? endDateTimeTimestamp.toDate() : null;

                        String marksString = String.valueOf(data.get(Constant.Database.Exam.MARKS));
                        marks = Float.parseFloat(marksString);

                        ExamModel examModel = new ExamModel(
                                (String) data.get(Constant.Database.Exam.ID),
                                quizModels,
                                (String) data.get(Constant.Database.Exam.TEST_NAME),
                                (String) data.get(Constant.Database.Exam.DURATION_IN_MINUTES),
                                startDateTime,
                                endDateTime,
                                (String) data.get(Constant.Database.Exam.STATE),
                                marks
                        );
                        mExams.add(examModel);
                    }
                    ShowHistoryquestionAdapter = new HistoryAdapter(R.layout.layout_history_item, mExams);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerCategory.setLayoutManager(layoutManager);
                    recyclerCategory.setAdapter(ShowHistoryquestionAdapter);
                    progressBar.hide();
                    ShowHistoryquestionAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String examId,int position) {
                            Intent intent = new Intent(HistoryActivity.this, ShowHistoryActivity.class);
                            intent.putExtra("Key",examId);
                            startActivity(intent);
                        }
                    });

                }
            }
        });
    }
}