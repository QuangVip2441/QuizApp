package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editEmail, editPassword, editMSSV, editUserName, editPhone;
    private MaterialButton buttonSignUp;
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editMSSV = findViewById(R.id.editMSSV);
        editUserName = findViewById(R.id.editUserName);
        editPhone = findViewById(R.id.editPhone);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String mssv = editMSSV.getText().toString();
                String username = editUserName.getText().toString();
                String phone = editPhone.getText().toString();
                AddUser(mssv, username, email, phone);
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void AddUser(String mssv, String username, String email, String phone){
        Map<String, Object> user = new HashMap<>();
        user.put(Constant.Database.User.MSSV, mssv);
        user.put(Constant.Database.User.USERNAME, username);
        user.put(Constant.Database.User.EMAIL, email);
        user.put(Constant.Database.User.PHONE, phone);

        mRefCollectionUser = mFirestore.collection(Constant.Database.User.COLLECTION_USER);

        mRefCollectionUser.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                Map<String, Object> update = new HashMap<>();
                update.put(Constant.Database.User.ID, id);
                mRefCollectionUser.document(id).update(update);
            }
        });
    }
}