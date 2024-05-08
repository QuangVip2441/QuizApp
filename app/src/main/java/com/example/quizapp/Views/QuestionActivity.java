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

public class QuestionActivity extends AppCompatActivity {

    private TextView txtTimer;
    private CountDownTimer countDownTimer;
    private RecyclerView recyclerNumberQuestion;
    private QuestionAdapter questionAdapter;
    private ArrayList<QuestionModel> mQuestions;
    private ArrayList<ModuleModel> mModels;
    private ExamModel examModel;
    private int mOrder;
    private FirebaseUser user;
    private String userID;
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionQuestions;
    private int Test_timer = 600;
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
                            //mQuestions.clear();

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



                                //listquestion.add(question);
                                mQuestions.add(question);

                            }
                            Collections.shuffle(mQuestions);
                            questionAdapter = new QuestionAdapter(R.layout.layout_item_header_number_question, mQuestions);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerNumberQuestion.setLayoutManager(layoutManager);
                            recyclerNumberQuestion.setAdapter(questionAdapter);

                            FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), new QuestionFragment(mQuestions,0, moduleID, examModel), true);

                        }
                    }
                });

        // Thời gian cho phép với 30 phút
        int timeAllowedInSeconds = 30 * 60;
        startQuizTimer(currentTimeMillis, timeAllowedInSeconds);
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
                // Finish Quiz when time is finished
                FinishQuiz();
            }
        };

        // Start Timer
        countDownTimer.start();
    }


    private void FinishQuiz(){
        // Gỡ bỏ Fragment Question
        Fragment fragmentquestion = getSupportFragmentManager().findFragmentById(R.id.fragmentQuestion);
        FragmentUtils.removeFragment(getSupportFragmentManager(), fragmentquestion);

        // Thêm ResultFragment vào activity
        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
        startActivity(intent);

    }

}