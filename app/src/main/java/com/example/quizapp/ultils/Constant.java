package com.example.quizapp.ultils;

public class Constant {
    public static class Database {
        public static class Module {
            public static final String COLLECTION_MODULE = "module";
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String INTRODUCTION = "introduction";
            public static final String NUMBER_QUESTIONS = "numberQuestions";

        }

        // Từng câu
        public static class Exam {
            public static final String COLLECTION_EXAM = "exam";
            public static final String EXAMID = "exid";
            public static final String EXAMQUESTION = "exquestion";
            public static final String EXAMANSWER = "exanswer"; // user choosed answer
            public static final String EXAMCORRECT = "excorrect";
        }

        // Từng bài thi
        public static class Candidate{
            public static final String COLLECTION_CANDIDATE = "candidate";
            public static final String ID = "id";
            public static final String START_TIMER = "start_timer";
            public static final String END_TIMER = "end_timer";
            public static final String NUMBERQUESTION = "numberquestion";
            public static final String DURATION_IN_MINUTES = "durationinminutes";
            public static final String MARKS = "marks";
            public static final String INCORRECT = "incorrect";
        }

        public static class Question {
            public static final String COLLECTION_QUESTIONS = "questions";
            public static final String ID = "id";
            public static final String CONTENT = "content";
            public static final String CHOICES = "choices";
            public static final String CORRECT = "correct";

        }

        public static class Choice {
            public static final String ID = "id";
            public static final String CONTENT = "answer";

        }

        public class User {
            public static final String COLLECTION_USER = "user";
            public static final String ID = "id"; //--
            public static final String MSSV = "mssv"; //--
            public static final String EMAIL = "email";//--
            public static final String TOKEN = "token";
            public static final String USERNAME = "username";//--
            public static final String PHONE = "phone";//--
            public static final String PHOTO = "photo";//--
            public static final String STATUS = "status";
            public static final String TIME_LOGIN = "time_login"; // ngày truy cập
        }

    }
}
