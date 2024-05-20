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



    private final int RESOURCE_ID;
    private ArrayList<ChoiceModel> mChoices;
    private int selectedChoiceIndex= -1;
    private int mOrder;
    private int response;
    private ArrayList<Integer> mPositions;



    public McqRvAdapter(int RESOURCE_ID, ArrayList<ChoiceModel> mChoices, int mOrder, int response) {
        this.RESOURCE_ID = RESOURCE_ID;
        this.mChoices = mChoices;
        this.mOrder = mOrder;
        this.response = response;
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


        // Kiểm tra nếu vị trí hiện tại đã được chọn trước đó
        if (holder.getAdapterPosition() == response) {
            holder.radioAnswer.setChecked(true);
        } else {
            holder.radioAnswer.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response = holder.getAdapterPosition();
                selectedChoiceIndex = response;
                notifyDataSetChanged();


            }
        });

        holder.radioAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response = holder.getAdapterPosition();
                selectedChoiceIndex = response;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAnswer = itemView.findViewById(R.id.textAnswer);
            radioAnswer = itemView.findViewById(R.id.radioAnswer);
        }

    }
}
