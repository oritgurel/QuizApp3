package com.oritmalki.quizapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oritmalki.quizapp.Data.GenerateData;
import com.oritmalki.quizapp.Data.QuestionsRepository;
import com.oritmalki.quizapp.Data.QuizRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Answer;
import com.oritmalki.quizapp.model.Question;
import com.oritmalki.quizapp.model.Quiz;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.oritmalki.quizapp.view.QuestionFragment.toast;

public class MainActivity extends AppCompatActivity implements OnButtonClickListener, QuizListAdapterCallback {

    protected static ViewPager viewPager;

    private Button prevButton;
    private Button nextButton;
    private FrameLayout frameLayout;
    private GenerateData generateData;
    private List<Question> questionList;
    static Bundle savedInstanceState;
    private List<Quiz> listOfQuizes = new ArrayList<>();
    public static int questionListId = 0;
    public static final String QUIZ_KEY = "Questions_List";
    public static final String TO_CREATE_QUIZ_FRAGMENT = "to_create_quiz_fragment";
    List<CreateQuizFragment> createQuizFragments;
    QuestionsPagerAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    Type type = new TypeToken<List<Quiz>>() {}.getType();
    List<Quiz> quizList;
    public static Quiz selectedQuiz;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        preferences = getApplicationContext().getSharedPreferences(CreateQuizFragment.PREFS_NAME, 0);
        editor = preferences.edit();


        savedInstanceState = this.savedInstanceState;

