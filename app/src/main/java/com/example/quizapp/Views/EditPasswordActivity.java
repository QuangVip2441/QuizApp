package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

public class EditPasswordActivity extends AppCompatActivity {

    private ShapeableImageView imageAvt;
    private EditText txteditUsedPassword, txteditnewPassword, txteditrepeatPassword;
    private Button btnEditPassword;
    private ContentLoadingProgressBar progressBar;
    private FirebaseUser user;
    private String Email = "";
    private String NewPassword = "";
    private String repeatPassword = "";
    private String currentPassword = "";
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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        Email = user.getEmail();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images").child(userID);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(imageAvt);
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPassword = txteditnewPassword.getText().toString();
                repeatPassword = txteditrepeatPassword.getText().toString();
                if (NewPassword.equals(repeatPassword)){
                    currentPassword = txteditUsedPassword.getText().toString();
                    AuthCredential credential = EmailAuthProvider.getCredential(Email, currentPassword);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(NewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(EditPasswordActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                Log.d(TAG, "Error re-authenticating user.");
                                Toast.makeText(EditPasswordActivity.this,"Mật khẩu mới không trùng khớp", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }else {
                    Toast.makeText(EditPasswordActivity.this,"Mật khẩu mới không trùng khớp", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}