package com.oritmalki.quizapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

import com.oritmalki.quizapp.Data.QuestionsRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Answer;
import com.oritmalki.quizapp.model.Question;
import com.oritmalki.quizapp.view.QuestionFragment.QuestionFragmentListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orit on 1.2.2018.
 */

public class CreateQuizFragment extends Fragment implements View.OnClickListener, OnItemSelectedListener {

    //declare layout views...
    private static final String ARGS_QUESTION_ID = "args_question_id";
    private QuestionFragmentListener listener;
    private OnButtonClickListener mOnButtonClickListener;
    private Question question;
    private ViewGroup innerQuestionLayout;
    private ViewGroup createQuestionLayout;
    private Button saveQuestion;
    private EditText questionET;
    private Button saveET;
    private Button nextBut;
    private Button prevBut;
    SharedPreferences preferences;
    Editor editor;
    public RadioGroup rg;
    public static boolean isInReview = false;
    public static Toast toast;
    private ImageView correct;
    private ImageView inCorrect;
    private Spinner typeSpinner;
    private ViewGroup addAnswerLayout;
    private Button removeAnswerButt;
    private Button addAnswerButt;
    private ViewGroup answerButtons;
    int questionType;
    private LayoutInflater inflater;

    List<Question> questionList = new ArrayList<>();




    public static CreateQuizFragment newInstance(int questionId) {
        CreateQuizFragment createQuizFragment = new CreateQuizFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_QUESTION_ID, questionId);
        createQuizFragment.setArguments(bundle);
        return createQuizFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnButtonClickListener = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity) context).getLocalClassName()
                    + " must implement OnButtonClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.create_quiz_fragment_layout, container, false);

        initializeViews(view);

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

        switch (v.getId()) {

            case R.id.save_question:
                //save question
                if (questionET.getText() != null && questionET.getText().length() > 0) {

//                    questionList.add(new Question(getArguments().getInt(ARGS_QUESTION_ID, questionET.getText().toString(),
//                            getQuestionType(typeSpinner.toString(),))));
                }

                break;

            case R.id.add_answer_butt:
                //add another answer
                EditText answer = (EditText) inflater.inflate(R.layout.answer_et, createQuestionLayout, false);
                answer.setHint("Answer " + createQuestionLayout.indexOfChild(answer) + 1);

                break;
            case R.id.remove_answer_butt:
                //add another answer
                if (createQuestionLayout.getChildCount() != 0 && createQuestionLayout.getChildAt(0) != null) {
                    createQuestionLayout.removeViewAt(createQuestionLayout.getChildCount()-1);
                }

                break;

            default:
                mOnButtonClickListener.onButtonClicked(v);
                break;
        }


    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)

    public void initializeViews(View view) {

        //buttons and onclick events
        prevBut = view.findViewById(R.id.prev_but);
        nextBut = view.findViewById(R.id.next_but);
        questionET = view.findViewById(R.id.question_et);
        innerQuestionLayout = view.findViewById(R.id.inner_question_layout);
        saveET = view.findViewById(R.id.save_et);
        saveET.setOnClickListener(this);
        saveQuestion = view.findViewById(R.id.save_question);
        saveQuestion.setOnClickListener(this);
        prevBut.setOnClickListener(this);
        nextBut.setOnClickListener(this);
        rg = view.findViewById(R.id.radio_group);
        correct = view.findViewById(R.id.correct_image);
        inCorrect = view.findViewById(R.id.incorrect_image);
        typeSpinner = view.findViewById(R.id.spinner_quest_type);
        createQuestionLayout = view.findViewById(R.id.inner_create_layout);
        addAnswerButt = view.findViewById(R.id.add_answer_butt);

        removeAnswerButt = view.findViewById(R.id.remove_answer_butt);

        answerButtons = view.findViewById(R.id.answer_buttons);



        MainActivity.viewPager.setBackgroundColor(getResources().getColor(R.color.welcomeBkg));

        //initialize spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.question_types_array, R.layout.spinner_text);
        typeSpinner.setAdapter(adapter);
    }

