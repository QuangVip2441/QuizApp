package com.example.quizapp.Views;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    DatabaseReference databaseReference;
    private static final String USERS = "user";
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

        databaseReference = FirebaseDatabase.getInstance().getReference("user");


        Toolbar toolbar = findViewById(R.id.toolbaredit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images").child(userID);


        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference useref = rootref.child(USERS);
        Log.v("USERID", useref.getKey());

        progressBar.show();
        useref.addValueEventListener(new ValueEventListener() {
            String name, phone, mssv;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId : snapshot.getChildren()){
                    if (keyId.child("id").getValue().equals(userID)){
                        name = keyId.child("username").getValue(String.class);
                        phone = keyId.child("phone").getValue(String.class);
                        mssv = keyId.child("mssv").getValue(String.class);
                        break;
                    }
                }
                txteditPhone.setText(phone);
                txteditName.setText(name);
                txteditMSSV.setText(mssv);

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .into(imageAvt);
                        progressBar.hide();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String nodeName = "user";
        String userId = userID;

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("username", txteditName.getText().toString());
                updatedData.put("phone",txteditPhone.getText().toString());
                updatedData.put("mssv",txteditMSSV.getText().toString());


                rootref.child(nodeName).child(userId).updateChildren(updatedData)
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