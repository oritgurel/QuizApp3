package com.oritmalki.quizapp.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Orit on 31.1.2018.
 */

public class Question implements Serializable {

    String question;
    Answer[] answers;
    int type;
    int id;
    int score=0;



    public Question(int id, String question, int type, Answer[] answers) {
        this.question = question;
        this.answers = answers;
        this.id = id;
        this.type = type;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answers=" + Arrays.toString(answers) +
                ", type=" + type +
                ", id=" + id +
                '}';
    }
}
