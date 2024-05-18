package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.ultils.DatabaseHelper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class McqRvAdapter extends RecyclerView.Adapter<McqRvAdapter.ViewHolder> {

    private List<String> savedPositions;
    private ArrayList<String> savedAnswers;
    private final int RESOURCE_ID;
    private ArrayList<ChoiceModel> mChoices;
    private int mSelectedItem = -1;
    private DatabaseHelper dbHelper;



    public void setSavedAnswers(ArrayList<String> savedAnswers) {
        this.savedAnswers = savedAnswers;
        notifyDataSetChanged();
    }

    public void setSelectedItem(int position) {
        // If the clicked item is already selected, deselect it
        if (mSelectedItem == position) {
            mSelectedItem = -1; // Deselect the item
        } else {
            mSelectedItem = position; // Otherwise, select the clicked item
        }
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }


    public McqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices, DatabaseHelper dbHelper) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
        this.dbHelper = dbHelper;
        this.savedAnswers = new ArrayList<>();
    }

    public McqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
        this.savedAnswers = new ArrayList<>();
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

        if (savedAnswers.contains(choice.getId()))
            holder.radioAnswer.setChecked(savedAnswers.contains(choice.getId()));
        else
            holder.radioAnswer.setChecked(holder.getAdapterPosition() == mSelectedItem);

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

    @Override
    public int getItemCount() {
        return mChoices.size();
    }
    public int getSelectedItem() {
        return mSelectedItem;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioAnswer;
        private TextView textAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAnswer = itemView.findViewById(R.id.textAnswer);
            radioAnswer = itemView.findViewById(R.id.radioAnswer);
        }

    }
}
