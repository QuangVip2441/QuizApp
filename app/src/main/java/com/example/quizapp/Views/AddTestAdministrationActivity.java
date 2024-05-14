package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.quizapp.R;
import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTestAdministrationActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionTestAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test_administration);
        mFirestore = FirebaseFirestore.getInstance();

        addTestAdmin("gH0DPmPCXqyd2tLvNFo2","IU01", 10, 30);
        addTestAdmin("nhzkAlsrBsEYuMwLt4tX","IU02", 10, 30);
        addTestAdmin("tKY7UM75QxiaEso7cg1P","IU03", 10, 30);
        addTestAdmin("RIcs3Vkbw67snMZDrQph","IU04", 10, 30);
        addTestAdmin("kVXcvqNDESn0XcGFZOwJ","IU05", 5, 30);
        addTestAdmin("yPcUWum0mjuL7mld9Yhy","IU06", 5, 30);
        //addTestAdmin("","Sum Test", 50, 30);

    }

    private void addTestAdmin(String moduleId, String test_name, int test_numberQuestions, int timeallowed) {
        Map<String, Object> testadmin = new HashMap<>();
        testadmin.put(Constant.Database.TestAdministration.TEST_NAME, test_name);
        testadmin.put(Constant.Database.TestAdministration.MODULEID, moduleId);
        testadmin.put(Constant.Database.TestAdministration.TEST_GET_NUMBER_QUESTIONS, test_numberQuestions);
        testadmin.put(Constant.Database.TestAdministration.TIMEALLOWED, timeallowed);


        mRefCollectionTestAdmin = mFirestore.collection(Constant.Database.TestAdministration.COLLECTION_TEST_ADMIN);
        mRefCollectionTestAdmin.document(moduleId).set(testadmin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "added successfully");
            }
        });
    }
}