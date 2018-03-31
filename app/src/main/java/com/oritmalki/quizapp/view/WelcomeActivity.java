package com.oritmalki.quizapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Quiz;

import java.io.Serializable;

/**
 * Created by Orit on 4.2.2018.
 */

public class WelcomeActivity extends AppCompatActivity implements WelcomFragmentCallback, QuizListAdapterCallback {

    FrameLayout welcome_container;
    RelativeLayout list_content;
    Button startQuiz;
    QuizListAdapterCallback quizListAdapterCallback;
    public static Quiz selectedQuiz;
    TextView startQuizTv;
    public static final String QUIZ_KEY = "Quiz_Key";
    public static final String TO_CREATE_QUIZ_FRAGMENT = "to_create_quiz_fragment";
    public static final String IS_IN_REVIEW = "isInReview_key";

    //TODO navigation back from start quiz screen


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        list_content = findViewById(R.id.list_content);
        welcome_container = findViewById(R.id.welcome_container);

        startQuizTv = findViewById(R.id.start_quiz_tv);

        if (savedInstanceState == null) {
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.welcome_container, welcomeFragment);
            transaction.show(welcomeFragment).commit();

        }




        startQuiz = findViewById(R.id.start_quiz_butt);
        startQuiz.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                QuestionFragment.isInReview = false;
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra(QUIZ_KEY, selectedQuiz);
                intent.putExtra(IS_IN_REVIEW, false);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (!(list_content.getVisibility() == View.VISIBLE) && !(welcome_container.getVisibility() == View.VISIBLE)) {
            list_content.setVisibility(View.VISIBLE);
            welcome_container.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.list_content, new QuizListSelectionFragment()).commit();
        } else if (list_content.getVisibility() == View.VISIBLE && (!(welcome_container.getVisibility() == View.VISIBLE)))
                 {

            list_content.setVisibility(View.GONE);
            welcome_container.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.welcome_container, new WelcomeFragment()).commit();

        } else if (list_content.getVisibility() == View.VISIBLE && welcome_container.getVisibility() == View.GONE) {
           getSupportFragmentManager().beginTransaction().replace(R.id.welcome_container, new WelcomeFragment()).commit();


        } else {

            super.onBackPressed();
            list_content.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_quiz:
                CreateQuizFragment.quizNameIsSet = false;
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(TO_CREATE_QUIZ_FRAGMENT, true);
                startActivity(intent);
                break;
            case (R.id.select_quiz):
                //attach listFragment
                FragmentManager fm = getSupportFragmentManager();

                QuizListSelectionFragment selectionFragment = (QuizListSelectionFragment) fm.findFragmentByTag("QuizListSelectionFragment");
//                if (selectionFragment == null) {
                    selectionFragment = new QuizListSelectionFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    welcome_container.setVisibility(View.GONE);
                    list_content.setVisibility(View.VISIBLE);
                    transaction.replace(R.id.list_content, selectionFragment, "QuizListSelectionFragment").show(selectionFragment).commit();

                break;
        }
    }


    @Override
    public void onAdapterClick(Quiz quiz) {
        this.selectedQuiz = quiz;
        startQuizTv.setText(quiz.getQuizName());
        welcome_container.setVisibility(View.GONE);
        list_content.setVisibility(View.GONE);
    }
}
