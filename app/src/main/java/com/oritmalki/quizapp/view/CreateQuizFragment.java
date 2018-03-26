package com.oritmalki.quizapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Answer;
import com.oritmalki.quizapp.model.Question;
import com.oritmalki.quizapp.model.Quiz;
import com.oritmalki.quizapp.view.QuestionFragment.QuestionFragmentListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Orit on 1.2.2018.
 */

public class CreateQuizFragment extends Fragment implements View.OnClickListener, OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    //declare layout views...
    private static final String ARGS_QUESTION_ID = "args_question_id";
    private QuestionFragmentListener listener;
    private OnButtonClickListener mOnButtonClickListener;
    private Question question;
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
    private ViewGroup controlTitles;
    private TextView isCorrectTv;
    int questionType;
    private LayoutInflater inflater;
    private Quiz quiz;
    private String quizName;
    private Switch[] isCorrect;

    List<Question> questionList = new ArrayList<>();
    Answer[] answers;





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

        //TODO add a dialogue for setting quiz name (only on first fragment)
        quiz = new Quiz(quizName, questionList);

        View view = inflater.inflate(R.layout.create_quiz_fragment_layout, container, false);

        setQuizNameDialog();

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
//        QuestionsRepository.getInstance().saveQuestion(question);

        listener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.save_question:
                //save question
                Question question;
                String questionText = "";
                String answer;
                int type;
                int answersCount=0;
                Answer[] answers = new Answer[answersCount];

                //get question text
                if (questionET.getText() != null && questionET.getText().length() > 0) {
                    questionText = questionET.getText().toString();
                } else {

                    //TODO popup dialogue show message "Please state question text."
                }
                    for (int i=1; i<createQuestionLayout.getChildCount(); i++) {
                        answersCount=0;
                        if (createQuestionLayout.getChildAt(i) instanceof EditText) {
                            EditText et = (EditText) createQuestionLayout.getChildAt(i);
                            answersCount++;

                            //TODO add boolean input for correct/incorrect

//                            if (et.getText() != null && et.getText().length() > 0) {
//                                answer = et.getText().toString();
//                                answers[i] = answer;
//                            }
//                            else {
//                                //TODO popup dialogue show message "You haven't filled in all answer fields"
//                            }
                        }

                    }
                    //TODO questionID = viewPager.get fragment current index...
                    question = new Question(0, questionText, getQuestionType(typeSpinner.getSelectedItem().toString()), answers);
                    questionList.add(question);


                    //TODO save and go to next page

                //TODO add another button for finish - that will lead to the list of quizes with the new one updated (add the quiz to the quiz list and go to list fragment)

//

                break;

            case R.id.add_answer_butt:
                //add another answer

                addMultipleChoiceAnswer();
                fillSwitchesArray();

                break;
            case R.id.remove_answer_butt:
                //remove answer

                removeMultipleChoiceAnswer();
                fillSwitchesArray();

                break;

            default:
                mOnButtonClickListener.onButtonClicked(v);
                break;
        }


    }

    //methods


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)

    public void initializeViews(View view) {

        //buttons and onclick events
        prevBut = view.findViewById(R.id.prev_but);
        nextBut = view.findViewById(R.id.next_but);
        questionET = view.findViewById(R.id.question_et);
        saveQuestion = view.findViewById(R.id.save_question);
        saveQuestion.setOnClickListener(this);
        prevBut.setOnClickListener(this);
        nextBut.setOnClickListener(this);
        rg = view.findViewById(R.id.radio_group);
        correct = view.findViewById(R.id.correct_image);
        inCorrect = view.findViewById(R.id.incorrect_image);
        typeSpinner = view.findViewById(R.id.spinner_quest_type);
        createQuestionLayout = view.findViewById(R.id.create_question_layout);




        MainActivity.viewPager.setBackgroundColor(getResources().getColor(R.color.welcomeBkg));

        //initialize spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.question_types_array, R.layout.spinner_text);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(this);
    }

