package com.example.quizapp.Models;

import java.util.ArrayList;

public class QuestionModel {
    private String id;
    private String content;
    private ArrayList<ChoiceModel> choices;
    private String correct;

    public QuestionModel() {
    }

    public QuestionModel(String id, String content, ArrayList<ChoiceModel> choices, String correct) {
        this.id = id;
        this.content = content;
        this.choices = choices;
        this.correct = correct;
    }

    public QuestionModel(String content, ArrayList<ChoiceModel> choices, String correct) {
        this.content = content;
        this.choices = choices;
        this.correct = correct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<ChoiceModel> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<ChoiceModel> choices) {
        this.choices = choices;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }
}
