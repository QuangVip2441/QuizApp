package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HeaderFragment extends Fragment {


    private TextView txtTimer;
    private CountDownTimer countDownTimer;
    private int Test_timer = 600;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        txtTimer = view.findViewById(R.id.txtTimer);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerNumberQuestion); // Khởi tạo RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);



        startQuizTimer(Test_timer);
        return view;
    }

//    private void initRecyclerView(View view){
//        RecyclerView recyclerView = view.findViewById(R.id.txtnumber);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//
//        recyclerView.setLayoutManager(layoutManager);
//
//
//        recyclerView.setAdapter();
//    }

    private void startQuizTimer(int maxTimeInseconds){
        countDownTimer = new CountDownTimer(maxTimeInseconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getMinute = TimeUnit.SECONDS.toMinutes(millisUntilFinished / 1000);
                long getSecond = TimeUnit.SECONDS.toSeconds(millisUntilFinished / 1000) % 60;

                String generatetime = String.format(Locale.getDefault(),"%02d:%02d", getMinute, getSecond);


                txtTimer.setText(generatetime);
            }

            @Override
            public void onFinish() {
                // finish Quiz when time is finished
                FinishQuiz();
            }
        };

        // Start Timer
        countDownTimer.start();
    }

    private void FinishQuiz(){
        // Gỡ bỏ HeaderFragment
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_header);
        FragmentUtils.removeFragment(getActivity().getSupportFragmentManager(), fragment);

        // Gỡ bỏ Fragment Question
        Fragment fragmentquestion = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentQuestion);
        FragmentUtils.removeFragment(getActivity().getSupportFragmentManager(), fragmentquestion);

        // Thêm ResultFragment vào activity
        ResultFragment resultFragment = new ResultFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentQuestion, resultFragment)
                .addToBackStack(null)
                .commit();
    }

}