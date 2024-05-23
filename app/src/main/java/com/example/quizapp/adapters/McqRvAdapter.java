package com.example.quizapp.adapters;

import static java.security.AccessController.getContext;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;
import com.example.quizapp.ultils.DatabaseHelper;
import com.example.quizapp.ultils.QuizDatabaseHelper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class McqRvAdapter extends RecyclerView.Adapter<McqRvAdapter.ViewHolder> {

    private final int RESOURCE_ID;
    private ArrayList<ChoiceModel> mChoices;
    private int selectedChoiceIndex= -1;
    private ArrayList<QuestionModel> mQuestions;
    private int mOrder;
    private int response;
    private DatabaseHelper dbHelper;
    private QuizDatabaseHelper quizDatabaseHelper;


    public McqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices, int mOrder, DatabaseHelper dbHelper, ArrayList<QuestionModel> mQuestions, QuizDatabaseHelper quizDatabaseHelper) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
        this.mOrder = mOrder;
        this.dbHelper = dbHelper;
        this.mQuestions = mQuestions;
        this.quizDatabaseHelper = quizDatabaseHelper;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(RESOURCE_ID, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChoiceModel choice = mChoices.get(holder.getAdapterPosition());
        holder.textAnswer.setText(choice.getAnswer());

        int savedResponse = -1; // Default value indicating no response

        if (dbHelper != null) {
            Integer responseFromDB = dbHelper.getResponse(mOrder);
            if (responseFromDB != null) {
                savedResponse = responseFromDB;
            }
        }

        // Kiểm tra nếu vị trí hiện tại đã được chọn trước đó
        if (holder.getAdapterPosition() == savedResponse) {
            holder.radioAnswer.setChecked(true);
            holder.cardViewAnswer.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.your_answer_color));
        } else {
            holder.radioAnswer.setChecked(false);
            holder.cardViewAnswer.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response = holder.getAdapterPosition();
                selectedChoiceIndex = response;
                dbHelper.saveResponse(mOrder, response);

                QuestionModel currentQuestion = mQuestions.get(mOrder);
                ChoiceModel selectedChoice = currentQuestion.getChoices().get(response);
                String selectId = selectedChoice.getId();
                String current = currentQuestion.getCorrect();

                boolean isCorrect = selectId.equals(current);
                QuizModel quiz = new QuizModel(currentQuestion.getId(), currentQuestion.getContent(), selectId, current, isCorrect);

                // Lưu quiz vào SQLite database
                quizDatabaseHelper.addQuiz(quiz);

                notifyDataSetChanged();
            }
        });

        holder.radioAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response = holder.getAdapterPosition();
                selectedChoiceIndex = response;
                dbHelper.saveResponse(mOrder, response);
                QuestionModel currentQuestion = mQuestions.get(mOrder);
                ChoiceModel selectedChoice = currentQuestion.getChoices().get(response);
                String selectId = selectedChoice.getId();
                String current = currentQuestion.getCorrect();

                boolean isCorrect = selectId.equals(current);
                QuizModel quiz = new QuizModel(currentQuestion.getId(), currentQuestion.getContent(), selectId, current, isCorrect);

                // Lưu quiz vào SQLite database
                quizDatabaseHelper.addQuiz(quiz);

                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mChoices.size();
    }

    public int getSelectedChoiceIndex() {
        return selectedChoiceIndex;
    }

    public void setSelectedChoiceIndex(int selectedChoiceIndex) {
        this.selectedChoiceIndex = selectedChoiceIndex;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioAnswer;
        private TextView textAnswer;
        private CardView cardViewAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAnswer = itemView.findViewById(R.id.textAnswer);
            radioAnswer = itemView.findViewById(R.id.radioAnswer);
            cardViewAnswer = itemView.findViewById(R.id.cardViewAnswer);
        }

    }
}
