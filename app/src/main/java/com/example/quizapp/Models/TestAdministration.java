package com.example.quizapp.Models;

public class TestAdministration {
    private String id;
    private String test_name;
    private long test_numberQuestions;
    private int timeAllowed;
    private int NumberQuestionModule1;
    private int NumberQuestionModule2;
    private int NumberQuestionModule3;
    private int NumberQuestionModule4;
    private int NumberQuestionModule5;
    private int NumberQuestionModule6;


    public TestAdministration() {
    }

    public TestAdministration(String id, String test_name, long test_numberQuestions, int timeAllowed, int numberQuestionModule1, int numberQuestionModule2, int numberQuestionModule3, int numberQuestionModule4, int numberQuestionModule5, int numberQuestionModule6) {
        this.id = id;
        this.test_name = test_name;
        this.test_numberQuestions = test_numberQuestions;
        this.timeAllowed = timeAllowed;
        NumberQuestionModule1 = numberQuestionModule1;
        NumberQuestionModule2 = numberQuestionModule2;
        NumberQuestionModule3 = numberQuestionModule3;
        NumberQuestionModule4 = numberQuestionModule4;
        NumberQuestionModule5 = numberQuestionModule5;
        NumberQuestionModule6 = numberQuestionModule6;
    }

    public TestAdministration(String test_name, long test_numberQuestions, int timeAllowed, int numberQuestionModule1, int numberQuestionModule2, int numberQuestionModule3, int numberQuestionModule4, int numberQuestionModule5, int numberQuestionModule6) {
        this.test_name = test_name;
        this.test_numberQuestions = test_numberQuestions;
        this.timeAllowed = timeAllowed;
        NumberQuestionModule1 = numberQuestionModule1;
        NumberQuestionModule2 = numberQuestionModule2;
        NumberQuestionModule3 = numberQuestionModule3;
        NumberQuestionModule4 = numberQuestionModule4;
        NumberQuestionModule5 = numberQuestionModule5;
        NumberQuestionModule6 = numberQuestionModule6;
    }

    public TestAdministration(int timeAllowed) {
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

    public long getTest_numberQuestions() {
        return test_numberQuestions;
    }

    public void setTest_numberQuestions(long test_numberQuestions) {
        this.test_numberQuestions = test_numberQuestions;
    }

    public int getTimeAllowed() {
        return timeAllowed;
    }

    public void setTimeAllowed(int timeAllowed) {
        this.timeAllowed = timeAllowed;
    }

    public int getNumberQuestionModule1() {
        return NumberQuestionModule1;
    }

    public void setNumberQuestionModule1(int numberQuestionModule1) {
        NumberQuestionModule1 = numberQuestionModule1;
    }

    public int getNumberQuestionModule2() {
        return NumberQuestionModule2;
    }

    public void setNumberQuestionModule2(int numberQuestionModule2) {
        NumberQuestionModule2 = numberQuestionModule2;
    }

    public int getNumberQuestionModule3() {
        return NumberQuestionModule3;
    }

    public void setNumberQuestionModule3(int numberQuestionModule3) {
        NumberQuestionModule3 = numberQuestionModule3;
    }

    public int getNumberQuestionModule4() {
        return NumberQuestionModule4;
    }

    public void setNumberQuestionModule4(int numberQuestionModule4) {
        NumberQuestionModule4 = numberQuestionModule4;
    }

    public int getNumberQuestionModule5() {
        return NumberQuestionModule5;
    }

    public void setNumberQuestionModule5(int numberQuestionModule5) {
        NumberQuestionModule5 = numberQuestionModule5;
    }

    public int getNumberQuestionModule6() {
        return NumberQuestionModule6;
    }

    public void setNumberQuestionModule6(int numberQuestionModule6) {
        NumberQuestionModule6 = numberQuestionModule6;
    }
}
