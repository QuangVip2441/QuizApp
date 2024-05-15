package com.example.quizapp.adapters;

import static android.app.PendingIntent.getActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.Views.QuestionFragment;
import com.example.quizapp.ultils.FragmentUtils;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final int RESOURCE_ID;
    private int Position;
    private ArrayList<QuestionModel> questionModels;


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public QuestionAdapter(int RESOURCE_ID, ArrayList<QuestionModel> questionModels) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.questionModels = questionModels;
    }

    public QuestionAdapter(OnItemClickListener mListener, int RESOURCE_ID, ArrayList<QuestionModel> questionModels) {
        this.mListener = mListener;
        this.RESOURCE_ID = RESOURCE_ID;
        this.questionModels = questionModels;
    }


    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(RESOURCE_ID, parent, false);
        QuestionAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        QuestionModel questionModel = questionModels.get(holder.getAdapterPosition());
        holder.txtnumber.setText(String.valueOf(position + 1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    setPosition(position);
                    mListener.onItemClick(questionModel.getId(), getPosition());
                }
            }
        });

    }
    public interface OnItemClickListener {
        void onItemClick(String questionId, int position);
    }

    @Override
    public int getItemCount() {
        return questionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtnumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtnumber = itemView.findViewById(R.id.txtnumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(questionModels.get(position).getId(), position);
                    }
                }
            });
        }
    }
}
