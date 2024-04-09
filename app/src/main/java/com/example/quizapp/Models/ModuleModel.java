package com.example.quizapp.Models;

public class ModuleModel {
    private String id;
    private String name;
    private String introduction;
    private long numberQuestions;

    public ModuleModel() {
    }

    public ModuleModel(String id, String name, String introduction, long numberQuestions) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.numberQuestions = numberQuestions;
    }

    public ModuleModel(String name, String introduction, long numberQuestions) {
        this.name = name;
        this.introduction = introduction;
        this.numberQuestions = numberQuestions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public long getNumberQuestions() {
        return numberQuestions;
    }

    public void setNumberQuestions(long numberQuestions) {
        this.numberQuestions = numberQuestions;
    }
}
