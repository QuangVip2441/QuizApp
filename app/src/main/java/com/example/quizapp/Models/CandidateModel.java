package com.example.quizapp.Models;

import java.util.ArrayList;

public class CandidateModel {
    private String id;
    private ArrayList<ExamModel> examModels;
    private String IncorrectAnswer;

    public CandidateModel() {
    }

    public CandidateModel(String id, ArrayList<ExamModel> examModels, String incorrectAnswer) {
        this.id = id;
        this.examModels = examModels;
        IncorrectAnswer = incorrectAnswer;
    }

    public CandidateModel(ArrayList<ExamModel> examModels, String incorrectAnswer) {
        this.examModels = examModels;
        IncorrectAnswer = incorrectAnswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getIncorrectAnswer() {
        return IncorrectAnswer;
    }

    public void setIncorrectAnswer(String incorrectAnswer) {
        IncorrectAnswer = incorrectAnswer;
    }

    public ArrayList<ExamModel> getExamModels() {
        return examModels;
    }

    public void setExamModels(ArrayList<ExamModel> examModels) {
        this.examModels = examModels;
    }
}
