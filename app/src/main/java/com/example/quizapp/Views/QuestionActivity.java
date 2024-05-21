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
import com.example.quizapp.Models.ModuleModel;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity{

    private TextView txtTimer, txtnumberquestion;
    private CountDownTimer countDownTimer;
    private RecyclerView recyclerNumberQuestion;
    private QuestionAdapter questionAdapter;
    private ExamModel examModel;
    private TestAdministration administration;
    private FirebaseUser user;
    private int trueCount = 0;
    private int TimeAllow = 0;
    private Float marks =  0f;
    private int numberOfQuestions = 0;
    private int timeAllowedInSeconds = 0;
    private ArrayList<QuestionModel> mQuestions;
    private ArrayList<QuestionModel> Questions;
    private String moduleID;
    private String userID;
    private FirebaseFirestore mFirestore;
    private DatabaseHelper dbHelper;
    private CollectionReference mRefCollectionQuestions, mRefCollectionExam;
    private DocumentReference mRefDocumentExam, mRefDocumentModule, mRefDocumentTestAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        txtTimer = findViewById(R.id.txtTimer);
        txtnumberquestion = findViewById(R.id.txtnumberquestion);
        recyclerNumberQuestion = findViewById(R.id.recyclerNumberQuestion);

        dbHelper = new DatabaseHelper(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        Intent intent = getIntent();
        moduleID = intent.getStringExtra("Key");

        mQuestions = new ArrayList<>();
        Questions = new ArrayList<>();


        mFirestore = FirebaseFirestore.getInstance();
        // get time allowed
        mRefDocumentTestAdmin = mFirestore.collection(Constant.Database.TestAdministration.COLLECTION_TEST_ADMIN)
                .document(moduleID);
        mRefDocumentTestAdmin.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        TestAdministration testAdministration = new TestAdministration(
                                ((String) data.get(Constant.Database.TestAdministration.TEST_NAME)),
                                ((Long) data.get(Constant.Database.TestAdministration.NUMBERQUESTION)).intValue(),
                                ((Long) data.get(Constant.Database.TestAdministration.TIMEALLOWED)).intValue()
                        );
                        TimeAllow = testAdministration.getTimeAllowed();
                        numberOfQuestions = testAdministration.getNumberquestion();
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
                        // get module name
                        mRefDocumentModule = mFirestore
                                .collection(Constant.Database.Module.COLLECTION_MODULE).document(moduleID);
                        mRefDocumentModule.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> data = document.getData();
                                        ModuleModel module = new ModuleModel(
                                                (String) data.get(Constant.Database.Module.ID),
                                                (String) data.get(Constant.Database.Module.NAME),
                                                (String) data.get(Constant.Database.Module.INTRODUCTION),
                                                Long.parseLong(data.get(Constant.Database.Module.NUMBER_QUESTIONS).toString())
                                        );
                                        examModel.setTestname(module.getName());

                                    } else {
                                        Log.d(TAG,"document does not exist");
                                    }
                                } else {
                                    Log.d(TAG,"task is not successful");
                                }
                            }
                        });

                        // Thời gian cho phép với 30 phút
                        timeAllowedInSeconds = TimeAllow * 60;
                        // Tạo object và gán thời gian bắt đầu
                        examModel = new ExamModel();
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
                                            dbHelper.resetData();
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
                                            Questions.addAll(getRandomQuestions(mQuestions,numberOfQuestions));


                                            // add questions
                                            questionAdapter = new QuestionAdapter(R.layout.layout_item_header_number_question, Questions);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                            recyclerNumberQuestion.setLayoutManager(layoutManager);
                                            recyclerNumberQuestion.setAdapter(questionAdapter);

                                            QuestionFragment questionFragment = new QuestionFragment(Questions, 0, moduleID, examModel);
                                            FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), questionFragment, true);

                                            questionAdapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(String questionId, int position) {
                                                    QuestionFragment questionFragment = new QuestionFragment(Questions, position, moduleID, examModel);
                                                    FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), questionFragment, true);
                                                }
                                            });
                                        }
                                    }
                                });
                    } else {
                        Log.d(TAG,"document does not exist");
                    }
                } else {
                    Log.d(TAG,"task is not successful");
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

                    mRefDocumentExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                            .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM)
                            .document(examModel.getId());
                    mRefDocumentExam.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            String examid = examModel.getId();
                            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                            intent.putExtra("Key", examid);
                            startActivity(intent);
                        }
                    });
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
                finishQuiz();
            }
        };
        // Start Timer
        countDownTimer.start();
    }

}