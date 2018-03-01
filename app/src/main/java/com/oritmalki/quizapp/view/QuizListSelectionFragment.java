package com.oritmalki.quizapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oritmalki.quizapp.Data.GenerateData;
import com.oritmalki.quizapp.Data.QuizRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Question;
import com.oritmalki.quizapp.model.Quiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orit on 28.2.2018.
 */

public class QuizListSelectionFragment extends ListFragment {

    GenerateData generateData = new GenerateData();
    QuizRepository quizRepository = QuizRepository.getInstance();


    List<Quiz> quizList = new ArrayList<>(quizRepository.getQuizList());
    QuizListAdapterCallback mCallback;

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
        intent.putExtra("Questions_List", quizList.get(position));
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        generateData = new GenerateData();

        List<Question> demoQuestionList = generateData.getQuestionsData();

        Quiz quiz = new Quiz("Music Quiz", demoQuestionList);

        quizRepository.saveQuiz(quiz);

//        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.quiz_list_selection_fragment, container, false);
        QuizListAdapter adapter = new QuizListAdapter(

                getActivity(), R.id.list, quizRepository.getQuizList(), mCallback);
        this.setListAdapter(adapter);
//        setRetainInstance(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
