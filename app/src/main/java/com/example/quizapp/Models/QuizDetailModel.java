package com.example.quizapp.Models;

import java.util.Date;

public class QuizDetailModel {
    private int numberQuestion;
    private int durationInMinutes;
    private Date startDateTime;
    private Date endDateTime;
    private String state;
    private int marks;
    private int grade;
    private String feedback;

    public QuizDetailModel() {
    }

    public QuizDetailModel(int numberQuestion, int durationInMinutes, Date startDateTime, Date endDateTime, String state, int marks, int grade, String feedback) {
        this.numberQuestion = numberQuestion;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
        this.grade = grade;
        this.feedback = feedback;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
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

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
