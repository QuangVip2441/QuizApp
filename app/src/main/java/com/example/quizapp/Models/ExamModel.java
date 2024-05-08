package com.example.quizapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class ExamModel {
    private int incorrect;
    private ArrayList<QuestionModel> questions;
    private ArrayList<Integer> correct;
    private int durationInMinutes;
    private Date startDateTime;
    private Date endDateTime;
    private String state;
    private float marks; //(diem)


    public ExamModel(int incorrect, ArrayList<QuestionModel> questions, ArrayList<Integer> correct, int durationInMinutes, Date startDateTime, Date endDateTime, String state, float marks) {
        this.incorrect = incorrect;
        this.questions = questions;
        this.correct = correct;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
    }

    public ExamModel() {
    }


    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public ArrayList<QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionModel> questions) {
        this.questions = questions;
    }

    public ArrayList<Integer> getCorrect() {
        return correct;
    }

    public void setCorrect(ArrayList<Integer> correct) {
        this.correct = correct;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getMarks() {
        return marks;
    }

    public void setMarks(float marks) {
        this.marks = marks;
    }
}
