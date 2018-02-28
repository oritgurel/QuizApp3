package com.oritmalki.quizapp.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.oritmalki.quizapp.Data.GenerateData;
import com.oritmalki.quizapp.Data.QuestionsRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Question;
import com.oritmalki.quizapp.model.QuestionsList;
import com.oritmalki.quizapp.view.QuestionFragment.OnButtonClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnButtonClickListener {

    protected static ViewPager viewPager;

    private Button prevButton;
    private Button nextButton;
    private FrameLayout frameLayout;
    private GenerateData generateData;
    private List<Question> questionList;
    static Bundle savedInstanceState;
    private List<QuestionsList> listOfQuizes = new ArrayList<>();
    public static int questionListId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        generateData = new GenerateData();
        List<Question> demoQuestionList = generateData.getQuestionsData();
        QuestionsList questionsList = new QuestionsList(questionListId++);
        questionsList.setQuestions(demoQuestionList);
        listOfQuizes.add(questionsList);
        savedInstanceState = this.savedInstanceState;

        //create an fragment which prompts the user to create a questions list or select from existing questionLists (if available). This should be in the welcome activity
        //TODO then, pass the title of the list selected (or "quiz") to the welcome activity.
        //TODO if user chooses to create a list of question ->
        //TODO create an activity with a questionFragment which prompts the user to insert the questions and the answers, like a form, triggered by a method "createQuestionList"
        //TODO to this form, add a "select type of question" at the top (add another viewGroup on top in editFragment and make it invisible when the question type is selected

        //Here is where the data is inserted.

        //this is for now, //TODO create a listFragment of quizes with adapter and pull the data from there
        questionList = questionsList.getQuestions();

        for (Question question : questionList) {
            QuestionsRepository.getInstance().saveQuestion(question);
        }

        initViewPager();
        frameLayout = findViewById(R.id.total_score_container);

        if (findViewById(R.id.total_score_container) != null) {


            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

//            FinalScoreFragment finalScoreFragment = new FinalScoreFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.total_score_fragment, finalScoreFragment).commit();

        }
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
    public void onButtonClicked(View view) {

        int currPos = viewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.next_but:
                if (currPos != viewPager.getAdapter().getCount()) {
                    viewPager.setCurrentItem(currPos + 1, true);
                }
                break;
            case R.id.prev_but:
                if (currPos != 0) {
                    viewPager.setCurrentItem(currPos - 1, true);
                }
                break;
            case R.id.review_answers:
                viewPager.setCurrentItem(currPos - 1, true);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < questionList.size(); i++) {
            for (int j = 0; j < QuestionsRepository.getInstance().getQuestion(i).getAnswers().length; j++) {

                if (QuestionsRepository.getInstance().getQuestion(i).getAnswers()[j].isChecked()) {
                    ViewGroup innerQuestionLayout = findViewById(R.id.inner_question_layout);
                    if (innerQuestionLayout.getChildAt(1) != null) {
                        RadioGroup radioGroup = (RadioGroup) innerQuestionLayout.getChildAt(1);

                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                        radioButton.setChecked(true);
                        outState.putBoolean("isButtonChecked", ((RadioButton) radioGroup.getChildAt(j)).isChecked());
                    }
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < questionList.size(); i++) {
            for (int j = 0; j < QuestionsRepository.getInstance().getQuestion(i).getAnswers().length; j++) {
                ViewGroup innerQuestionLayout = findViewById(R.id.inner_question_layout);
                if (innerQuestionLayout.getChildAt(1) != null) {
                    RadioGroup radioGroup = (RadioGroup) innerQuestionLayout.getChildAt(1);

                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                    savedInstanceState.getBoolean("isButtonChecked");
                }
            }
        }
    }
}
