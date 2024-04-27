package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.quizapp.ultils.Constant.Database.Module.COLLECTION_MODULE;
import static com.example.quizapp.ultils.Constant.Database.User.COLLECTION_USER;
import static com.example.quizapp.ultils.Constant.Database.User.ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Models.UserModel;
import com.example.quizapp.R;
import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtChangePassword, txtMSSV, txtEmail, txtUsername,
            txtPhoneNumber;
    private Button btnEditProfile;
    private ImageButton imgbtnChangeAvtImage;
    private ImageView imageAvt;
    private ContentLoadingProgressBar progressBar;
    private FirebaseUser user;
    private String userID = "";
    private String Email = "";
    private FirebaseFirestore mFirestore;
    private ArrayList<UserModel> userModels;
    private DocumentReference mRefCollectionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtChangePassword = findViewById(R.id.txtChangePassword);
        txtMSSV = findViewById(R.id.txtMSSV);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        imgbtnChangeAvtImage = findViewById(R.id.imgbtnChangeAvtImage);
        imageAvt = findViewById(R.id.imageAvt);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        mFirestore = FirebaseFirestore.getInstance();

        Email = user.getEmail();
        txtEmail.setText(Email);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //txtChangePassword dùng để thay đổi mật khẩu

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images").child(userID);
        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,EditPasswordActivity.class);
                startActivity(intent);
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        progressBar.show();

        mFirestore.collection(COLLECTION_USER).document(Email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String mssv = (String) data.get(Constant.Database.User.MSSV);
                        String phone = (String) data.get(Constant.Database.User.PHONE);
                        String username = (String) data.get(Constant.Database.User.USERNAME);

                        txtMSSV.setText(mssv);
                        txtUsername.setText(username);
                        txtPhoneNumber.setText(phone);
                    } else {
                        Log.d(TAG, "No document found with email: " + Email);
                    }
                    progressBar.hide();
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });

        imgbtnChangeAvtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpLoadImageProfileActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(imageAvt);
            }
        });

    }
}