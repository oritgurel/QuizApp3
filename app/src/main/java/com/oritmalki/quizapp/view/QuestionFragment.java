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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.oritmalki.quizapp.Data.QuestionsRepository;
import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Answer;
import com.oritmalki.quizapp.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Orit on 1.2.2018.
 */

public class QuestionFragment extends Fragment implements View.OnClickListener, TextWatcher {

    //TODO add vectorDrawable animations for correct and incorrect imageviews for review

    //declare layout views...
    private static final String ARGS_QUESTION_ID = "args_question_id";
    private static final String IS_CHECKBUTTON_CHECKED = "is_cButton_checked";
    private static final String IS_RADIOBUTTON_CHECKED = "is_rButton_checked";
    private QuestionFragmentListener listener;
    private OnButtonClickListener mOnButtonClickListener;
    private Question question;
    private ViewGroup innerQuestionLayout;
    private Button submitButton;
    private TextView questionTV;
    private Button nextBut;
    private Button prevBut;
    public static boolean isCheckButtonChecked;
    public static boolean isRadioButtonChecked;
    public String[] goodRemarks = {"Great Job!", "keep it up!", "Excellent!", "Awesom!", "That is Correct!"};
    public String[] halfCorrectRemarks = {"Almost...", "Not Exactly", "That is half correct"};
    public String[] badRemarks = {"Wrong answer", "Try again", "Are you sure?", "Nope. Try again"};
    String remark;
    SharedPreferences preferences;
    Editor editor;
    public static final String INDEX_OF_CHECKED_BUTTON_KEY = "IndexOfCheckedButton";
    public static int INDEX_OF_CHECKED_BUTTON = -1;
    public RadioGroup rg;
    public static boolean isInReview = false;
    public static Toast toast;
    private ImageView correct;
    private ImageView inCorrect;



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
//        if (context instanceof QuestionFragmentListener) {
//            listener = (QuestionFragmentListener) context;
//        }

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
        int questionId = getArguments().getInt(ARGS_QUESTION_ID);
        this.question = QuestionsRepository.getInstance().getQuestion(questionId);
    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.question_fragment_layout, container, false);

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

            case R.id.submit_but:
                //calculate score
                int finalScore = 0;
                for (Question question : questions) {
                    finalScore += question.getScore();
                }

                if (getActivity().findViewById(R.id.total_score_container) != null) {

                    if (MainActivity.savedInstanceState != null) {
                        return;
                    }

                    FinalScoreFragment finalScoreFragment = FinalScoreFragment.getInstance(finalScore);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.total_score_container, finalScoreFragment).setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(finalScoreFragment).commit();

                }



//TODO make fragment visible
//                Intent intent = new Intent(this.getContext(), FinalScoreActivity.class);
//                intent.putExtra("Score", finalScore);
//                startActivity(intent);
                break;
            default:
                mOnButtonClickListener.onButtonClicked(v);
                break;
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        question.setScore(0);
        int score;
        boolean isCorrect = question.getAnswers()[0].getCorrect();

        question.getAnswers()[0].setTextAnswerInput(s.toString());

        if (s.toString().equals("crescendo") ||
                s.toString().equals("Crescendo")) {
            score = 10;
            question.setScore(score);
            Collections.shuffle(Arrays.asList(goodRemarks));
            remark = goodRemarks[0];
            toast = Toast.makeText(getContext(), remark, Toast.LENGTH_SHORT);
            if (isInReview) {
                toast.cancel();
                correct.setVisibility(View.VISIBLE);
                inCorrect.setVisibility(View.GONE);
            } else toast.show();

        }
        else if (s.toString().length() > 10) {
            Collections.shuffle(Arrays.asList(badRemarks));
            remark = badRemarks[0];
            if (isInReview) {
                toast.cancel();
                correct.setVisibility(View.GONE);
                inCorrect.setVisibility(View.VISIBLE);
//            } else toast.show();
            }
        }
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)

    public void initializeViews(View view) {

       //buttons and onclick events
        prevBut = view.findViewById(R.id.prev_but);
        nextBut = view.findViewById(R.id.next_but);
        questionTV = view.findViewById(R.id.question_tv);
        innerQuestionLayout = view.findViewById(R.id.inner_question_layout);
        submitButton = view.findViewById(R.id.submit_but);
        submitButton.setOnClickListener(this);
        prevBut.setOnClickListener(this);
        nextBut.setOnClickListener(this);
        rg = view.findViewById(R.id.radio_group);
        correct = view.findViewById(R.id.correct_image);
        inCorrect = view.findViewById(R.id.incorrect_image);



        submitButton.setVisibility(View.INVISIBLE);
        if (question.getId() == questions.size() - 1) {
            submitButton.setVisibility(View.VISIBLE);
            nextBut.setVisibility(View.INVISIBLE);
        }

        //set data to views
        final int questionId = getArguments().getInt(ARGS_QUESTION_ID);
        questionTV.setText(questions.get(questionId).getQuestion());

        QuestionsRepository.getInstance().getQuestion(questionId).setScore(0);

        //setLayoutParams
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 5, 24, 5);
        layoutParams.gravity = Gravity.LEFT;

        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        rg = (RadioGroup) inflater1.inflate(R.layout.answers_radiogroup_layout, innerQuestionLayout, false);
        rg.setOrientation(LinearLayout.VERTICAL);
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            rg.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
        }
        int type = questions.get(questionId).getType();

        //switch for answer types
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

                            //for persistance try
                            if (buttonView.isChecked()) isChecked = true;
                            QuestionFragment.isRadioButtonChecked = isChecked;
                            //

                            int score = QuestionsRepository.getInstance().getQuestion(question.getId()).getScore();

                            for (int i = 0; i < question.getAnswers().length; i++) {
                                if (buttonView.isChecked()) {

                                    //set the answer data as checked

                                    if (buttonView.getText().equals(question.getAnswers()[i].getAnswer())) {
                                        boolean isCorrect = question.getAnswers()[i].getCorrect();
                                        if (isCorrect) {
                                            score += 10;
                                            Collections.shuffle(Arrays.asList(goodRemarks));

                                            remark = goodRemarks[0];
                                            toast = Toast.makeText(getContext(), remark, Toast.LENGTH_SHORT);
                                            if (isInReview) {
                                                toast.cancel();
                                                correct.setVisibility(View.VISIBLE);
                                                inCorrect.setVisibility(View.GONE);
                                            } else toast.show();

                                        } else if (isCorrect == false) {
                                            Collections.shuffle(Arrays.asList(badRemarks));
                                            remark = badRemarks[0];
                                            toast = Toast.makeText(getContext(), remark, Toast.LENGTH_SHORT);
                                            if (isInReview) {
                                                toast.cancel();
                                                inCorrect.setVisibility(View.VISIBLE);
                                                correct.setVisibility(View.GONE);
                                            } else toast.show();

                                        }
                                    }
                                }
                                question.getAnswers()[getCheckedRadioButtonIndex(rb)].setChecked(true);

                            }
                            question.setScore(score);

                        }
                    });

