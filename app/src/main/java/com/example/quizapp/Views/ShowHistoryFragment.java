package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.HistoryMcqRvAdapter;
import com.example.quizapp.adapters.McqRvAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShowHistoryFragment extends Fragment {

    private TextView textContent;
    private RecyclerView recyclerHistoryChoices;
    private ContentLoadingProgressBar progressBar;
    private HistoryMcqRvAdapter historyMcqRvAdapter;
    private ArrayList<QuestionModel> mQuestions;
    private ArrayList<ExamModel> mExam;
    private ArrayList<QuizModel> quiz;
    private int mOrder;
    private ExamModel NewexamModel;
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private String userID;

    public ShowHistoryFragment(ArrayList<QuestionModel> mQuestions, ArrayList<QuizModel> quiz, int mOrder) {
        this.mQuestions = mQuestions;
        this.quiz = quiz;
        this.mOrder = mOrder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_history, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerHistoryChoices = view.findViewById(R.id.recyclerHistoryChoices);
        textContent = view.findViewById(R.id.textContent);


        loadData(view);
        return view;
    }

    private void loadData(View view) {
        progressBar.show();
        if (mQuestions != null) {
            showQuestion(view);
            progressBar.hide();
        }
    }


    private void showQuestion(View view) {
        if (mOrder < mQuestions.size()) {
            textContent.setText(mQuestions.get(mOrder).getContent());

            historyMcqRvAdapter = new HistoryMcqRvAdapter(
                    R.layout.layout_item_answer,
                    mQuestions.get(mOrder).getChoices(),
                    quiz, mQuestions, mOrder
            );

            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerHistoryChoices.setLayoutManager(layoutManager);
            recyclerHistoryChoices.setAdapter(historyMcqRvAdapter);

        }
    }
}