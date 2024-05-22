package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.quizapp.ultils.Constant.Database.Module.COLLECTION_MODULE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.example.quizapp.MainActivity;
import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.Models.TestAdministration;
import com.example.quizapp.R;
import com.example.quizapp.adapters.QuestionAdapter;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.DatabaseHelper;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RandomQuestionActivity extends AppCompatActivity {

    private TextView txtTimer;
    private CountDownTimer countDownTimer;
    private RecyclerView recyclerNumberQuestion;
    private QuestionAdapter questionAdapter;
    private ExamModel examModel;
    private TestAdministration administration;
    private FirebaseUser user;
    private int trueCount = 0;
    private int TimeAllow = 0;
    private int timeAllowedInSeconds = 0;
    private ArrayList<QuestionModel> mQuestions;
    private ArrayList<TestAdministration> mtestadmin;
    private ArrayList<QuestionModel> mQuestionsModule1;
    private ArrayList<QuestionModel> mQuestionsModule2;
    private ArrayList<QuestionModel> mQuestionsModule3;
    private ArrayList<QuestionModel> mQuestionsModule4;
    private ArrayList<QuestionModel> mQuestionsModule5;
    private ArrayList<QuestionModel> mQuestionsModule6;
    private String IDmodule1, IDmodule2, IDmodule3, IDmodule4, IDmodule5, IDmodule6;
    private String userID;
    private FirebaseFirestore mFirestore;
    private DatabaseHelper dbHelper;
    private Float marks =  0f;
    private CollectionReference mRefCollectionQuestions, mRefCollectionExam, mRefCollectionTestAdmin;
    private DocumentReference mRefDocumentExam, mRefDocumentTestAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_question);

        txtTimer = findViewById(R.id.txtTimer);
        recyclerNumberQuestion = findViewById(R.id.recyclerNumberQuestion);

        dbHelper = new DatabaseHelper(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        IDmodule1 = "gH0DPmPCXqyd2tLvNFo2";
        IDmodule2 = "nhzkAlsrBsEYuMwLt4tX";
        IDmodule3 = "tKY7UM75QxiaEso7cg1P";
        IDmodule4 = "RIcs3Vkbw67snMZDrQph";
        IDmodule5 = "kVXcvqNDESn0XcGFZOwJ";
        IDmodule6 = "yPcUWum0mjuL7mld9Yhy";

        mQuestions = new ArrayList<>();
        mtestadmin = new ArrayList<>();
        mQuestionsModule1  = new ArrayList<>();
        mQuestionsModule2  = new ArrayList<>();
        mQuestionsModule3  = new ArrayList<>();
        mQuestionsModule4  = new ArrayList<>();
        mQuestionsModule5  = new ArrayList<>();
        mQuestionsModule6  = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        examModel = new ExamModel();

        mRefDocumentTestAdmin = mFirestore.collection(Constant.Database.TestAdministration.COLLECTION_TEST_ADMIN).document("ZayqBjRnPr1GXBoyPdOh");
        mRefDocumentTestAdmin.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    dbHelper.resetDatabase();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        mtestadmin.clear();
                        Map<String, Object> data = document.getData();
                        TestAdministration testAdministration = new TestAdministration(
                                ((String) data.get(Constant.Database.TestAdministration.TEST_NAME)),
                                ((Long) data.get(Constant.Database.TestAdministration.NUMBERQUESTION)).intValue(),
                                ((Long) data.get(Constant.Database.TestAdministration.TIMEALLOWED)).intValue()
                        );

                        TimeAllow = testAdministration.getTimeAllowed();
                        examModel.setTestname(testAdministration.getTest_name());
                        examModel.setModuleId("ZayqBjRnPr1GXBoyPdOh");


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
                        // Thời gian cho phép với 30 phút
                        timeAllowedInSeconds = TimeAllow * 60;
                        // Tạo object và gán thời gian bắt đầu
                        try {
                            Date startDateTime = dateFormat.parse(currentDateTime);
                            // Thiết lập startDateTime
                            examModel.setStartDateTime(startDateTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
                    }
                }
            }
        });

        mRefCollectionTestAdmin = mFirestore.collection(Constant.Database.TestAdministration.COLLECTION_TEST_ADMIN);
        mRefCollectionTestAdmin.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mtestadmin.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();

                        TestAdministration testAdministration = new TestAdministration(
                                (String) data.get(Constant.Database.TestAdministration.MODULEID),
                                (String) data.get(Constant.Database.TestAdministration.TEST_NAME),
                                ((Long) data.get(Constant.Database.TestAdministration.TEST_GET_NUMBER_QUESTIONS)).intValue(),
                                ((Long) data.get(Constant.Database.TestAdministration.NUMBERQUESTION)).intValue(),
                                ((Long) data.get(Constant.Database.TestAdministration.TIMEALLOWED)).intValue()
                        );

                        mtestadmin.add(testAdministration);
                    }

                    Map<String, Integer> moduleQuestionMap = new HashMap<>();
                    moduleQuestionMap.put(IDmodule1, 0);
                    moduleQuestionMap.put(IDmodule2, 0);
                    moduleQuestionMap.put(IDmodule3, 0);
                    moduleQuestionMap.put(IDmodule4, 0);
                    moduleQuestionMap.put(IDmodule5, 0);
                    moduleQuestionMap.put(IDmodule6, 0);

                    // Lặp qua danh sách mtestadmin để cập nhật số câu hỏi cho từng module
                    for (TestAdministration testAdmin : mtestadmin) {
                        if (moduleQuestionMap.containsKey(testAdmin.getId())) {
                            moduleQuestionMap.put(testAdmin.getId(), testAdmin.getTest_numberQuestions());
                        }
                    }
                    // ============================================================1

                    mRefCollectionQuestions = mFirestore
                            .collection(COLLECTION_MODULE)
                            .document(IDmodule1)
                            .collection(Constant.Database.Question.COLLECTION_QUESTIONS);

                    mRefCollectionQuestions
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        mQuestionsModule1.clear();
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

                                            mQuestionsModule1.add(question);

                                        }
                                        Collections.shuffle(mQuestionsModule1);
                                        mQuestions.addAll(getRandomQuestions(mQuestionsModule1, moduleQuestionMap.get(IDmodule1)));


                                        // ============================================================2
                                        mRefCollectionQuestions = mFirestore
                                                .collection(COLLECTION_MODULE)
                                                .document(IDmodule2)
                                                .collection(Constant.Database.Question.COLLECTION_QUESTIONS);

                                        mRefCollectionQuestions
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            mQuestionsModule2.clear();
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

                                                                mQuestionsModule2.add(question);

                                                            }
                                                            Collections.shuffle(mQuestionsModule2);
                                                            mQuestions.addAll(getRandomQuestions(mQuestionsModule2, moduleQuestionMap.get(IDmodule2)));

                                                            // ============================================================3
                                                            mRefCollectionQuestions = mFirestore
                                                                    .collection(COLLECTION_MODULE)
                                                                    .document(IDmodule3)
                                                                    .collection(Constant.Database.Question.COLLECTION_QUESTIONS);

                                                            mRefCollectionQuestions
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                mQuestionsModule3.clear();
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

                                                                                    mQuestionsModule3.add(question);

                                                                                }
                                                                                Collections.shuffle(mQuestionsModule3);
                                                                                mQuestions.addAll(getRandomQuestions(mQuestionsModule3, moduleQuestionMap.get(IDmodule3)));

                                                                                //==============================================================4
                                                                                mRefCollectionQuestions = mFirestore
                                                                                        .collection(COLLECTION_MODULE)
                                                                                        .document(IDmodule4)
                                                                                        .collection(Constant.Database.Question.COLLECTION_QUESTIONS);

                                                                                mRefCollectionQuestions
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    mQuestionsModule4.clear();
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

                                                                                                        mQuestionsModule4.add(question);

                                                                                                    }
                                                                                                    Collections.shuffle(mQuestionsModule4);
                                                                                                    mQuestions.addAll(getRandomQuestions(mQuestionsModule4, moduleQuestionMap.get(IDmodule4)));

                                                                                                    //===========================================================5
                                                                                                    mRefCollectionQuestions = mFirestore
                                                                                                            .collection(COLLECTION_MODULE)
                                                                                                            .document(IDmodule5)
                                                                                                            .collection(Constant.Database.Question.COLLECTION_QUESTIONS);

                                                                                                    mRefCollectionQuestions
                                                                                                            .get()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        mQuestionsModule5.clear();
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

                                                                                                                            mQuestionsModule5.add(question);

                                                                                                                        }
                                                                                                                        Collections.shuffle(mQuestionsModule5);
                                                                                                                        mQuestions.addAll(getRandomQuestions(mQuestionsModule5, moduleQuestionMap.get(IDmodule5)));

                                                                                                                        //===============================================================6
                                                                                                                        mRefCollectionQuestions = mFirestore
                                                                                                                                .collection(COLLECTION_MODULE)
                                                                                                                                .document(IDmodule6)
                                                                                                                                .collection(Constant.Database.Question.COLLECTION_QUESTIONS);

                                                                                                                        mRefCollectionQuestions
                                                                                                                                .get()
                                                                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                            mQuestionsModule6.clear();
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

                                                                                                                                                mQuestionsModule6.add(question);

                                                                                                                                            }
                                                                                                                                            Collections.shuffle(mQuestionsModule6);
                                                                                                                                            mQuestions.addAll(getRandomQuestions(mQuestionsModule6, moduleQuestionMap.get(IDmodule6)));


                                                                                                                                            Collections.shuffle(mQuestions);
                                                                                                                                            examModel.setmQuestion(mQuestions);
                                                                                                                                            // add questions
                                                                                                                                            questionAdapter = new QuestionAdapter(R.layout.layout_item_header_number_question, mQuestions);
                                                                                                                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                                                                                                                            recyclerNumberQuestion.setLayoutManager(layoutManager);
                                                                                                                                            recyclerNumberQuestion.setAdapter(questionAdapter);

                                                                                                                                            QuestionFragment questionFragment = new QuestionFragment(mQuestions, 0, examModel, dbHelper);
                                                                                                                                            FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), questionFragment, true);

                                                                                                                                            questionAdapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
                                                                                                                                                @Override
                                                                                                                                                public void onItemClick(String questionId, int position) {
                                                                                                                                                    QuestionFragment questionFragment = new QuestionFragment(mQuestions, position, examModel, dbHelper);
                                                                                                                                                    FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), questionFragment, true);
                                                                                                                                                }
                                                                                                                                            });

                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });

                }
            }
        });


    }


    private ArrayList<QuestionModel> getRandomQuestions(ArrayList<QuestionModel> questionList, int numberOfQuestions) {
        ArrayList<QuestionModel> randomQuestions = new ArrayList<>();
        Collections.shuffle(questionList); // Trộn ngẫu nhiên danh sách câu hỏi
        for (int i = 0; i < Math.min(numberOfQuestions, questionList.size()); i++) {
            randomQuestions.add(questionList.get(i));
        }
        return randomQuestions;
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
                finishQuiz();
            }
        };
        // Start Timer
        countDownTimer.start();
    }

    public void finishQuiz() {
        mRefDocumentExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM)
                .document(examModel.getId());
        mRefDocumentExam.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> documentData = document.getData();
                        ArrayList<QuizModel> quizs = new ArrayList<>();
                        ArrayList<HashMap<String, Object>> temp = (ArrayList<HashMap<String, Object>>) documentData.get(Constant.Database.Exam.QUESTION);
                        for (HashMap<String, Object> i : temp) {
                            quizs.add(new QuizModel(
                                    (String) i.get("id"),
                                    (String) i.get("questioncontent"),
                                    (String) i.get("idanswer"),
                                    (String) i.get("idcorrect"),
                                    (boolean) i.get("state")
                            ));
                        }


                        if (quizs.size() > 10){
                            for (QuizModel quiz : quizs) {
                                if (quiz.isState()) {
                                    trueCount = trueCount + 2;
                                }
                            }
                            double scoreOutOfTen = ((double) trueCount / 10);
                            // Quy đổi điểm về thang điểm 10
                            marks = (float) Math.round(scoreOutOfTen);
                        }else {
                            for (QuizModel quiz : quizs) {
                                if (quiz.isState()) {
                                    trueCount = trueCount + 1;
                                }
                            }
                            marks = (float) trueCount;
                        }


                    }

                    examModel.setState("Hoàn thành");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    String currentDateTime = dateFormat.format(new Date());
                    Date startTime = examModel.getStartDateTime();
                    if (startTime == null) {
                        Log.d(TAG, "NewexamModel.getStartDateTime() is null");
                    }
                    long startTimeMillis = startTime.getTime();
                    Date currentTime;
                    try {
                        currentTime = dateFormat.parse(currentDateTime);
                    } catch (ParseException e) {
                        return;
                    }
                    long currentTimeMillis = currentTime.getTime();
                    // Convert milliseconds to minutes
                    long durationMillis = currentTimeMillis - startTimeMillis;
                    long durationMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis - startTimeMillis);
                    long remainingMillis = durationMillis - TimeUnit.MINUTES.toMillis(durationMinutes);
                    long durationSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis);

                    String durationString = String.format("%d minutes, %d seconds", durationMinutes, durationSeconds);

                    examModel.setEndDateTime(currentTime);
                    examModel.setDurationInMinutes(durationString);
                    examModel.setMarks(marks);

                    HashMap<String, Object> map = new HashMap<>();
                    map.put(Constant.Database.Exam.ENDDATETIME, examModel.getEndDateTime());
                    map.put(Constant.Database.Exam.DURATION_IN_MINUTES, examModel.getDurationInMinutes());
                    map.put(Constant.Database.Exam.MARKS, examModel.getMarks());
                    map.put(Constant.Database.Exam.STATE, examModel.getState());
                    map.put(Constant.Database.Exam.TEST_NAME, examModel.getTestname());
                    map.put(Constant.Database.Exam.MODULEID, examModel.getModuleId());
                    map.put(Constant.Database.Exam.LISTQUESTIONS, examModel.getmQuestion());

                    mRefDocumentExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                            .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM)
                            .document(examModel.getId());
                    mRefDocumentExam.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            String id = examModel.getId();
                            Intent intent = new Intent(RandomQuestionActivity.this, ResultActivity.class);
                            intent.putExtra("Key", id);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

    }
}