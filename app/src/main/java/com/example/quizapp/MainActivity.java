package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Views.AddModuleFragment;
import com.example.quizapp.Views.AddQuestionFragment;
import com.example.quizapp.Views.HomeFragment;
import com.example.quizapp.Views.ProfileActivity;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<QuestionModel> mQuestions;
    BottomNavigationView bottom_nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQuestions = new ArrayList<>();
        bottom_nav = findViewById(R.id.bottom_nav);
        replaceFragment(new HomeFragment());

        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.Home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.Profile) {
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentUtils.replaceFragment(
                getSupportFragmentManager(),
                fragment,
                false);
    }
}