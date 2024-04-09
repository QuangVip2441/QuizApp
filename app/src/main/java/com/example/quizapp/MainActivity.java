package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Views.HomeFragment;
import com.example.quizapp.ultils.FragmentUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<QuestionModel> mQuestions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQuestions = new ArrayList<>();

        replaceFragment(new HomeFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentUtils.replaceFragment(
                getSupportFragmentManager(),
                fragment,
                false);
    }
}