//                    if (preferences.contains(INDEX_OF_CHECKED_BUTTON_KEY)) {
//                        rb[preferences.getInt(INDEX_OF_CHECKED_BUTTON_KEY, -1)].setChecked(true);

                    rg.addView(rb[i]);
                    if (rg != null && rb != null) {
                        if (question.getAnswers()[i].isChecked()) {
                            rb[i].setChecked(true);
                        }
                        if (rb[i].isChecked() == false) {
                            question.getAnswers()[i].setChecked(false);
                        }
                    }
                }

                innerQuestionLayout.addView(rg);

                break;

            case Answer.MULTIPLE_ANSWERS:

                for (int i = 0; i < question.getAnswers().length; i++) {
                    final CheckBox checkBox = new CheckBox(getContext());
                    layoutParams.gravity = Gravity.LEFT;
                    checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    checkBox.setLayoutParams(layoutParams);
                    checkBox.setTextSize(1, 18);
                    checkBox.setId(i);
                    //get answer from list
                    final Answer[] answers = question.getAnswers();
                    checkBox.setText(answers[i].getAnswer());
                    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            question.setScore(0);
                            int score = 0;


                            if (buttonView.isChecked()) isChecked = true;
                            QuestionFragment.isCheckButtonChecked = isChecked;

                            //get count of correct answers in answers[]
                            int numOfCorrectAnswers = getCountOfcorrectAnswers(answers);

                            //get buttonViews array
                            CheckBox[] buttonViews = getCheckBoxesViewsArray(rg);

                            //get count of checked answers
                            int numOfCheckedButtons = getCountOfCheckedButtons(buttonViews);

                            //get List of checked buttons
                            List<CheckBox> checkedButtonsList = new ArrayList<>(getCheckedButtonsList(rg));

                            //get List of unchecked buttons
                            List<CheckBox> unCheckedButtonsList = new ArrayList<>(getUnCheckedButtonsList(rg));

                            //primary filtering
                            if (numOfCheckedButtons > numOfCorrectAnswers || numOfCheckedButtons < numOfCorrectAnswers) {
                                score = 0;
                            }
                            //in case that number of checked answers equals number of correct answers - check if checked answers are correct:
                            else {
                                //test the checked buttons list
                                for (int i = 0; i < checkedButtonsList.size(); i++) {

                                    //set answers data as checked
                                    question.getAnswers()[i].setChecked(true);

                                    if (question.getAnswers()[checkedButtonsList.get(i).getId()].getCorrect() == true) {
                                        score = 10; //if that's the final result of loop, checked buttons are CORRECT
                                    } else {

                                        break; //get out of loop and set score 0
                                    }
                                }
                                // if checked are correct, test the unchecked buttons, else go to set the 0 score
                                if (score == 10) {

                                    for (int i = 0; i < unCheckedButtonsList.size(); i++) {

                                        question.getAnswers()[i].setChecked(false);

                                        if (question.getAnswers()[unCheckedButtonsList.get(i).getId()].getCorrect() == false) {
                                            score = 10; //if thats the final result of loop, unChecked buttons are CORRECT

                                        } else {
                                            score = 0;
//                                                    Collections.shuffle(Arrays.asList(halfCorrectRemarks));
//                                                    remark = halfCorrectRemarks[0];
//                                                    Toast.makeText(getContext(), remark, Toast.LENGTH_SHORT).show();
                                            break; //get out of loop and set score 0
                                        }
                                    }
                                }
                                question.setScore(score);
                                if (score == 10) {
                                    Collections.shuffle(Arrays.asList(goodRemarks));
                                    remark = goodRemarks[0];
                                    toast = Toast.makeText(getContext(), remark, Toast.LENGTH_SHORT);
                                    if (isInReview) {
                                        toast.cancel();
                                        correct.setVisibility(View.VISIBLE);
                                        inCorrect.setVisibility(View.GONE);
                                    } else toast.show();
                                }
                                else if (score == 0) {
                                    Collections.shuffle(Arrays.asList(badRemarks));
                                    remark = badRemarks[0];
                                    toast = Toast.makeText(getContext(), remark, Toast.LENGTH_SHORT);
                                    if (isInReview) {
                                        toast.cancel();
                                        correct.setVisibility(View.GONE);
                                        inCorrect.setVisibility(View.VISIBLE);
                                    } else toast.show();
                            }
                        }
                        }
                    });

                    //continue to set the view
                    rg.addView(checkBox);

                    if (rg != null && rg.getChildCount() == question.getAnswers().length) {
                        if (question.getAnswers()[i].isChecked()) {
                            getCheckBoxesViewsArray(rg)[i].setChecked(true);
                        }
                        if (getCheckBoxesViewsArray(rg)[i].isChecked() == false) {
                            question.getAnswers()[i].setChecked(false);
                        }
                    }

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
                editText.addTextChangedListener(this);

                rg.addView(editText);
                if (question.getAnswers()[0].getTextAnswerInput() != null) {
                    editText.setText(question.getAnswers()[0].getTextAnswerInput());
                }
                innerQuestionLayout.addView(rg);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    //interfaces
    public interface QuestionFragmentListener {
        void onQuestionSelected(Question question);
    }

    public interface OnButtonClickListener {
        void onButtonClicked(View view);
    }

    //utility methods

    public int getCountOfcorrectAnswers(Answer[] answers) {
        int countOfcorrectAnswers = 0;
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].getCorrect() == true) {
                countOfcorrectAnswers++;
            }
        }
        return countOfcorrectAnswers;
    }

    public int getCountOfCheckedButtons(CheckBox[] checkBoxes) {
        int countOfCheckedButtons = 0;
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                countOfCheckedButtons++;
            }
        }
        return countOfCheckedButtons;
    }


    //get buttonViews array
    public CheckBox[] getCheckBoxesViewsArray(ViewGroup viewGroup) {
        CheckBox[] buttonViews = new CheckBox[viewGroup.getChildCount()];
        for (int i = 0; i < question.getAnswers().length; i++) {
            buttonViews[i] = (CheckBox) viewGroup.getChildAt(i);
        }
        return buttonViews;
    }

    public RadioButton[] getRadioButtonViewsArray(ViewGroup viewGroup) {
        RadioButton[] buttonViews = new RadioButton[viewGroup.getChildCount()];
        for (int i = 0; i < question.getAnswers().length; i++) {
            buttonViews[i] = (RadioButton) viewGroup.getChildAt(i);
        }
        return buttonViews;
    }

    public int getCheckedRadioButtonIndex(RadioButton[] radioButtons) {
        int buttonIndex = -1;
        for (int i = 0; i< question.getAnswers().length; i++) {
            if (radioButtons[i].isChecked()) {
                buttonIndex = i;
                break;
            }
        }
        return buttonIndex;
    }

    //get checked buttons ArrayList
    public List<CheckBox> getCheckedButtonsList(ViewGroup viewGroup) {
        List<CheckBox> checkedButtons = new ArrayList<>(getCountOfCheckedButtons(getCheckBoxesViewsArray(viewGroup)));
        for (int i = 0; i < question.getAnswers().length; i++) {
            CheckBox cb = (CheckBox) viewGroup.getChildAt(i);
            if (cb.isChecked()) {
                checkedButtons.add(cb);
            }
        }
        return checkedButtons;
    }


    //get unchecked buttons ArrayList
    public List<CheckBox> getUnCheckedButtonsList(ViewGroup viewGroup) {
        List<CheckBox> unCheckedButtons = new ArrayList<>(question.getAnswers().length - getCountOfCheckedButtons(getCheckBoxesViewsArray(viewGroup)));
        for (int i = 0; i < question.getAnswers().length; i++) {
            CheckBox cb = (CheckBox) viewGroup.getChildAt(i);
            if (cb.isChecked() == false) {
                unCheckedButtons.add(cb);
            }
        }
        return unCheckedButtons;
    }

    //Toast remarks
    public void toastRemarks(String remark, String[] remarks) {
        Collections.shuffle(Arrays.asList(remarks));
        remark = goodRemarks[0];
        Toast.makeText(getContext(), remark, Toast.LENGTH_SHORT).show();
    }


}
