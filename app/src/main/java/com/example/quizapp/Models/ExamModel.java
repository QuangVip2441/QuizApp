package com.example.quizapp.Models;

public class ExamModel {
    private String ExId;
    private String ExQuestion;
    private String ExAnswer;
    private String ExCorrect;

    public ExamModel() {
    }

    public ExamModel(String exId, String exQuestion, String exAnswer, String exCorrect) {
        ExId = exId;
        ExQuestion = exQuestion;
        ExAnswer = exAnswer;
        ExCorrect = exCorrect;
    }

    public ExamModel(String exQuestion, String exAnswer, String exCorrect) {
        ExQuestion = exQuestion;
        ExAnswer = exAnswer;
        ExCorrect = exCorrect;
    }

    public String getExId() {
        return ExId;
    }

    public void setExId(String exId) {
        ExId = exId;
    }

    public String getExQuestion() {
        return ExQuestion;
    }

    public void setExQuestion(String exQuestion) {
        ExQuestion = exQuestion;
    }

    public String getExAnswer() {
        return ExAnswer;
    }

    public void setExAnswer(String exAnswer) {
        ExAnswer = exAnswer;
    }

    public String getExCorrect() {
        return ExCorrect;
    }

    public void setExCorrect(String exCorrect) {
        ExCorrect = exCorrect;
    }
}
