package com.example.quizapp.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.HistoryAdapter;
import com.example.quizapp.adapters.HistoryQuestionAdapter;
import com.example.quizapp.adapters.QuestionAdapter;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShowHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerShowHistoryNumberQuestion;
    private HistoryQuestionAdapter historyQuestionAdapter;
    private ExamModel examModel;
    private FirebaseUser user;
    private ArrayList<QuestionModel> mQuestions;
    private ArrayList<ExamModel> mExams;
    private FirebaseFirestore mFirestore;
    private String userID;
    private ArrayList<QuizModel> quizModels;
    private CollectionReference mRefCollectionQuestions, mRefCollectionExam;
    private DocumentReference mRefDocumentExam;
    private String ExamID;
    private Float marks =  0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        recyclerShowHistoryNumberQuestion = findViewById(R.id.recyclerShowHistoryNumberQuestion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        examModel = new ExamModel();
        mQuestions = new ArrayList<>();
        mExams = new ArrayList<>();
        quizModels = new ArrayList<>();

        Intent intent = getIntent();
        ExamID = intent.getStringExtra("Key");

        mFirestore = FirebaseFirestore.getInstance();

        mRefDocumentExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM)
                .document(ExamID);
        mRefDocumentExam.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    mExams.clear();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        if (data != null) {
                            mQuestions = new ArrayList<>();
                            ArrayList<HashMap<String, Object>> templistquestion = (ArrayList<HashMap<String, Object>>) data.get(Constant.Database.Exam.LISTQUESTIONS);
                            for (HashMap<String, Object> i : templistquestion) {

                                ArrayList<ChoiceModel> choices = new ArrayList<>();

                                ArrayList<HashMap<String, Object>> tempc = (ArrayList<HashMap<String, Object>>) i.get("choices");

                                for (HashMap<String, Object> ic : tempc) {
                                    choices.add(new ChoiceModel(
                                            (String) ic.get(Constant.Database.Choice.ID),
                                            (String) ic.get(Constant.Database.Choice.CONTENT)
                                    ));
                                }

                                QuestionModel question = new QuestionModel(
                                        (String) i.get(Constant.Database.Question.ID),
                                        (String) i.get(Constant.Database.Question.CONTENT),
                                        choices,
                                        (String) i.get(Constant.Database.Question.CORRECT)
                                );

                                mQuestions.add(question);
                            }


                            quizModels = new ArrayList<>();
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

                           examModel = new ExamModel(
                                    (String) data.get(Constant.Database.Exam.ID),
                                   (String) data.get(Constant.Database.Exam.MODULEID),
                                    quizModels,
                                    (String) data.get(Constant.Database.Exam.TEST_NAME),
                                    (String) data.get(Constant.Database.Exam.DURATION_IN_MINUTES),
                                    startDateTime,
                                    endDateTime,
                                    (String) data.get(Constant.Database.Exam.STATE),
                                    marks,
                                   mQuestions
                           );

                            mExams.add(examModel);

                            historyQuestionAdapter = new HistoryQuestionAdapter(R.layout.layout_item_header_number_question, mQuestions);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                            recyclerShowHistoryNumberQuestion.setLayoutManager(layoutManager);
                            recyclerShowHistoryNumberQuestion.setAdapter(historyQuestionAdapter);

                            ShowHistoryFragment showHistoryFragment = new ShowHistoryFragment(mQuestions,quizModels,0);
                            FragmentUtils.replaceHistoryFragmentQuestion(getSupportFragmentManager(), showHistoryFragment, true);

                            historyQuestionAdapter.setOnItemClickListener(new HistoryQuestionAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String questionId, int position) {
                                    ShowHistoryFragment showHistoryFragment = new ShowHistoryFragment(mQuestions,quizModels,position);
                                    FragmentUtils.replaceHistoryFragmentQuestion(getSupportFragmentManager(), showHistoryFragment, true);

                                }
                            });
                        }
                    }
                }
            }
        });


    }
}