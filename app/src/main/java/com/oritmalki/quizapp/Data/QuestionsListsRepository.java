package com.oritmalki.quizapp.Data;

import com.oritmalki.quizapp.model.QuestionsList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Orit on 2.2.2018.
 */

public class QuestionsListsRepository {

    private final static QuestionsListsRepository INSTANCE = new QuestionsListsRepository();

    //sorted Map, maintains items order
    private TreeMap<Integer, QuestionsList> questionMap;

    public static QuestionsListsRepository getInstance() {
        return INSTANCE;
    }

    private QuestionsListsRepository() {
        this.questionMap = new TreeMap<>();

    }

    public void saveQuestionList(QuestionsList questions) {
        questionMap.put(questions.getQuestionListId(), questions);
    }

    public QuestionsList getQuestionList(int questionListId) {
        return questionMap.get(questionMap.get(questionListId));
    }

    public List<QuestionsList> getQuestionsLists() {
        return new ArrayList<>(questionMap.values());
    }
}
