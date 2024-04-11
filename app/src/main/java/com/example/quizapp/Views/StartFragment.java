package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizapp.R;
import com.example.quizapp.ultils.FragmentUtils;


public class StartFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AppCompatButton btnStartQuiz = view.findViewById(R.id.btnStartQuiz);
        final AppCompatButton btnQuit = view.findViewById(R.id.btnQuit);

        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    String moduleID = bundle.getString("Key");
                    FragmentUtils.replaceFragment(
                            getActivity().getSupportFragmentManager(),
                            new QuestionFragment(0, moduleID),
                            false
                    );
                }else {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
    }
}