        //Here is where the data is inserted.

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().containsKey(FinalScoreFragment.CALLING_ACTIVITY)) {

                //if got in here from "try again", reset all answers (uncheck) and score
                if (getIntent().getExtras().containsKey(FinalScoreFragment.CALLING_ACTIVITY)) {
                    if (getIntent().getExtras().getString(FinalScoreFragment.CALLING_ACTIVITY).equals("FinalScoreF")) {
                        selectedQuiz = (Quiz) getIntent().getExtras().getSerializable(MainActivity.QUIZ_KEY);
                        for (Question question : selectedQuiz.getQuestions()) {
                            question = selectedQuiz.getQuestions().get(question.getId());
                            question.setScore(0);
                            for (Answer answer : question.getAnswers()) {
                                answer.setChecked(false);
                            }
                        }
                    }
                }
            } else if (getIntent().getExtras().containsKey(TO_CREATE_QUIZ_FRAGMENT)) {
                boolean toCreateQuiz = getIntent().getExtras().getBoolean(TO_CREATE_QUIZ_FRAGMENT);
                if (toCreateQuiz) {

                    //attach creation fragment to pagerAdapter
                    initViewPagerWithCreateQuiz();
                }
            } else {
                selectedQuiz = (Quiz) getIntent().getExtras().getSerializable(WelcomeActivity.QUIZ_KEY);

                if (selectedQuiz != null) {
                    questionList = selectedQuiz.getQuestions();
                }

                //save to repository for persistance
                if (questionList != null) {
                    for (Question question : questionList) {
                        QuestionsRepository.getInstance().saveQuestion(question);
                    }

                    //attach question fragment to pagerAdapter
                    initViewPagerWithQuizQuestions();

                    frameLayout = findViewById(R.id.total_score_container);

                    if (findViewById(R.id.total_score_container) != null) {


                        // However, if we're being restored from a previous state,
                        // then we don't need to do anything and should return or else
                        // we could end up with overlapping fragments.
                        if (savedInstanceState != null) {
                            return;
                        }

//            FinalScoreFragment finalScoreFragment = new FinalScoreFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.final_score_fragment, finalScoreFragment).commit();
                    }
                }
            }
            }
        }



    public void initViewPagerWithQuizQuestions() {

        //get quizzes from shared prefs
        quizList = gson.fromJson(preferences.getString(CreateQuizFragment.QUIZ_LIST, ""), type);
        List<Question> questions = selectedQuiz.getQuestions();
        QuizRepository.getInstance().saveQuiz(new Quiz(selectedQuiz.getQuizName(), questions));
//        List<Question> questions = QuestionsRepository.getInstance().getQuestions();
        List<QuestionFragment> questionFragments = new ArrayList<>();
        for (Question question : questions) {
            questionFragments.add(QuestionFragment.newInstance(question.getId()));

        }

        adapter = new QuestionsPagerAdapter(getSupportFragmentManager(), questionFragments);
        viewPager.setAdapter(adapter);
    }

    public void initViewPagerWithCreateQuiz() {

       createQuizFragments = new ArrayList<>();
       List<Question> questionList = new ArrayList<>();

        createQuizFragments.add(CreateQuizFragment.newInstance(viewPager.getCurrentItem()));
        QuestionsPagerAdapter adapter = new QuestionsPagerAdapter(getSupportFragmentManager(), createQuizFragments);
        viewPager.setAdapter(adapter);

    }



    @Override
    public void onButtonClicked(View view) {
        int currPos;

        //TODO replace bottom buttons with a bottom navigation menu bar
        //TODO saveQuiz isn't enables till question list is not empty (show toast "question list is empty")
        //TODO convert save quiz butt into match parent width one
        //TODO set xml drawable selector for enabled/disabled button colors

        switch (view.getId()) {
            case R.id.next_but:
               currPos = viewPager.getCurrentItem();
                if (currPos != viewPager.getAdapter().getCount()) {
                    viewPager.setCurrentItem(currPos + 1, true);
                }
                if (toast != null) {
                    toast.cancel();
                }

                break;
            case R.id.prev_but:
                currPos = viewPager.getCurrentItem();
                if (currPos != 0) {
                    viewPager.setCurrentItem(currPos - 1, true);
                    if (toast != null) {
                        toast.cancel();
                    }
                }
                break;
            case R.id.review_answers:
                currPos = viewPager.getCurrentItem();
                QuestionFragment.isInReview = true;
                viewPager.setCurrentItem(currPos - 1, true);
                break;

            case R.id.next_create_butt:
                //TODO if question have'nt been saved, disable next butt and show dialog
                currPos = viewPager.getCurrentItem();

                    createQuizFragments.add(CreateQuizFragment.newInstance(currPos + 1));
                    viewPager.getAdapter().notifyDataSetChanged();

                    MainActivity.viewPager.setCurrentItem(currPos + 1, true);


                break;
            case R.id.prev_create_but:
                currPos = viewPager.getCurrentItem();
                MainActivity.viewPager.setCurrentItem(currPos - 1, true);

                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (getFragmentManager().getFragment(outState, "QuestionFragment") != null) {

            for (int i = 0; i < questionList.size(); i++) {
                for (int j = 0; j < QuestionsRepository.getInstance().getQuestion(i).getAnswers().length; j++) {

                    if (QuestionsRepository.getInstance().getQuestion(i).getAnswers()[j].isChecked()) {
                        ViewGroup innerQuestionLayout = findViewById(R.id.inner_question_layout);
                        if (innerQuestionLayout.getChildAt(3) != null) {
                            RadioGroup radioGroup = (RadioGroup) innerQuestionLayout.getChildAt(3);

                            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                            if (radioButton != null) {
                                toast.cancel();
                                outState.putBoolean("isButtonChecked", ((RadioButton) radioGroup.getChildAt(j)).isChecked());
                                outState.putBoolean("isInReview", QuestionFragment.isInReview);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (getFragmentManager().getFragment(savedInstanceState, "QuestionFragment") != null) {
            QuestionFragment.isInReview = savedInstanceState.getBoolean("isInReview");
            for (int i = 0; i < questionList.size(); i++) {
                for (int j = 0; j < QuestionsRepository.getInstance().getQuestion(i).getAnswers().length; j++) {
                    ViewGroup innerQuestionLayout = findViewById(R.id.inner_question_layout);
                    if (innerQuestionLayout.getChildAt(3) != null) {
                        RadioGroup radioGroup = (RadioGroup) innerQuestionLayout.getChildAt(3);

                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                        savedInstanceState.getBoolean("isButtonChecked");

                    }
                }
            }
        }
    }

    @Override
    public void onAdapterClick(Quiz quiz) {

    }
}
