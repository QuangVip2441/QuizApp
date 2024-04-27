package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ShapeableImageView imageAvt;
    private EditText txteditName, txteditPhone, txteditMSSV;
    private Button btnEditProfile;
    private ContentLoadingProgressBar progressBar;
    private FirebaseUser user;
    private String Email = "";
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        txteditName = findViewById(R.id.txteditName);
        txteditPhone = findViewById(R.id.txteditPhone);
        txteditMSSV = findViewById(R.id.txteditMSSV);
        imageAvt = findViewById(R.id.imageAvt);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        Toolbar toolbar = findViewById(R.id.toolbaredit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        Email = user.getEmail();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images").child(userID);

        progressBar.show();
        mFirestore.collection(Constant.Database.User.COLLECTION_USER).document(Email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String mssv = (String) data.get(Constant.Database.User.MSSV);
                        String phone = (String) data.get(Constant.Database.User.PHONE);
                        String username = (String) data.get(Constant.Database.User.USERNAME);

                        txteditMSSV.setText(mssv);
                        txteditName.setText(username);
                        txteditPhone.setText(phone);

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get()
                                        .load(uri)
                                        .into(imageAvt);
                                progressBar.hide();
                            }
                        });
                    } else {
                        Log.d(TAG, "No document found with email: " + Email);
                    }
                    progressBar.hide();
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txteditName.getText().toString();
                String phone = txteditPhone.getText().toString();
                String mssv = txteditMSSV.getText().toString();

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("username", username);
                updatedData.put("phone", phone);
                updatedData.put("mssv", mssv);

                mFirestore.collection(Constant.Database.User.COLLECTION_USER).document(Email)
                        .update(updatedData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfileActivity.this, "Update Fails", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}