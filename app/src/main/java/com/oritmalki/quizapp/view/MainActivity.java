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
import com.oritmalki.quizapp.model.Quiz;
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
    public static final String QUESTIONS_LIST_KEY = "Questions_List";
    public static final String TO_CREATE_QUIZ_FRAGMENT = "to_create_quiz_fragment";
    List<CreateQuizFragment> createQuizFragments;
    QuestionsPagerAdapter adapter;
    int currPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);



        savedInstanceState = this.savedInstanceState;

        //Here is where the data is inserted.

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().containsKey(TO_CREATE_QUIZ_FRAGMENT)) {
                boolean toCreateQuiz = (boolean) getIntent().getExtras().getBoolean(TO_CREATE_QUIZ_FRAGMENT);
                if (toCreateQuiz) {

                    //attach creation fragment to pagerAdapter
                    initViewPagerWithCreateQuiz();
                }

            } else {
                Quiz selectedQuiz = (Quiz) getIntent().getExtras().getSerializable(QUESTIONS_LIST_KEY);

                if (selectedQuiz != null) {
                    questionList = selectedQuiz.getQuestions();
                }

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

        List<Question> questions = QuestionsRepository.getInstance().getQuestions();
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

        createQuizFragments.add(CreateQuizFragment.newInstance(currPos));
        QuestionsPagerAdapter adapter = new QuestionsPagerAdapter(getSupportFragmentManager(), createQuizFragments);
        viewPager.setAdapter(adapter);
        currPos = viewPager.getCurrentItem();
    }



    @Override
    public void onButtonClicked(View view) {

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
                QuestionFragment.isInReview = true;
                viewPager.setCurrentItem(currPos - 1, true);
                break;

            case R.id.next_create_butt:
                currPos = viewPager.getCurrentItem();
                createQuizFragments.add(CreateQuizFragment.newInstance(currPos+1));
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
