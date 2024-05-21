package com.example.quizapp.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.quizapp.MainActivity;
import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.McqRvAdapter;
import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<QuestionModel> mQuestions;
    private TextView txtScore, txttotalScore, txtcorrect, txtincorrect,txtdurationinminutes;
    private MaterialButton buttonCancel;
    private String ExamId;
    private DocumentReference mRefDocumentExam;
    private FirebaseFirestore mFirestore;
    private String userID;
    private FirebaseUser user;
    private Float marks =  0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtScore = findViewById(R.id.txtScore);
        txtcorrect = findViewById(R.id.txtcorrect);
        txtincorrect = findViewById(R.id.txtincorrect);
        txttotalScore = findViewById(R.id.txttotalScore);
        buttonCancel = findViewById(R.id.buttonCancel);
        txtdurationinminutes = findViewById(R.id.txtdurationinminutes);

        Intent intent = getIntent();
        ExamId = intent.getStringExtra("Key");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mFirestore = FirebaseFirestore.getInstance();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        mRefDocumentExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM)
                .document(ExamId);
        mRefDocumentExam.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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

                        int correct = 0;

                        for (QuizModel quiz : quizModels) {
                            if (quiz.isState()) {
                                correct = correct + 1;
                            }
                        }
                        txtcorrect.setText(String.valueOf(correct));


                        int totalQuestions = quizModels.size();
                        txttotalScore.setText(String.valueOf(totalQuestions));

                        txtincorrect.setText(String.valueOf(totalQuestions - correct));

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

                        txtdurationinminutes.setText(examModel.getDurationInMinutes());
                        txtScore.setText(String.valueOf(marks));
                    }
                }
            }
        });


    }
}