//        @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
//        public void setQuestionType(int type) {
//
//            //setLayoutParams
//            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(100, 5, 24, 5);
//            layoutParams.gravity = Gravity.LEFT;
//
//            LayoutInflater inflater1 = LayoutInflater.from(getContext());
//            rg = (RadioGroup) inflater1.inflate(R.layout.answers_radiogroup_layout, innerQuestionLayout, false);
//            rg.setOrientation(LinearLayout.VERTICAL);
//            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
//                rg.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
//            }
//
//        //switch for answer types
//        //TODO add a spinner for choosing answer type. according to selection, create answer views
//
//            switch (getQuestionType(typeSpinner.getSelectedItem().toString())) {
//
//            case Answer.ONE_CORRECT_ANSWER:
//
//                final RadioButton[] rb = new RadioButton[question.getAnswers().length];
//
//
//                for (int i = 0; i < question.getAnswers().length; i++) {
//                    rb[i] = new RadioButton(getContext());
//                    rb[i].setTextSize(1, 18);
//                    rb[i].setLayoutParams(layoutParams);
//                    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
//                        rb[i].setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                        rb[i].setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG);
//                        rb[i].setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
//                    }
//
//
//                    //get answer from list
//                    Answer[] answers = question.getAnswers();
//                    rb[i].setText(answers[i].getAnswer());
//
//                    rg.addView(rb[i]);
//
//                }
//
//                innerQuestionLayout.addView(rg);
//
//                break;
//
//            case Answer.MULTIPLE_ANSWERS:
//
//                for (int i = 0; i < question.getAnswers().length; i++) {
//                    final CheckBox checkBox = new CheckBox(getContext());
//                    layoutParams.gravity = Gravity.LEFT;
//                    checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                    checkBox.setLayoutParams(layoutParams);
//                    checkBox.setTextSize(1, 18);
//                    checkBox.setId(i);
//                    //get answer from list
//                    final Answer[] answers = question.getAnswers();
//                    checkBox.setText(answers[i].getAnswer());
//
//                    //continue to set the view
//                    rg.addView(checkBox);
//
//                    innerQuestionLayout.addView(rg);
//                }
//
//                break;
//
//            case Answer.TEXT_ANSWER:
//
//                EditText editText = new EditText(getContext());
//                layoutParams.gravity = Gravity.CENTER;
//                layoutParams.width = LayoutParams.MATCH_PARENT;
//                layoutParams.rightMargin = 100;
//                editText.setLayoutParams(layoutParams);
//                editText.setTextSize(18);
//                editText.setHint("type your answer here");
//                editText.setGravity(View.TEXT_ALIGNMENT_CENTER);
//
//                rg.addView(editText);
//                if (question.getAnswers()[0].getTextAnswerInput() != null) {
//                    editText.setText(question.getAnswers()[0].getTextAnswerInput());
//                }
//                innerQuestionLayout.addView(rg);
//                break;
//        }
//    }
//
//
//    //interfaces
//
//
//    //utility methods
//
//    //get buttonViews array
//    public CheckBox[] getCheckBoxesViewsArray(ViewGroup viewGroup) {
//        CheckBox[] buttonViews = new CheckBox[viewGroup.getChildCount()];
//        for (int i = 0; i < question.getAnswers().length; i++) {
//            buttonViews[i] = (CheckBox) viewGroup.getChildAt(i);
//        }
//        return buttonViews;
//    }
//
//    public RadioButton[] getRadioButtonViewsArray(ViewGroup viewGroup) {
//        RadioButton[] buttonViews = new RadioButton[viewGroup.getChildCount()];
//        for (int i = 0; i < question.getAnswers().length; i++) {
//            buttonViews[i] = (RadioButton) viewGroup.getChildAt(i);
//        }
//        return buttonViews;
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);

        switch (selection) {
            case "One correct Answer":

            case "Multiple correct answers":

                inflater = LayoutInflater.from(getContext());
                addAnswerLayout = (ViewGroup) inflater.inflate(R.layout.add_answer_layout, createQuestionLayout, false);
                addAnswerButt.setOnClickListener(this);
                removeAnswerButt.setOnClickListener(this);

                break;
            case "Text answer":
                inflater = LayoutInflater.from(getContext());
                addAnswerLayout = (ViewGroup) inflater.inflate(R.layout.add_answer_layout, createQuestionLayout, false);
                answerButtons.setVisibility(View.GONE);
                EditText ans = (EditText) addAnswerLayout.getChildAt(1);
                ans.setHint("Enter your answer here");

                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
//convert spinner selected type into int

    public int getQuestionType(String selectedItem) {
        int type = -1;
        switch (selectedItem) {
            case "One correct Answer":
                type = Answer.ONE_CORRECT_ANSWER;
                break;
            case "Multiple correct answers":
                type = Answer.MULTIPLE_ANSWERS;
                break;
            case "Text answer":
                type = Answer.TEXT_ANSWER;
                break;
        }
        return type;
    }
}
