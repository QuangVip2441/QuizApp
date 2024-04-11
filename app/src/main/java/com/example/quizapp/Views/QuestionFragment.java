package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.McqRvAdapter;
import com.example.quizapp.ultils.Constant;
import com.example.quizapp.ultils.FragmentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestionFragment extends Fragment {

    private TextView textContent, QuizTimer, currentQuestion, totalQuestion;
    private RecyclerView recyclerChoices;
    private MaterialButton buttonPrevious;
    private MaterialButton buttonNext;
    private CountDownTimer countDownTimer;
    private ContentLoadingProgressBar progressBar;
    private ArrayList<QuestionModel> mQuestions;
    private int mOrder;
    private String mSelectedModuleID;

    private McqRvAdapter mcqRVAdapter;
    // Firebase
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionQuestions;
    private int Test_timer = 600;
    private long remainingTimeMillis;
    private int correctAns = 0;
    public QuestionFragment() {
    }

    public QuestionFragment(int mOrder, String mSelectedModuleID) {
        this.mQuestions = new ArrayList<>();
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
    }

    public QuestionFragment(ArrayList<QuestionModel> mQuestions, int mOrder, String mSelectedModuleID) {
        this.mQuestions = mQuestions;
        this.mOrder = mOrder;
        this.mSelectedModuleID = mSelectedModuleID;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        QuizTimer = view.findViewById(R.id.quizTimer);
        currentQuestion = view.findViewById(R.id.CurrentQuestion);
        totalQuestion = view.findViewById(R.id.TotalQuestion);
        textContent = view.findViewById(R.id.textContent);
        recyclerChoices = view.findViewById(R.id.recyclerChoices);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);
        buttonNext = view.findViewById(R.id.buttonNext);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mFirestore = FirebaseFirestore.getInstance();
        mRefCollectionQuestions = mFirestore
                .collection(Constant.Database.Module.COLLECTION_MODULE)
                .document(mSelectedModuleID)
                .collection(Constant.Database.Question.COLLECTION_QUESTIONS);


        Bundle bundle = getArguments();
        if (bundle != null) {
            correctAns = bundle.getInt("correctAns", 0);
        }

        currentQuestion.setText("Question " + (mOrder + 1));
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition = mcqRVAdapter.getSelectedItem();
                if (selectedPosition < 0){
                    Toast.makeText(requireContext(),"Answer is not Checked",Toast.LENGTH_SHORT).show();
                }else {
                    QuestionModel currentQuestion = mQuestions.get(mOrder);
                    ChoiceModel selectedChoice = currentQuestion.getChoices().get(selectedPosition);
                    String selectId = selectedChoice.getId();
                    String current = currentQuestion.getCorrect();
                    if (selectId.equals(current)) {
                        correctAns++;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("correctAns", correctAns);
                    if (mOrder < mQuestions.size() - 1) {
                        QuestionFragment nextFragment = new QuestionFragment(mQuestions, mOrder + 1, mSelectedModuleID);
                        nextFragment.setArguments(bundle);
                        FragmentUtils.replaceFragment(
                                getActivity().getSupportFragmentManager(),
                                nextFragment,
                                true);
                    } else {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        FinishQuiz();
                    }
                }
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOrder >= 1) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    currentQuestion.setText("Question " + (mOrder - 1));
                }

            }
        });
        loadData(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Bắt đầu đếm ngược thời gian khi Fragment được hiển thị lại
        startQuizTimer(Test_timer);
    }
    @Override
    public void onPause() {
        super.onPause();
        // Dừng đếm ngược thời gian khi Fragment bị tạm dừng
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    private void loadData(View view) {
        if (mQuestions.size() > 0) {
            showQuestion(view);
        }
        else {
            progressBar.show();
            mRefCollectionQuestions
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            progressBar.hide();

                            if (task.isSuccessful()) {
                                mQuestions.clear();
                                int count = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    ArrayList<ChoiceModel> choices = new ArrayList<>();

                                    ArrayList<HashMap<String, Object>> temp = (ArrayList<HashMap<String, Object>>) data.get(Constant.Database.Question.CHOICES);

                                    for (HashMap<String, Object> i : temp) {
                                        choices.add(new ChoiceModel(
                                                (String) i.get(Constant.Database.Choice.ID),
                                                (String) i.get(Constant.Database.Choice.CONTENT)
                                        ));
                                    }

                                    QuestionModel question = new QuestionModel(
                                            (String) data.get(Constant.Database.Question.ID),
                                            (String) data.get(Constant.Database.Question.CONTENT),
                                            choices,
                                            (String) data.get(Constant.Database.Question.CORRECT)
                                    );
                                    if (count < 10) {
                                        mQuestions.add(question);
                                        count++;
                                    } else {
                                        break;
                                    }
                                }

                                if (mQuestions != null) {
                                    // Trộn danh sách câu hỏi sau khi lấy từ Firestore
                                    Collections.shuffle(mQuestions);
                                    showQuestion(view);
                                    totalQuestion.setText("/" + mQuestions.size());
                                }
                            }
                        }
                    });

        }
    }

    private void showQuestion(View view) {
        if (mOrder < mQuestions.size()) {
            textContent.setText(mQuestions.get(mOrder).getContent());

            mcqRVAdapter = new McqRvAdapter(
                    R.layout.layout_item_answer,
                    mQuestions.get(mOrder).getChoices()
            );
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerChoices.setLayoutManager(layoutManager);
            recyclerChoices.setAdapter(mcqRVAdapter);
        }
    }


    private void startQuizTimer(int maxTimeInseconds){
        countDownTimer = new CountDownTimer(maxTimeInseconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getMinute = TimeUnit.SECONDS.toMinutes(millisUntilFinished / 1000);
                long getSecond = TimeUnit.SECONDS.toSeconds(millisUntilFinished / 1000) % 60;

                String generatetime = String.format(Locale.getDefault(),"%02d:%02d", getMinute, getSecond);
                remainingTimeMillis = millisUntilFinished;

                QuizTimer.setText(generatetime);
            }

            @Override
            public void onFinish() {
                // finish Quiz when time is finished
                FinishQuiz();
                // gán thời gian kết thúc lên firebase
                Map<String, Object> candidate = new HashMap<>();
                candidate.put(Constant.Database.Candidate.END_TIMER, ServerValue.TIMESTAMP);
            }
        };

        // Start Timer
        countDownTimer.start();
    }

    private void FinishQuiz(){
        // back to fragment
        Bundle bundle = new Bundle();
        bundle.putInt("correctAns", correctAns);

        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentQuestion, resultFragment)
                .addToBackStack(null)
                .commit();
    }
}