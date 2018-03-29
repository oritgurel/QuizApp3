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
    private TreeMap<String, Quiz> quizTreeMap;

    public static QuizRepository getInstance() {
        return INSTANCE;
    }

    private QuizRepository() {
        this.quizTreeMap = new TreeMap<>();

    }

    public void saveQuiz(Quiz questions) {
        quizTreeMap.put(questions.getQuizName(), questions);
    }

    public Quiz getQuiz(String questionListName) {
        return quizTreeMap.get(quizTreeMap.get(questionListName));
    }

    public List<Quiz> getQuizList() {
        return new ArrayList<>(quizTreeMap.values());
    }
}

