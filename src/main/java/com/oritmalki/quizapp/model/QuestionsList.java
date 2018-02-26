package com.oritmalki.quizapp.model;

import java.util.List;

/**
 * Created by Orit on 26.2.2018.
 */

public class QuestionsList {

    private List<Question> questions;
    private int questionListId;
    private String questionListName;

    public QuestionsList(List<Question> questions, int questionListId) {
        this.questions = questions;
        this.questionListId = questionListId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getQuestionListId() {
        return questionListId;
    }

    public void setQuestionListId(int questionListId) {
        this.questionListId = questionListId;
    }

    public String getQuestionListName() {
        return questionListName;
    }

    public void setQuestionListName(String questionListName) {
        this.questionListName = questionListName;
    }
}
