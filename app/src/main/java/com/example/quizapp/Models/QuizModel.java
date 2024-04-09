package com.example.quizapp.Models;

import java.util.ArrayList;

public class QuizModel {
    private String id;
    private ArrayList<QuestionModel> questions;
    private ArrayList<String> answers;

    private QuizDetailModel quizDetail;

    public QuizModel() {
    }

    public QuizModel(String id, ArrayList<QuestionModel> questions, ArrayList<String> answers, QuizDetailModel quizDetail) {
        this.id = id;
        this.questions = questions;
        this.answers = answers;
        this.quizDetail = quizDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionModel> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public QuizDetailModel getQuizDetail() {
        return quizDetail;
    }

    public void setQuizDetail(QuizDetailModel quizDetail) {
        this.quizDetail = quizDetail;
    }
}
