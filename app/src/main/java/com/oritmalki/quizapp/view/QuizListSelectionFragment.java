package com.oritmalki.quizapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oritmalki.quizapp.Data.GenerateData;
import com.oritmalki.quizapp.Data.QuizRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Question;
import com.oritmalki.quizapp.model.Quiz;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orit on 28.2.2018.
 */

public class QuizListSelectionFragment extends ListFragment {

    SharedPreferences preferences;


    GenerateData generateData = new GenerateData();
    QuizRepository quizRepository = QuizRepository.getInstance();
    public static QuizListAdapter adapter;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    Type type = new TypeToken<List<Quiz>>() {}.getType();



    List<Quiz> quizList;
//  old:   = new ArrayList<>(quizRepository.getQuizList());

    QuizListAdapterCallback mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getContext().getSharedPreferences(CreateQuizFragment.PREFS_NAME, 0);
        editor = preferences.edit();
        editor.putString(CreateQuizFragment.QUIZ_LIST, gson.toJson(quizList));
        editor.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (QuizListAdapterCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement QuizListAdapterCallback");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getContext(), MainActivity.class);
        int pos = WelcomeActivity.selectedQuizListPosition;
        intent.putExtra("Questions_List_Pos", pos);
        intent.putExtra("Question_List", quizList.indexOf(position));
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get list of quizzes from shared prefs
        String quizListPrefs = preferences.getString(CreateQuizFragment.QUIZ_LIST, "");

        if (!quizListPrefs.equals("null")) {
            String jsonQuizList = gson.toJson(preferences.getString(CreateQuizFragment.QUIZ_LIST, ""));
                quizList = gson.fromJson(jsonQuizList, type);
        } else {
            quizList = new ArrayList<>();
        }

        //add demo questionList to QuizRepository (short term)
        List<Question> demoQuestionList = generateData.getQuestionsData();
        Quiz quiz = new Quiz("Music Quiz", demoQuestionList);
        quizList.add(quiz);
        quizRepository.saveQuiz(quiz);

        //save changes in quizList back to shared prefs (long term)
        String jsonPrefs = gson.toJson(quizRepository.getQuizList());
        editor.putString(CreateQuizFragment.QUIZ_LIST, jsonPrefs);
        editor.commit();


        //get quizList for adapter from shared prefs
        this.quizList = gson.fromJson(jsonPrefs, type);

        adapter = new QuizListAdapter(

                getActivity(), R.id.list, this.quizList, mCallback);
        this.setListAdapter(adapter);
        adapter.notifyDataSetChanged();

//        setRetainInstance(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
