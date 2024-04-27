package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditPasswordActivity extends AppCompatActivity {

    private ShapeableImageView imageAvt;
    private EditText txteditUsedPassword, txteditnewPassword, txteditrepeatPassword;
    private Button btnEditPassword;
    private FirebaseUser user;
    private String Email = "";
    private String Password = "";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        txteditUsedPassword = findViewById(R.id.txteditUsedPassword);
        txteditnewPassword = findViewById(R.id.txteditnewPassword);
        txteditrepeatPassword = findViewById(R.id.txteditrepeatPassword);
        imageAvt = findViewById(R.id.imageAvt);
        btnEditPassword = findViewById(R.id.btnEditPassword);


        user = FirebaseAuth.getInstance().getCurrentUser();
        Email = user.getEmail();
        mAuth = FirebaseAuth.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images").child(Email);

        if (txteditnewPassword.getText().toString() == txteditrepeatPassword.getText().toString()){
            String currentPassword = txteditnewPassword.getText().toString();
            AuthCredential credential = EmailAuthProvider.getCredential(Email, currentPassword);

            user.reauthenticate(credential).addOnSuccessListener(new OnCompleteListener<Void>() {
                @Override
                public void onSuccess(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        String newPassword = txteditnewPassword.getText().toString();
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }else {
                        Log.d(TAG, "Error re-authenticating user.");
                        Toast.makeText(EditPasswordActivity.this,"Mật khẩu mới không trùng khớp", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }else
        {
            Toast.makeText(EditPasswordActivity.this,"Mật khẩu mới không trùng khớp", Toast.LENGTH_LONG).show();
        }


        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}