package com.example.quizapp.Views;

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
import android.widget.TextView;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.QuestionAdapter;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
    private int mOrder;
    private String mSelectedModuleID;
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionQuestions;
    private int Test_timer = 600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        txtTimer = findViewById(R.id.txtTimer);
        recyclerNumberQuestion = findViewById(R.id.recyclerNumberQuestion);

        Intent intent = getIntent();
        String moduleID = intent.getStringExtra("Key");

        ArrayList<QuestionModel> mQuestions = new ArrayList<>();

        mFirestore = FirebaseFirestore.getInstance();
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
                            questionAdapter = new QuestionAdapter(R.layout.layout_item_header_number_question, mQuestions);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerNumberQuestion.setLayoutManager(layoutManager);
                            recyclerNumberQuestion.setAdapter(questionAdapter);

                            FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), new QuestionFragment(mQuestions,0, moduleID), true);
                        }
                    }
                });

        startQuizTimer(Test_timer);

    }

    private void startQuizTimer(int maxTimeInseconds){
        countDownTimer = new CountDownTimer(maxTimeInseconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getMinute = TimeUnit.SECONDS.toMinutes(millisUntilFinished / 1000);
                long getSecond = TimeUnit.SECONDS.toSeconds(millisUntilFinished / 1000) % 60;

                String generatetime = String.format(Locale.getDefault(),"%02d:%02d", getMinute, getSecond);


                txtTimer.setText(generatetime);
            }

            @Override
            public void onFinish() {
                // finish Quiz when time is finished
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
        ResultFragment resultFragment = new ResultFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentQuestion, resultFragment)
                .addToBackStack(null)
                .commit();
    }
}