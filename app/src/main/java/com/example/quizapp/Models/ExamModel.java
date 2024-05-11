package com.example.quizapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class ExamModel {
    private String id;
    private ArrayList<QuizModel> quizs ;
    private Date durationInMinutes;//
    private Date startDateTime;
    private Date endDateTime;
    private String state;
    private float marks; //(diem)

    public ExamModel(String id, ArrayList<QuizModel> quizs, Date durationInMinutes, Date startDateTime, Date endDateTime, String state, float marks) {
        this.id = id;
        this.quizs = quizs;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
    }

    public ExamModel() {
    }

    public ExamModel(ArrayList<QuizModel> quizs, Date durationInMinutes, Date startDateTime, Date endDateTime, String state, float marks) {
        this.quizs = quizs;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<QuizModel> getQuizs() {
        return quizs;
    }

    public void setQuizs(ArrayList<QuizModel> quizs) {
        this.quizs = quizs;
    }

    public Date getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Date durationInMinutes) {
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
