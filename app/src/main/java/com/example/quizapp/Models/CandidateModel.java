package com.example.quizapp.Models;

import java.util.ArrayList;

public class CandidateModel {
    private String id;
    private ArrayList<QuizDetailModel> quizzdetail;
    private String IncorrectAnswer;

    public CandidateModel() {
    }

    public CandidateModel(String id, ArrayList<QuizDetailModel> quizzdetail, String incorrectAnswer) {
        this.id = id;
        this.quizzdetail = quizzdetail;
        IncorrectAnswer = incorrectAnswer;
    }

    public CandidateModel(ArrayList<QuizDetailModel> quizzdetail, String incorrectAnswer) {
        this.quizzdetail = quizzdetail;
        IncorrectAnswer = incorrectAnswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<QuizDetailModel> getQuizzdetail() {
        return quizzdetail;
    }

    public void setQuizzdetail(ArrayList<QuizDetailModel> quizzdetail) {
        this.quizzdetail = quizzdetail;
    }

    public String getIncorrectAnswer() {
        return IncorrectAnswer;
    }

    public void setIncorrectAnswer(String incorrectAnswer) {
        IncorrectAnswer = incorrectAnswer;
    }
}
