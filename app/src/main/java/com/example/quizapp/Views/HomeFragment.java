package com.example.quizapp.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quizapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private TextView hiddenContent, txtNameApp;
    private ImageButton btnToggle;
    private ShapeableImageView imgTestModule, imageAvatar, imgTestRandom, imgHistoryTest, imgIntroduction;
    private FirebaseUser user;
    private String userID = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnToggle = view.findViewById(R.id.btnToggle);
        hiddenContent = view.findViewById(R.id.hiddenContent);
        txtNameApp = view.findViewById(R.id.txtNameApp);
        imgTestModule = view.findViewById(R.id.imgTestModule);
        imageAvatar = view.findViewById(R.id.imageAvatar);
        imgTestRandom = view.findViewById(R.id.imgTestRandom);
        imgHistoryTest = view.findViewById(R.id.imgHistoryTest);
        imgIntroduction = view.findViewById(R.id.imgIntroduction);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images").child(userID);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(imageAvatar);
            }
        });

        imgIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IntroductionActivity.class);
                startActivity(intent);
            }
        });

        imgHistoryTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent();
            }
        });

        imgTestRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartSumTestActivity.class);
                startActivity(intent);
            }
        });

        imgTestModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModuleActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }
    private void toggleContent() {
        if (hiddenContent.getVisibility() == View.VISIBLE) {
            hiddenContent.setVisibility(View.GONE);
            btnToggle.setImageResource(R.drawable.ic_show);
        } else {
            hiddenContent.setVisibility(View.VISIBLE);
            btnToggle.setImageResource(R.drawable.ic_hide);
        }
    }
}