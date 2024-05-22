package com.example.quizapp.adapters;

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

import java.util.ArrayList;

public class HistoryMcqRvAdapter extends RecyclerView.Adapter<HistoryMcqRvAdapter.ViewHolder>{
    private final int RESOURCE_ID;
    private ArrayList<ChoiceModel> mChoices;
    private ArrayList<QuizModel> quizModels;
    private ArrayList<QuestionModel> mQuestions;
    private int mOrder;
    private int mSelectedItem = -1;

    public HistoryMcqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices, ArrayList<QuizModel> quizModels) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
        this.quizModels = quizModels;
    }

    public HistoryMcqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices, ArrayList<QuizModel> quizModels, ArrayList<QuestionModel> mQuestions) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
        this.quizModels = quizModels;
        this.mQuestions = mQuestions;
    }

    public HistoryMcqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices, ArrayList<QuizModel> quizModels, ArrayList<QuestionModel> mQuestions, int mOrder) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
        this.quizModels = quizModels;
        this.mQuestions = mQuestions;
        this.mOrder = mOrder;
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
    public int getItemCount() {
        return mChoices.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChoiceModel choice = mChoices.get(holder.getAdapterPosition());

        holder.radioAnswer.setChecked(false);

        // Đặt trạng thái "checked" nếu lựa chọn là câu trả lời hoặc câu trả lời đúng
        if (choice.getId().equals(quizModels.get(mOrder).getIdanswer()) ||
                choice.getId().equals(quizModels.get(mOrder).getIdcorrect())) {
            holder.radioAnswer.setChecked(true);
        }

        // Đặt nội dung của câu trả lời
        holder.textAnswer.setText(choice.getAnswer());

        // Đặt màu nền cho đáp án
        if (choice.getId().equals(quizModels.get(mOrder).getIdanswer())) {
            holder.cardViewAnswer.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.your_answer_color));
        } else if (choice.getId().equals(quizModels.get(mOrder).getIdcorrect())) {
            holder.cardViewAnswer.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.correct_answer_color));
        } else {
            holder.cardViewAnswer.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.default_answer_color));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
        holder.radioAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
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
