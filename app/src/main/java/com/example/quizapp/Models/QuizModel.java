package com.example.quizapp.Models;

public class QuizModel {
    private String id;
    private String questioncontent;
    private String idanswer;
    private String idcorrect;
    private boolean state; // True/false

    public QuizModel(String id, String questioncontent, String idanswer, String idcorrect, boolean state) {
        this.id = id;
        this.questioncontent = questioncontent;
        this.idanswer = idanswer;
        this.idcorrect = idcorrect;
        this.state = state;
    }

    public QuizModel(String questioncontent, String idanswer, String idcorrect, boolean state) {
        this.questioncontent = questioncontent;
        this.idanswer = idanswer;
        this.idcorrect = idcorrect;
        this.state = state;
    }

    public QuizModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestioncontent() {
        return questioncontent;
    }

    public void setQuestioncontent(String questioncontent) {
        this.questioncontent = questioncontent;
    }

    public String getIdanswer() {
        return idanswer;
    }

    public void setIdanswer(String idanswer) {
        this.idanswer = idanswer;
    }

    public String getIdcorrect() {
        return idcorrect;
    }

    public void setIdcorrect(String idcorrect) {
        this.idcorrect = idcorrect;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
