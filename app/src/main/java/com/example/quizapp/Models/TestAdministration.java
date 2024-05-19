package com.example.quizapp.Models;

public class TestAdministration {
    private String id;
    private String test_name;
    private int test_numberQuestions;
    private int numberquestion;
    private int timeAllowed;

    public TestAdministration() {
    }


    public TestAdministration(String id, String test_name, int test_numberQuestions, int numberquestion, int timeAllowed) {
        this.id = id;
        this.test_name = test_name;
        this.test_numberQuestions = test_numberQuestions;
        this.numberquestion = numberquestion;
        this.timeAllowed = timeAllowed;
    }

    public TestAdministration(String test_name, int test_numberQuestions, int numberquestion, int timeAllowed) {
        this.test_name = test_name;
        this.test_numberQuestions = test_numberQuestions;
        this.numberquestion = numberquestion;
        this.timeAllowed = timeAllowed;
    }

    public TestAdministration(int test_numberQuestions) {
        this.test_numberQuestions = test_numberQuestions;
    }

    public TestAdministration(String test_name, int timeAllowed) {
        this.test_name = test_name;
        this.timeAllowed = timeAllowed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public int getTest_numberQuestions() {
        return test_numberQuestions;
    }

    public void setTest_numberQuestions(int test_numberQuestions) {
        this.test_numberQuestions = test_numberQuestions;
    }

    public int getNumberquestion() {
        return numberquestion;
    }

    public void setNumberquestion(int numberquestion) {
        this.numberquestion = numberquestion;
    }

    public int getTimeAllowed() {
        return timeAllowed;
    }

    public void setTimeAllowed(int timeAllowed) {
        this.timeAllowed = timeAllowed;
    }
}
