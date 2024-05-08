package com.example.quizapp.Models;

public class TestAdministration {
    private String id;
    private String moduleId;
    private String test_name;
    private long test_numberQuestions;


    public TestAdministration(String id, String moduleId, String test_name, long test_numberQuestions) {
        this.id = id;
        this.moduleId = moduleId;
        this.test_name = test_name;
        this.test_numberQuestions = test_numberQuestions;
    }

    public TestAdministration(String moduleId, String test_name, long test_numberQuestions) {
        this.moduleId = moduleId;
        this.test_name = test_name;
        this.test_numberQuestions = test_numberQuestions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
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
}
