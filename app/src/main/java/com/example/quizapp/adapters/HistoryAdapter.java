package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.ExamModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.Models.QuizModel;
import com.example.quizapp.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private OnItemClickListener mListenerHistory;
    private final int RESOURCE_ID;
    private int Position;
    private ArrayList<ExamModel> examModels;
    private ArrayList<QuestionModel> questionModels;
    private ArrayList<QuizModel> quizModels;

    public HistoryAdapter(int RESOURCE_ID, ArrayList<ExamModel> examModels, ArrayList<QuestionModel> questionModels, ArrayList<QuizModel> quizModels) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.examModels = examModels;
        this.questionModels = questionModels;
        this.quizModels = quizModels;
    }

    public void setOnItemClickListener(OnItemClickListener mListenerHistory) {
        this.mListenerHistory = mListenerHistory;
    }
    public HistoryAdapter(int RESOURCE_ID, ArrayList<ExamModel> examModels) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.examModels = examModels;
    }
    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(RESOURCE_ID, parent, false);
        HistoryAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamModel examModel = examModels.get(holder.getAdapterPosition());
        QuestionModel questionModel = questionModels.get(holder.getAdapterPosition());
        QuizModel quizModel = quizModels.get(holder.getAdapterPosition());
        holder.txtName.setText(examModel.getTestname());
        holder.txtmarks.setText(String.valueOf(examModel.getMarks()));
        holder.txtdurationinminutes.setText(String.valueOf(examModel.getDurationInMinutes()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenerHistory != null) {
                    int position = holder.getAdapterPosition();
                    setPosition(position);
                    mListenerHistory.onItemClick(examModel.getId(),questionModel.getId(), quizModel.getId(),getPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {return examModels.size();}


    public interface OnItemClickListener {
        void onItemClick(String examId,String questionId, String quizId,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtdurationinminutes, txtmarks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtdurationinminutes = itemView.findViewById(R.id.txtdurationinminutes);
            txtmarks = itemView.findViewById(R.id.txtmarks);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListenerHistory.onItemClick(examModels.get(position).getId(), questionModels.get(position).getId(),quizModels.get(position).getId(),position);
                    }
                }
            });
        }
    }
}
