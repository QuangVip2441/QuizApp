package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quizapp.R;

public class HomeFragment extends Fragment {

    private TextView hiddenContent, txtNameApp;
    private ImageButton btnToggle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnToggle = view.findViewById(R.id.btnToggle);
        hiddenContent = view.findViewById(R.id.hiddenContent);
        txtNameApp = view.findViewById(R.id.txtNameApp);

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent();
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