//

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);

        switch (selection) {
            case "One correct Answer":

                createMultipleChoiceQuestion();
                Switch[] isCorrect = new Switch[10];

                //collect switches into array

                for (int i=1; i<createQuestionLayout.getChildCount(); i++) {
                    ViewGroup vg = (ViewGroup) createQuestionLayout.getChildAt(i);
                    Switch sw = (Switch) vg.getChildAt(1);
                    sw.setOnCheckedChangeListener(this::onCheckedChanged);
                    isCorrect[i] = sw;

                }





                //TODO set the isCorrect switches to be selected only once or give an error dialogue when multiple selected on saveQuestion button press

            case "Multiple correct answers":

                createMultipleChoiceQuestion();


                break;
            case "Text answer":

                createTextQuestion();

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

    public void createMultipleChoiceQuestion(){

        if (createQuestionLayout != null) {
            createQuestionLayout.removeAllViews();
        }
        inflater = LayoutInflater.from(getContext());
        addAnswerLayout = (ViewGroup) inflater.inflate(R.layout.add_answer_layout, createQuestionLayout, false);
        createQuestionLayout.addView(addAnswerLayout);
        answerButtons = addAnswerLayout.findViewById(R.id.answer_buttons);
        addAnswerButt = answerButtons.findViewById(R.id.add_answer_butt);
        removeAnswerButt = answerButtons.findViewById(R.id.remove_answer_butt);
        if (answerButtons != null)
            answerButtons.setVisibility(View.VISIBLE);
        if (addAnswerButt !=null)
            addAnswerButt.setOnClickListener(this);
        if (removeAnswerButt !=null)
            removeAnswerButt.setOnClickListener(this);

        ViewGroup controlTitles = (ViewGroup) addAnswerLayout.getChildAt(1);
        TextView isCorrectTv = (TextView) controlTitles.getChildAt(1);
        isCorrectTv.setVisibility(View.VISIBLE);

    }

    public void createTextQuestion() {
        if (createQuestionLayout !=null) {
            createQuestionLayout.removeAllViews();
        }
        inflater = LayoutInflater.from(getContext());
        addAnswerLayout = (ViewGroup) inflater.inflate(R.layout.add_answer_layout, createQuestionLayout, false);
        createQuestionLayout.addView(addAnswerLayout);
        answerButtons = addAnswerLayout.findViewById(R.id.answer_buttons);
        if (answerButtons != null)
            answerButtons.setVisibility(View.GONE);

        EditText ans = (EditText) inflater.inflate(R.layout.text_answer_et, addAnswerLayout, false);
        addAnswerLayout.addView(ans);

        controlTitles = (ViewGroup) addAnswerLayout.getChildAt(1);
        isCorrectTv = (TextView) controlTitles.getChildAt(1);

        ans.setHint("Insert text answer here");
        isCorrectTv.setVisibility(View.INVISIBLE);

    }

 /*
    Dialogs
  */

    public void setQuizNameDialog() {


        AlertDialog.Builder quizNameAlertDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            quizNameAlertDialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
        } else {
            quizNameAlertDialog = new AlertDialog.Builder(getContext());
        }

        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        ViewGroup dialogQuizNameView = (ViewGroup) inflater1.inflate(R.layout.dialog_set_quiz_name, null);

        EditText et = dialogQuizNameView.findViewById(R.id.dialog_qname_et);
        TextView tv = dialogQuizNameView.findViewById(R.id.dialog_qname_error_tv);

        quizNameAlertDialog.setTitle("Set quiz name")
                .setView(dialogQuizNameView)
                .setTitle("Set Quiz Name")
                .setCancelable(false)
                .setPositiveButton("Save", null);


        final AlertDialog d = quizNameAlertDialog.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button saveButt = d.getButton(AlertDialog.BUTTON_POSITIVE);
                saveButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        EditText et = ((AlertDialog) dialog).findViewById(R.id.dialog_qname_et);
                        TextView tv = ((AlertDialog) dialog).findViewById(R.id.dialog_qname_error_tv);

                        if (et.getText().toString() != null && et.getText().toString().length() > 0) {
                            quizName = et.getText().toString();
//                           getActivity().getActionBar().setTitle(quizName);
                            Toast.makeText(getContext(), "Created quiz: " + quizName, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
//                            tv.setText("Please enter quiz name");
                            et.requestFocus();
                            et.setError("Please enter quiz name");

                        }

                    }

                });

            }
        });

        d.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            //check if multiple answers are marked as correct
            int count=0;

                for (int i = 1; i < isCorrect.length; i++) {
                    Log.d("SwitchArrayList: ", Arrays.toString(isCorrect));
                    if (isCorrect[i].isChecked()) {
                        count++;

                    }
                }

                if (count > 1) {

                        buttonView.setChecked(false);
                        onlyOneCorrectAllowedDialog();
                        //buttonView.requestFocus();
                    //buttonView.setError("Only one correct answer is allowed.");
                }

        }


    public void onlyOneCorrectAllowedDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(R.string.one_correct_answer_title_dialog)
                .setMessage(R.string.one_correct_msg_dialog);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void addMultipleChoiceAnswer() {
        ViewGroup insertAnswer = (ViewGroup) inflater.inflate(R.layout.answer_et, createQuestionLayout, false);
        EditText answerText = (EditText) insertAnswer.getChildAt(0);


        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            insertAnswer.setId(View.generateViewId());
        } //else generate id from resourse file ids.xml

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) insertAnswer.getLayoutParams();
        if (createQuestionLayout.getChildCount() > 0) {

            //if this is the first answer
            if (addAnswerLayout == createQuestionLayout.getChildAt(0) && insertAnswer == createQuestionLayout.getChildAt(createQuestionLayout.getChildCount()-1)) {
                lp.addRule(RelativeLayout.BELOW, insertAnswer.getId());
            } else
                //if this is not the first answer inserted, reference last item id for layout.below rule
                lp.addRule(RelativeLayout.BELOW, createQuestionLayout.getChildAt(createQuestionLayout.getChildCount()-1).getId());
        }
        insertAnswer.setLayoutParams(lp);
        createQuestionLayout.addView(insertAnswer);
        answerText.setHint("Insert answer " + (createQuestionLayout.indexOfChild(createQuestionLayout.getChildAt(createQuestionLayout.getChildCount()-2))+1));

    }

    public void fillSwitchesArray() {
        isCorrect = new Switch[createQuestionLayout.getChildCount()];
        for (int i=1; i<createQuestionLayout.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) createQuestionLayout.getChildAt(1);
            isCorrect[i] = (Switch) viewGroup.getChildAt(1);
            isCorrect[i].setOnCheckedChangeListener(this::onCheckedChanged);
        }
    }

    public void removeMultipleChoiceAnswer() {
        if (createQuestionLayout.getChildAt(1) != null) {
//                    if (createQuestionLayout.getChildAt(createQuestionLayout.getChildCount()-1) instanceof EditText) {
            createQuestionLayout.removeViewAt(createQuestionLayout.getChildCount() - 1);

        }
    }

}
