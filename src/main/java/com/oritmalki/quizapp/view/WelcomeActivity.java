package com.oritmalki.quizapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.oritmalki.quizapp.R;

/**
 * Created by Orit on 4.2.2018.
 */

public class WelcomeActivity extends AppCompatActivity {

    FrameLayout container;
    Button startQuiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        if (savedInstanceState == null) {
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.welcome_container, welcomeFragment).show(welcomeFragment).commit();
        }

        startQuiz = findViewById(R.id.start_quiz_butt);
        startQuiz.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //create welcome Fragment

    }
}
