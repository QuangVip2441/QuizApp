package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.quizapp.ultils.Constant.Database.Module.COLLECTION_MODULE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.ModuleModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.QuestionAdapter;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {

    private TextView txtTimer;
    private CountDownTimer countDownTimer;
    private RecyclerView recyclerNumberQuestion;
    private QuestionAdapter questionAdapter;
    private ExamModel examModel;
    private FirebaseUser user;
    private String userID;
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionQuestions, mRefCollectionExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        txtTimer = findViewById(R.id.txtTimer);
        recyclerNumberQuestion = findViewById(R.id.recyclerNumberQuestion);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        Intent intent = getIntent();
        String moduleID = intent.getStringExtra("Key");

        ArrayList<QuestionModel> mQuestions = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        // Tạo thời gian bắt đầu
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());
        Date date = null;
        try {
            date = dateFormat.parse(currentDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long currentTimeMillis = date.getTime();
        // Tạo object và gán thời gian bắt đầu
        examModel = new ExamModel();
        try {
            Date startDateTime = dateFormat.parse(currentDateTime);
            // Thiết lập startDateTime
            examModel.setStartDateTime(startDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Thời gian cho phép với 30 phút
        int timeAllowedInSeconds = 30 * 60;
        startQuizTimer(currentTimeMillis, timeAllowedInSeconds);
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constant.Database.Exam.STARTDATETIME, examModel.getStartDateTime());


        mRefCollectionExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ).document(userID)
                .collection(Constant.Database.Exam.COLLECTION_EXAM);

        mRefCollectionExam.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                Map<String, Object> update = new HashMap<>();
                examModel.setId(id);
                update.put(Constant.Database.Exam.ID, id);
                mRefCollectionExam.document(id).update(update);
            }
        });

        mRefCollectionQuestions = mFirestore
                .collection(COLLECTION_MODULE)
                .document(moduleID)
                .collection(Constant.Database.Question.COLLECTION_QUESTIONS);

        mRefCollectionQuestions
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            mQuestions.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                ArrayList<ChoiceModel> choices = new ArrayList<>();

                                ArrayList<HashMap<String, Object>> temp = (ArrayList<HashMap<String, Object>>) data.get(Constant.Database.Question.CHOICES);

                                for (HashMap<String, Object> i : temp) {
                                    choices.add(new ChoiceModel(
                                            (String) i.get(Constant.Database.Choice.ID),
                                            (String) i.get(Constant.Database.Choice.CONTENT)
                                    ));
                                }

                                QuestionModel question = new QuestionModel(
                                        (String) data.get(Constant.Database.Question.ID),
                                        (String) data.get(Constant.Database.Question.CONTENT),
                                        choices,
                                        (String) data.get(Constant.Database.Question.CORRECT)
                                );

                                mQuestions.add(question);

                            }
                            Collections.shuffle(mQuestions);
                            // add questions
                            questionAdapter = new QuestionAdapter(R.layout.layout_item_header_number_question, mQuestions);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerNumberQuestion.setLayoutManager(layoutManager);
                            recyclerNumberQuestion.setAdapter(questionAdapter);

                            QuestionFragment questionFragment = new QuestionFragment(mQuestions, 0, moduleID, examModel);
                            FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), questionFragment, true);
                        }
                    }
                });

    }



    private void startQuizTimer(long currentTimeMillis, int timeAllowedInSeconds) {
        // Chuyển thời gian cho phép từ giây sang mili giây
        long timeAllowedMillis = timeAllowedInSeconds * 1000;
        // Tính thời gian kết thúc làm bài
        long endTimeMillis = currentTimeMillis + timeAllowedMillis;

        countDownTimer = new CountDownTimer(endTimeMillis - currentTimeMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long getSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                String generateTime = String.format(Locale.getDefault(),"%02d:%02d", getMinute, getSecond);
                txtTimer.setText(generateTime);
            }
            @Override
            public void onFinish() {
//                Date dateendtime = new Date(endTimeMillis);
//                Date date = new Date(endTimeMillis - currentTimeMillis);
//                // Finish Quiz when time is finished
//                examModel.setEndDateTime(dateendtime);
//                examModel.setDurationInMinutes(date);
                finishQuiz();
            }
        };
        // Start Timer
        countDownTimer.start();
    }

    private void finishQuiz() {
        // Gọi phương thức finishQuiz từ fragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentQuestion);
        if (fragment instanceof QuestionFragment) {
            ((QuestionFragment) fragment).finishQuiz();
        }
//        HashMap<String, Object> map = new HashMap<>();
//        map.put(Constant.Database.Exam.INCORRECT, examModel.getIncorrect());
//        map.put(Constant.Database.Exam.QUESTION, examModel.getQuestions());
//        map.put(Constant.Database.Exam.CORRECT, examModel.getCorrect());
//        map.put(Constant.Database.Exam.DURATION_IN_MINUTES, examModel.getDurationInMinutes());
//        map.put(Constant.Database.Exam.STARTDATETIME, examModel.getStartDateTime());
//        map.put(Constant.Database.Exam.ENDDATETIME, examModel.getEndDateTime());
//        map.put(Constant.Database.Exam.STATE, examModel.getState());
//        map.put(Constant.Database.Exam.MARKS, examModel.getMarks());
//
//
//        mRefCollectionExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ).document(userID)
//                .collection(Constant.Database.Exam.COLLECTION_EXAM);
//
//        mRefCollectionExam.add(examModel)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        String Examid = documentReference.getId();
//                        Map<String, Object> update = new HashMap<>();
//                        update.put(Constant.Database.Exam.ID, Examid);
//                        mRefCollectionExam.document(Examid).set(update) // Use set() instead of update()
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        // Document updated successfully
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // Handle failure
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle failure
//                    }
//                });


        // Thêm ResultFragment vào activity
//        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
//        startActivity(intent);
    }


}