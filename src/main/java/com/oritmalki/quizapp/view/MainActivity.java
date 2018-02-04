package com.oritmalki.quizapp.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.oritmalki.quizapp.Data.GenerateData;
import com.oritmalki.quizapp.Data.QuestionsRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;

    private Button prevButton;
    private Button nextButton;
    private GenerateData generateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        generateData = new GenerateData();

        List<Question> questionList = generateData.getQuestionsData();

        for (Question question : questionList) {
            QuestionsRepository.getInstance().saveQuestion(question);
        }
//        prevButton = findViewById(R.id.prv_butt);


//        nextButton = findViewById(R.id.next_butt);


        initViewPager();
    }

    public void initViewPager() {

                List<Question> questions = QuestionsRepository.getInstance().getQuestions();
                List<QuestionFragment> fragments = new ArrayList<>();
                for (Question question : questions) {
                    fragments.add(QuestionFragment.newInstance(question.getId()));

                }

                QuestionsPagerAdapter adapter = new QuestionsPagerAdapter(getSupportFragmentManager(), fragments);
                viewPager.setAdapter(adapter);
            }

    @Override
    public void onClick(View v) {

    }
}
