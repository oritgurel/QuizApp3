package com.oritmalki.quizapp.Data;

import com.oritmalki.quizapp.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Orit on 2.2.2018.
 */

public class QuestionsRepository {

    private final static QuestionsRepository INSTANCE = new QuestionsRepository();

    //sorted Map, maintains items order
    private TreeMap<Integer, Question> questionMap;

    public static QuestionsRepository getInstance() {
        return INSTANCE;
    }

    private QuestionsRepository() {
        this.questionMap = new TreeMap<>();

    }

    public void saveQuestion(Question question) {
        questionMap.put(question.getId(), question);
    }

    public Question getQuestion(int id) {
        return questionMap.get(id);
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questionMap.values());
    }

}
