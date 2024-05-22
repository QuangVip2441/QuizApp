package com.example.quizapp.ultils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.R;



public class FragmentUtils {
    public static void replaceFragment(FragmentManager manager,
                                       Fragment fragment,
                                       boolean addToBackStack) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.commit();
    }

    public static void replaceFragmentQuestion(FragmentManager manager,
                                       Fragment fragment,
                                       boolean addToBackStack) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.fragment_question, fragment);
        transaction.commit();
    }
    public static void removeFragment(FragmentManager manager,Fragment fragment){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public static void replaceHistoryFragmentQuestion(FragmentManager manager,
                                               Fragment fragment,
                                               boolean addToBackStack) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.fragmentShowHistoryQuestion, fragment);
        transaction.commit();
    }
}
