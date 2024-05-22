package com.example.quizapp.Models;

public class PositionModel {
    private String questionId;
    private int position;

    public PositionModel() {
    }

    public PositionModel(String questionId, int position) {
        this.questionId = questionId;
        this.position = position;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
