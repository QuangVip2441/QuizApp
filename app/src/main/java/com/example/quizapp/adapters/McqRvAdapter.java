package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.R;


import java.util.ArrayList;

public class McqRvAdapter extends RecyclerView.Adapter<McqRvAdapter.ViewHolder> {

    private final int RESOURCE_ID;
    private ArrayList<ChoiceModel> mChoices;
    private int mSelectedItem = -1;

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    private int Position;

    public McqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
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
        holder.radioAnswer.setChecked(holder.getAdapterPosition() == mSelectedItem);
        holder.textAnswer.setText(choice.getAnswer());

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
