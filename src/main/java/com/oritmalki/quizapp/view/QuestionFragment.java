package com.oritmalki.quizapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import com.oritmalki.quizapp.Data.GenerateData;
import com.oritmalki.quizapp.Data.QuestionsRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Answer;
import com.oritmalki.quizapp.model.Question;

import java.util.List;

/**
 * Created by Orit on 1.2.2018.
 */

public class QuestionFragment extends Fragment implements View.OnClickListener {

    //declare layout views...
    private static final String ARGS_QUESTION_ID = "args_question_id";
    private QuestionFragmentListener listener;
    private Question question;
    private GenerateData generateData;
    private ViewGroup questionLayout;
    private ViewGroup innerQuestionLayout;
    private Button submitButton;
    private TextView questionTV;



    private List<Question> questions = QuestionsRepository.getInstance().getQuestions();




    public static QuestionFragment newInstance(int questionId) {
        QuestionFragment questionFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_QUESTION_ID, questionId);
        questionFragment.setArguments(bundle);
        return questionFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuestionFragmentListener) {
            listener = (QuestionFragmentListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int questionId = getArguments().getInt(ARGS_QUESTION_ID);
        this.question = QuestionsRepository.getInstance().getQuestion(questionId);




    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.question_fragment_layout, container, false);

        //list of differend backgrounds for layout view
//        List<Drawable> backgrounds = new ArrayList<>();
//        backgrounds.add(getResources().getDrawable(R.drawable.back1));
//        backgrounds.add(getResources().getDrawable(R.drawable.back2));
//        backgrounds.add(getResources().getDrawable(R.drawable.back3));

        //init views

        questionTV = view.findViewById(R.id.question_tv);
        innerQuestionLayout = view.findViewById(R.id.inner_question_layout);
        submitButton = view.findViewById(R.id.submit_but);
        submitButton.setOnClickListener(this);


        submitButton.setVisibility(View.INVISIBLE);
        if(question.getId() == questions.size()-1)
            submitButton.setVisibility(View.VISIBLE);

        //set data to views
        final int questionId = getArguments().getInt(ARGS_QUESTION_ID);
        questionTV.setText(questions.get(questionId).getQuestion());


//        setLayoutParams
        float density = getResources().getDisplayMetrics().density;
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 5, 24, 5);
        layoutParams.gravity = Gravity.LEFT;

        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        RadioGroup rg = (RadioGroup) inflater1.inflate(R.layout.answers_radiogroup_layout, innerQuestionLayout, false);
        rg.setOrientation(LinearLayout.VERTICAL);
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            rg.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
        }
        int type = questions.get(questionId).getType();
            switch (type) {
                case Answer.ONE_CORRECT_ANSWER:

                    final RadioButton[] rb = new RadioButton[question.getAnswers().length];


        for (int i = 0; i < question.getAnswers().length; i++) {
                        rb[i] = new RadioButton(getContext());
                        rb[i].setTextSize(1, 18);
                        rb[i].setLayoutParams(layoutParams);
                        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
                            rb[i].setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            rb[i].setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG);
                            rb[i].setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        }




                        //get answer from list
                        Answer[] answers = question.getAnswers();
                        rb[i].setText(answers[i].getAnswer());
                        rb[i].setOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                question.setScore(0);

                                int score = QuestionsRepository.getInstance().getQuestion(question.getId()).getScore();

                                for (int i=0; i<question.getAnswers().length; i++) {
                                    String answer = question.getAnswers()[i].getAnswer();
                                    if (buttonView.isChecked()) {
                                        boolean isCorrect = question.getAnswers()[i].getCorrect();
                                        if (isCorrect) {
                                            score +=10;
                                            question.setScore(score);
                                        }

                                    }
                                }
                            }
                        });
                        rg.addView(rb[i]);
                    }
                    innerQuestionLayout.addView(rg);

                    break;
                case Answer.MULTIPLE_ANSWERS:

                    for (int i = 0; i < question.getAnswers().length; i++) {
                        CheckBox checkBox = new CheckBox(getContext());
                        layoutParams.gravity = Gravity.LEFT;
                        checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        checkBox.setLayoutParams(layoutParams);
                        checkBox.setTextSize(1, 18);
                        //get answer from list
                        Answer[] answers = question.getAnswers();
                        checkBox.setText(answers[i].getAnswer());
//                        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                int score = QuestionsRepository.getInstance().getQuestion(getId()).getScore();
//
//                                for (int i=0; i<buttonView.length(); i++) {
//                                    if (buttonView.isChecked()) {
//                                        boolean isCorrect = question.getAnswers()[i].getCorrect();
//                                        if (for ())
//                                            QuestionsRepository.getInstance().getQuestion(getId()).setScore(score);
//                                        }
//
//                                    }
//                                }
//                            }
//                        });
                        rg.addView(checkBox);
                    }
                    innerQuestionLayout.addView(rg);

                    break;
                case Answer.TEXT_ANSWER:
                    EditText editText = new EditText(getContext());
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.width = LayoutParams.MATCH_PARENT;
                    layoutParams.rightMargin = 100;
                    editText.setLayoutParams(layoutParams);
                    editText.setTextSize(18);
                    editText.setHint("type your answer here");
                    editText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    rg.addView(editText);
                    innerQuestionLayout.addView(rg);
                    break;
            }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        QuestionsRepository.getInstance().saveQuestion(question);


        listener = null;
    }

    @Override
    public void onClick(View v) {

        //calculate score
        int finalScore = question.getScore();

        Intent intent = new Intent(this.getContext(), FinalScoreActivity.class);
        intent.putExtra("Score",finalScore);
        startActivity(intent);
    }


    public interface QuestionFragmentListener {
        void onQuestionSelected(Question question);
    }

}
