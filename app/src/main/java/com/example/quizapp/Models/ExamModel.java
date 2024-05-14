package com.example.quizapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class ExamModel {
    private String id;
    private ArrayList<QuizModel> quizs ;
    private String modulename;
    private String durationInMinutes;//
    private Date startDateTime;
    private Date endDateTime;//
    private String state;
    private int marks; //(diem)


    public ExamModel() {
    }

    public ExamModel(String id, ArrayList<QuizModel> quizs, String modulename, String durationInMinutes, Date startDateTime, Date endDateTime, String state, int marks) {
        this.id = id;
        this.quizs = quizs;
        this.modulename = modulename;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
    }

    public ExamModel(ArrayList<QuizModel> quizs, String modulename, String durationInMinutes, Date startDateTime, Date endDateTime, String state, int marks) {
        this.quizs = quizs;
        this.modulename = modulename;
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

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public String getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(String durationInMinutes) {
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
}
