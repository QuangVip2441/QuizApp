package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.MainActivity;
import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.McqRvAdapter;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestionFragment extends Fragment {

    private TextView textContent;
    private RecyclerView recyclerChoices;
    private MaterialButton buttonPrevious;
    private MaterialButton buttonNext;
    private ContentLoadingProgressBar progressBar;
    private ArrayList<QuestionModel> mQuestions;
    private ArrayList<QuizModel> quiz;
    private ExamModel NewexamModel;
    private Date startDateTime;
    private int mOrder;
    private String mSelectedModuleID;
    private String questionId;
    private float marks = 0.0F;
    private McqRvAdapter mcqRVAdapter;
    // Firebase
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private String userID;
    private DocumentReference mRefDocumentExam;

    public QuestionFragment() {
        this.quiz = new ArrayList<QuizModel>(); // Initialize the ArrayList
    }

    public QuestionFragment(ArrayList<QuestionModel> mQuestions, int mOrder, String mSelectedModuleID, ExamModel examModel, ArrayList<QuizModel> quiz) {
        this.mQuestions = mQuestions;
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
        this.NewexamModel = examModel;
        this.quiz = quiz;
    }

    public QuestionFragment(ArrayList<QuestionModel> mQuestions, int mOrder, String mSelectedModuleID, ExamModel examModel) {
        this.mQuestions = mQuestions;
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
        this.NewexamModel = examModel;
        this.quiz = new ArrayList<QuizModel>(); // Initialize the ArrayList
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        textContent = view.findViewById(R.id.textContent);
        recyclerChoices = view.findViewById(R.id.recyclerChoices);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);
        buttonNext = view.findViewById(R.id.buttonNext);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mFirestore = FirebaseFirestore.getInstance();


        if (NewexamModel != null) {
             startDateTime = NewexamModel.getStartDateTime();
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition = mcqRVAdapter.getSelectedItem();
                if (selectedPosition < 0){
                    Toast.makeText(requireContext(),"Answer is not Checked",Toast.LENGTH_SHORT).show();
                }else {
                    QuestionModel currentQuestion = mQuestions.get(mOrder);
                    ChoiceModel selectedChoice = currentQuestion.getChoices().get(selectedPosition);
                    String selectId = selectedChoice.getId();
                    String current = currentQuestion.getCorrect();

                    if (mQuestions != null && mOrder >= 0 && mOrder < mQuestions.size()) {
                        if (selectId.equals(current)) {
                            quiz.add(new QuizModel(mQuestions.get(mOrder).getId(), mQuestions.get(mOrder).getContent(), selectId, current, true));
                            NewexamModel.setQuizs(quiz);
                            NewexamModel.setMarks(marks = (float) (marks + 0.2));
                        } else {
                            quiz.add(new QuizModel(mQuestions.get(mOrder).getId(), mQuestions.get(mOrder).getContent(), selectId, current, false));
                            NewexamModel.setQuizs(quiz);
                        }
                        NewexamModel.setState("Đang làm bài");
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(Constant.Database.Exam.QUESTION, NewexamModel.getQuizs());
                    map.put(Constant.Database.Exam.STATE, NewexamModel.getState());

                    mRefDocumentExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                            .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM)
                            .document(NewexamModel.getId());
                    mRefDocumentExam.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Inserted");
                        }
                    });

                    if (mOrder < mQuestions.size() - 1) {
                        QuestionFragment nextFragment = new QuestionFragment(mQuestions, mOrder + 1, mSelectedModuleID, NewexamModel, quiz);
                        FragmentUtils.replaceFragmentQuestion(
                                getActivity().getSupportFragmentManager(),
                                nextFragment,
                                true);
                    }else {
                        finishQuiz();
                    }
                }
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
//                if (mOrder >= 1) {
//                    String selectedChoiceId = dbHelper.getSelectedChoiceId(questionId);
//                    if (selectedChoiceId != null) {
//
//                    }
//                }
            }
        });

        progressBar.show();
        loadData(view);
        progressBar.hide();
        return view;
    }

    private void loadData(View view) {
        if (mQuestions != null) {
            showQuestion(view);
        }
    }

    private void showQuestion(View view) {
        if (mOrder < mQuestions.size()) {
            QuestionModel question = mQuestions.get(mOrder);
            textContent.setText(mQuestions.get(mOrder).getContent());
            questionId = question.getId();

            mcqRVAdapter = new McqRvAdapter(
                    R.layout.layout_item_answer,
                    mQuestions.get(mOrder).getChoices()
            );


            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerChoices.setLayoutManager(layoutManager);
            recyclerChoices.setAdapter(mcqRVAdapter);
        }
    }


    public void finishQuiz() {
        NewexamModel.setState("Hoàn thành");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());
        Date startTime = NewexamModel.getStartDateTime();
        long startTimeMillis = startTime.getTime();
        Date currentTime;
        try {
            currentTime = dateFormat.parse(currentDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long currentTimeMillis = currentTime.getTime();
        long durationMillis = currentTimeMillis - startTimeMillis;
        // Convert milliseconds to minutes
        long durationMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis);
        // Use durationMinutes as needed

        NewexamModel.setEndDateTime(currentTime);
        NewexamModel.setDurationInMinutes(durationMinutes);


        HashMap<String, Object> map = new HashMap<>();
        map.put(Constant.Database.Exam.ENDDATETIME, NewexamModel.getEndDateTime());
        map.put(Constant.Database.Exam.DURATION_IN_MINUTES, NewexamModel.getDurationInMinutes());
        map.put(Constant.Database.Exam.MARKS, NewexamModel.getMarks());
        map.put(Constant.Database.Exam.STATE, NewexamModel.getState());

        mRefDocumentExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ)
                .document(userID).collection(Constant.Database.Exam.COLLECTION_EXAM)
                .document(NewexamModel.getId());
        mRefDocumentExam.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}