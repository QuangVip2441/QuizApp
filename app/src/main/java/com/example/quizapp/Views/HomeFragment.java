package com.example.quizapp.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quizapp.R;
import com.google.android.material.imageview.ShapeableImageView;

public class HomeFragment extends Fragment {

    private TextView hiddenContent, txtNameApp;
    private ImageButton btnToggle;
    private ShapeableImageView imgTestModule;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnToggle = view.findViewById(R.id.btnToggle);
        hiddenContent = view.findViewById(R.id.hiddenContent);
        txtNameApp = view.findViewById(R.id.txtNameApp);
        imgTestModule = view.findViewById(R.id.imgTestModule);

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent();
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