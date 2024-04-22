package com.example.quizapp.Views;

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
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.McqRvAdapter;
import com.example.quizapp.adapters.QuestionAdapter;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.util.concurrent.TimeUnit;

public class QuestionFragment extends Fragment {

    private TextView textContent, QuizTimer, currentQuestion, totalQuestion;
    private RecyclerView recyclerChoices;
    private MaterialButton buttonPrevious;
    private MaterialButton buttonNext;
    private CountDownTimer countDownTimer;
    private ContentLoadingProgressBar progressBar;
    private ArrayList<QuestionModel> mQuestions;
    private int mOrder;
    private String mSelectedModuleID;

    private McqRvAdapter mcqRVAdapter;
    private QuestionAdapter questionAdapter;
    // Firebase
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionQuestions;
    private int Test_timer = 600;

    private int correctAns = 0;
    private int numberOfQuestionsToSelect = 10;
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            correctAns = bundle.getInt("correctAns", 0);
        }

        //currentQuestion.setText("Question " + (mOrder + 1));
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
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("correctAns", correctAns);
                    if (mOrder < mQuestions.size() - 1) {
                        QuestionFragment nextFragment = new QuestionFragment(mQuestions, mOrder + 1, mSelectedModuleID);
                        nextFragment.setArguments(bundle);
                        FragmentUtils.replaceFragmentQuestion(
                                getActivity().getSupportFragmentManager(),
                                nextFragment,
                                true);
                    } else {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        FinishQuiz();
                    }
                }
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOrder >= 1) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    currentQuestion.setText("Question " + (mOrder - 1));
                }

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
            textContent.setText(mQuestions.get(mOrder).getContent());

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