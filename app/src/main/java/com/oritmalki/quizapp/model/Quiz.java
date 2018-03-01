package com.oritmalki.quizapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Orit on 26.2.2018.
 */

public class Quiz implements Serializable {

    private List<Question> questions;
    private String quizName;

    public Quiz(String quizName, List<Question> questions) {
        this.questions = questions;
        this.quizName = quizName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String questionListName) {
        this.quizName = questionListName;
    }
}
