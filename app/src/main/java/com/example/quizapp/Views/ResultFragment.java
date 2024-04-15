package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.McqRvAdapter;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private ArrayList<QuestionModel> mQuestions;
    private TextView txtScore, txttotalScore, txtcorrect, txtincorrect;
    private AppCompatButton btnShare, btnreTakeQuiz;
    private McqRvAdapter mcqRVAdapter;
    private int correctAns = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtScore = view.findViewById(R.id.txtScore);
        txtcorrect = view.findViewById(R.id.txtcorrect);
        txtincorrect = view.findViewById(R.id.txtincorrect);
        txttotalScore = view.findViewById(R.id.txttotalScore);
        btnShare = view.findViewById(R.id.btnShare);
        btnreTakeQuiz = view.findViewById(R.id.btnreTakeQuiz);


        Bundle bundle = getArguments();
        if (bundle != null) {
//            correctAns = bundle.getInt("correctAns", 0);
            correctAns = 10;
        }

        txttotalScore.setText("/" + 10);
        txtScore.setText(correctAns  + "");
        txtcorrect.setText(correctAns + "");
        txtincorrect.setText(String.valueOf(10 - correctAns));

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnreTakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ModuleFragment moduleFragment = new ModuleFragment();
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.FragmentResult, moduleFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
    }
}