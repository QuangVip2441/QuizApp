package com.example.quizapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.quizapp.R;
import com.example.quizapp.ultils.FragmentUtils;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        String moduleID = intent.getStringExtra("Key");


        FragmentUtils.replaceFragmentQuestion(getSupportFragmentManager(), new QuestionFragment(0, moduleID), true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HeaderFragment headerFragment = new HeaderFragment();
        fragmentTransaction.add(R.id.fragment_header, headerFragment);
        fragmentTransaction.commit();
    }
}