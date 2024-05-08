package com.example.quizapp.Views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.McqRvAdapter;
import com.example.quizapp.adapters.QuestionAdapter;
import com.example.quizapp.ultils.AnswerDatabaseHelper;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
    private ExamModel examModel;
    private Date startDateTime, endDateTime;
    private int mOrder;
    private String mSelectedModuleID;
    private String questionId;
    private McqRvAdapter mcqRVAdapter;
    // Firebase
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private String userID = "";
    private CollectionReference mRefCollectionQuestions, mRefCollectionExam;
    private int correctAns = 0;
    public QuestionFragment() {
    }


    public QuestionFragment(int mOrder, String mSelectedModuleID) {
        this.mQuestions = new ArrayList<>();
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
    }


    public QuestionFragment(ArrayList<QuestionModel> mQuestions, int mOrder, String mSelectedModuleID) {
        this.mQuestions = mQuestions;
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
    }
    public QuestionFragment(ArrayList<QuestionModel> mQuestions, int mOrder, String mSelectedModuleID, int correctAns) {
        this.mQuestions = mQuestions;
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
        this.correctAns = correctAns;
    }

    public QuestionFragment(ArrayList<QuestionModel> mQuestions, int mOrder, String mSelectedModuleID, ExamModel examModel) {
        this.mQuestions = mQuestions;
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
        this.examModel = examModel;
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

        ArrayList<Integer> correct = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        if (examModel != null) {
             startDateTime = examModel.getStartDateTime();
        }
        ArrayList<Integer> Acorrect = new ArrayList<>();
        examModel.setQuestions(mQuestions);
        examModel.setStartDateTime(startDateTime);
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constant.Database.Exam.QUESTION, examModel.getQuestions());
        map.put(Constant.Database.Exam.STARTDATETIME, examModel.getStartDateTime());

        mRefCollectionExam = mFirestore.collection(Constant.Database.Quiz.COLLECTION_QUIZ).document(userID)
                .collection(Constant.Database.Exam.COLLECTION_EXAM);

        mRefCollectionExam.add(examModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String Examid = documentReference.getId();
                Map<String, Object> update = new HashMap<>();
                update.put(Constant.Database.Exam.ID, Examid);
                mRefCollectionExam.document(Examid).update(update);
            }
        });

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
                    if (selectId.equals(current)) {
                        correctAns++;
                        examModel.setMarks((float) (correctAns * 0.2));
                        Acorrect.add(1);
                    }else {
                        Acorrect.add(0);
                        examModel.setIncorrect(mQuestions.size() - correctAns);
                    }
                    examModel.setCorrect(correct);
                    examModel.setState("Đang làm bài");

                    if (mOrder < mQuestions.size() - 1) {
                        QuestionFragment nextFragment = new QuestionFragment(mQuestions, mOrder + 1, mSelectedModuleID, correctAns);
                        FragmentUtils.replaceFragmentQuestion(
                                getActivity().getSupportFragmentManager(),
                                nextFragment,
                                true);
                    } else {
                        FinishQuiz();
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


    private void FinishQuiz(){
        // back to fragment
        Bundle bundle = new Bundle();
        bundle.putInt("correctAns", correctAns);
    }

}