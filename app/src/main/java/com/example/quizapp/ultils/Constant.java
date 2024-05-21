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
        public static class Quiz {
            public static final String COLLECTION_QUIZ = "quiz";
            public static final String USERID = "userid";
        }

        // Từng bài thi
        public static class Exam{
            public static final String ID = "id";
            public static final String TEST_NAME = "test_name";
            public static final String COLLECTION_EXAM = "exam";
            public static final String STARTDATETIME = "start_timer";
            public static final String ENDDATETIME = "end_timer";
            public static final String MODULEID = "moduleid";
            public static final String QUESTION = "question";
            public static final String DURATION_IN_MINUTES = "durationinminutes";
            public static final String MARKS = "marks";
            public static final String STATE = "state";
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

        public class TestAdministration {
            public static final String COLLECTION_TEST_ADMIN = "testadmin";
            public static final String ID = "id";
            public static final String MODULEID = "moduleid";
            public static final String TIMEALLOWED = "timeAllowed";
            public static final String TEST_NAME = "test_name";
            public static final String NUMBERQUESTION = "numberquestion";
            public static final String TEST_GET_NUMBER_QUESTIONS = "test_get_numberQuestions";
        }

    }
}
