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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    private ArrayList<QuestionModel> questionModels;
    private ItemClickListener clickListener;
    public QuestionAdapter(ArrayList<QuestionModel> questionModels){
        this.questionModels = questionModels;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public QuestionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_header_number_question, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.MyViewHolder holder, int position) {
        holder.txtnumber.setText(questionModels.get(position).getId());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickListener.onItemClick(questionModels.get(position));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtnumber;
        public MyViewHolder (View view){
            super(view);
            txtnumber = view.findViewById(R.id.txtnumber);
        }
    }

    public  interface ItemClickListener{

        public void onItemClick(QuestionModel questionModel);
    }
}
