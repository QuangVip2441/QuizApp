package com.example.quizapp.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

        addTestAdmin("gH0DPmPCXqyd2tLvNFo2","IU01", 10);
        addTestAdmin("nhzkAlsrBsEYuMwLt4tX","IU02", 10);
        addTestAdmin("tKY7UM75QxiaEso7cg1P","IU03", 10);
        addTestAdmin("RIcs3Vkbw67snMZDrQph","IU04", 10);
        addTestAdmin("kVXcvqNDESn0XcGFZOwJ","IU05", 5);
        addTestAdmin("yPcUWum0mjuL7mld9Yhy","IU06", 5);
        addTestAdmin("","Sum Test", 50);

    }

    private void addTestAdmin(String moduleId, String test_name, int test_numberQuestions) {
        Map<String, Object> testadmin = new HashMap<>();
        testadmin.put(Constant.Database.TestAdministration.TEST_NAME, test_name);
        testadmin.put(Constant.Database.TestAdministration.MODULEID, moduleId);
        testadmin.put(Constant.Database.TestAdministration.TEST_GET_NUMBER_QUESTIONS, test_numberQuestions);

        mRefCollectionTestAdmin = mFirestore.collection(Constant.Database.TestAdministration.COLLECTION_TEST_ADMIN);
        mRefCollectionTestAdmin.add(testadmin).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                Map<String, Object> update = new HashMap<>();
                update.put(Constant.Database.Module.ID, id);
                mRefCollectionTestAdmin.document(id).update(update);
            }
        });
    }
}