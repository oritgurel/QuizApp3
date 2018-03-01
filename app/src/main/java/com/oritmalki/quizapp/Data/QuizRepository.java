package com.oritmalki.quizapp.Data;

import com.oritmalki.quizapp.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Orit on 2.2.2018.
 */

public class QuizRepository {

    private final static QuizRepository INSTANCE = new QuizRepository();

    //sorted Map, maintains items order
    private TreeMap<String, Quiz> questionMap;

    public static QuizRepository getInstance() {
        return INSTANCE;
    }

    private QuizRepository() {
        this.questionMap = new TreeMap<>();

    }

    public void saveQuiz(Quiz questions) {
        questionMap.put(questions.getQuizName(), questions);
    }

    public Quiz getQuiz(String questionListName) {
        return questionMap.get(questionMap.get(questionListName));
    }

    public List<Quiz> getQuizList() {
        return new ArrayList<>(questionMap.values());
    }
}

