package com.example.quizapp.Models;

import java.util.ArrayList;
import java.util.Date;

public class ExamModel {
    private String id;
    private String moduleId;
    private ArrayList<QuizModel> quizs ;
    private String testname;
    private String durationInMinutes;//
    private Date startDateTime;
    private Date endDateTime;//
    private String state;
    private Float marks; //(diem)
    private ArrayList<QuestionModel> mQuestion;


    public ExamModel() {
    }


    public ExamModel(String id, ArrayList<QuizModel> quizs, String testname, String durationInMinutes, Date startDateTime, Date endDateTime, String state, Float marks) {
        this.id = id;
        this.quizs = quizs;
        this.testname = testname;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
    }



    public ExamModel(String id, String moduleId, ArrayList<QuizModel> quizs, String testname, String durationInMinutes, Date startDateTime, Date endDateTime, String state, Float marks, ArrayList<QuestionModel> mQuestion) {
        this.id = id;
        this.moduleId = moduleId;
        this.quizs = quizs;
        this.testname = testname;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
        this.mQuestion = mQuestion;
    }

    public ExamModel(String moduleId, ArrayList<QuizModel> quizs, String testname, String durationInMinutes, Date startDateTime, Date endDateTime, String state, Float marks, ArrayList<QuestionModel> mQuestion) {
        this.moduleId = moduleId;
        this.quizs = quizs;
        this.testname = testname;
        this.durationInMinutes = durationInMinutes;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.state = state;
        this.marks = marks;
        this.mQuestion = mQuestion;
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

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
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

    public Float getMarks() {
        return marks;
    }

    public void setMarks(Float marks) {
        this.marks = marks;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public ArrayList<QuestionModel> getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(ArrayList<QuestionModel> mQuestion) {
        this.mQuestion = mQuestion;
    }
}
