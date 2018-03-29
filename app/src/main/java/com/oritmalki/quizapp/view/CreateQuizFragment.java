package com.oritmalki.quizapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.oritmalki.quizapp.Data.QuestionsRepository;
import com.oritmalki.quizapp.Data.QuizRepository;
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

public class CreateQuizFragment extends Fragment implements View.OnClickListener, OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    //declare layout views...
    private static final String ARGS_QUESTION_ID = "args_question_id";
    private static String ARGS_CURRENT_ITEM = "args_current_item";
    private QuestionFragmentListener listener;
    private OnButtonClickListener mOnButtonClickListener;
    private Question question;
    private ViewGroup createQuestionLayout;
    private Button saveQuestion;
    private EditText questionET;
    private Button saveET;
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
    private Button nextCreateBut;
    private Button prevCreateBut;
    private Button saveQuiz;
    private ViewGroup answerButtons;
    private ViewGroup controlTitles;
    private TextView isCorrectTv;
    int questionType;
    private LayoutInflater inflater;
    private Quiz quiz;
    private String quizName;
    private Switch[] isCorrect;
    private EditText[] inputAnswerEt;
    private ViewGroup questionParamContainer;
    private ViewGroup questionLayout;
    public static boolean isSwitchChecked;
    private static int currentItem;
    boolean quizNameIsSet = false;
    int type;
    QuestionsRepository questionsRepository = QuestionsRepository.getInstance();


    List<Question> questionList = new ArrayList<>();

    Answer[] answers;





    public static CreateQuizFragment newInstance(int currentItem) {
        CreateQuizFragment createQuizFragment = new CreateQuizFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_CURRENT_ITEM, currentItem);
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
        currentItem = (int) this.getArguments().get(ARGS_CURRENT_ITEM);
        //TODO save quiz data to shared preferences for persistancy

    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        quiz = new Quiz(quizName, questionList);

        View view = inflater.inflate(R.layout.create_quiz_fragment_layout, container, false);
        currentItem = (int) getArguments().get(ARGS_CURRENT_ITEM);

        if (quizNameIsSet ==false && currentItem == 0) {
            setQuizNameDialog();
            quizNameIsSet = true;

        }

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


                //get question text
                if (questionET.getText() != null && questionET.getText().length() > 0) {
                    questionText = questionET.getText().toString();
                } else {

                    questionET.requestFocus();
                    questionET.setError("Please fill in question text.");
                }


                //get answers texts and correctness

                try {
                    answers = new Answer[inputAnswerEt.length];
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(), "Please add answers to your question", Toast.LENGTH_SHORT).show();
                }

                for (int i=0; i<inputAnswerEt.length; i++) {

                    //if text fields are not empty
                    if (inputAnswerEt[i].getText() != null && inputAnswerEt[i].getText().length() > 0) {


                        answers[i] = new Answer(inputAnswerEt[i].getText().toString(), isCorrect[i].isChecked());


                    } else {
                        inputAnswerEt[i].setError("Please fill answer text");
                        break;
                    }

                }


                //check if no correct answer was switched
                int countOff = 0;
                int countError = 0;
                for (int i=0; i<inputAnswerEt.length; i++) {
                    //get total errors
                    if (inputAnswerEt[i].getError() !=null && inputAnswerEt[i].getError().equals("Please fill answer text")) {
                        countError++;
                    } if (questionET.getError() != null && questionET.getError().equals("Please fill answer text")) {
                    }
                    //get total unchecked switches
                    if (isCorrect[i].isChecked() == false) {
                        countOff++;
                    }

                }

                    //if not all fields have text - break operation and don't save
                    if (countError > 0) {
                        Toast.makeText(getContext(), "Must fill all fields", Toast.LENGTH_SHORT).show();
                    }

                    else if (countOff == inputAnswerEt.length) {
                        //display error dialog
                    atLeastOneCorrectAllowedDialog();
                }

                    else {
                        //save question
                        type = getQuestionType(typeSpinner.getSelectedItem().toString());
                        currentItem = (int) getArguments().get(ARGS_CURRENT_ITEM);
                        question = new Question(currentItem, questionText, type, answers);
                        questionList.add(question);
                        Toast.makeText(getContext(), "Question saved", Toast.LENGTH_SHORT).show();
                        Log.d("Question saved: ", question.toString());
                        Log.d("QuestionList", questionList.toString());

                        //TODO add another button for finish - that will lead to the list of quizes with the new one updated (add the quiz to the quiz list and go to list fragment
                }

                break;

            case R.id.add_answer_butt:
                //add another answer

                addMultipleChoiceAnswer();
                fillAnswersArray();

                break;
            case R.id.remove_answer_butt:
                //remove answer

                removeMultipleChoiceAnswer();
                fillAnswersArray();

                break;
            case R.id.save_quiz:
                Quiz quiz = new Quiz(quizName, questionList);
                QuizRepository.getInstance().saveQuiz(quiz);
//                QuizListSelectionFragment.adapter.notifyDataSetChanged();
                QuizListSelectionFragment selectionFragment = new QuizListSelectionFragment();
                getFragmentManager().beginTransaction().replace(R.id.total_score_container, selectionFragment).show(selectionFragment).commit();



            default:
                mOnButtonClickListener.onButtonClicked(v);
                break;
        }


    }

    //methods


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)

    public void initializeViews(View view) {

        //buttons and onclick events
        prevCreateBut = view.findViewById(R.id.prev_create_but);
        nextCreateBut = view.findViewById(R.id.next_create_butt);
        questionET = view.findViewById(R.id.question_et);
        questionET.addTextChangedListener(this);
        saveQuestion = view.findViewById(R.id.save_question);
        saveQuestion.setOnClickListener(this);
        prevCreateBut.setOnClickListener(this);
        nextCreateBut.setOnClickListener(this);
        rg = view.findViewById(R.id.radio_group);
        correct = view.findViewById(R.id.correct_image);
        inCorrect = view.findViewById(R.id.incorrect_image);
        typeSpinner = view.findViewById(R.id.spinner_quest_type);
        createQuestionLayout = view.findViewById(R.id.create_question_layout);
        questionParamContainer = view.findViewById(R.id.question_params_container);
        questionLayout = view.findViewById(R.id.question_layout);
        saveQuiz = view.findViewById(R.id.save_quiz);
        saveQuiz.setOnClickListener(this);


        questionET.setHint(currentItem+1 + ". Enter question text here");


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
                type = getQuestionType("One correct Answer");
                createMultipleChoiceQuestion();
                Switch[] isCorrect = new Switch[10];

                //collect switches into array

                for (int i=1; i<createQuestionLayout.getChildCount(); i++) {
                    ViewGroup vg = (ViewGroup) createQuestionLayout.getChildAt(i);
                    Switch sw = (Switch) vg.getChildAt(1);
                    sw.setOnCheckedChangeListener(this::onCheckedChanged);
                    isCorrect[i] = sw;

                }

            case "Multiple correct answers":
                type = getQuestionType("Multiple correct answers");
                createMultipleChoiceQuestion();


                break;
            case "Text answer":
                type = getQuestionType("Text answer");
                createTextQuestion();
//TODO fix bug when saving text question
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
            if (questionLayout.getChildAt(3).equals(addAnswerLayout)) {
                questionLayout.removeView(addAnswerLayout);
        }
        inflater = LayoutInflater.from(getContext());
        addAnswerLayout = (ViewGroup) inflater.inflate(R.layout.add_answer_layout, questionLayout, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(addAnswerLayout.getLayoutParams());
        params.addRule(RelativeLayout.BELOW, typeSpinner.getId());
        addAnswerLayout.setLayoutParams(params);

        questionLayout.addView(addAnswerLayout, 3);
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
        if (questionLayout.getChildAt(3).equals(addAnswerLayout)) {
            questionLayout.removeView(addAnswerLayout);
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

        //check if multiple single-choice answers are marked as correct. if so,  popup dialog
        int count = 0;

        if (typeSpinner.getSelectedItem().equals("One correct Answer")) {

            for (int i = 0; i < isCorrect.length; i++) {
                Log.d("SwitchArrayList: ", Arrays.toString(isCorrect));
                if (isCorrect[i].isChecked()) {
                    count++;
                    if (count > 1) {
                        buttonView.setChecked(false);
                        onlyOneCorrectAllowedDialog();
                        break;
                    }

                }
            }

        }
    }

    public void showDialogIfNonSelectedAsCorrect() {
        int countOff = 0;
        for (int i=1; i<inputAnswerEt.length; i++) {
            if (isCorrect[i].isChecked() == false) {
                countOff++;
            }
        }

        if (countOff == inputAnswerEt.length) {
            atLeastOneCorrectAllowedDialog();
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

    public void atLeastOneCorrectAllowedDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(R.string.no_correct_checked_dialog_title)
                .setMessage(R.string.no_correct_checked_dialog_msg);

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
        answerText.setHint("Insert answer " + (createQuestionLayout.indexOfChild(createQuestionLayout.getChildAt(createQuestionLayout.getChildCount()-1))+1));

    }

    public void fillAnswersArray() {
        isCorrect = new Switch[createQuestionLayout.getChildCount()];
        inputAnswerEt = new EditText[createQuestionLayout.getChildCount()];
        for (int i=0; i<createQuestionLayout.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) createQuestionLayout.getChildAt(i);
            for (int j=i; j<createQuestionLayout.getChildCount(); j++) {
                isCorrect[j] = (Switch) viewGroup.getChildAt(1);
                isCorrect[j].setChecked(false);
                isCorrect[j].setOnCheckedChangeListener(this);
                inputAnswerEt[j] = (EditText) viewGroup.getChildAt(0);
                inputAnswerEt[j].addTextChangedListener(this);

            }
        }
    }



    public void removeMultipleChoiceAnswer() {
        if (createQuestionLayout.getChildAt(0) != null) {
            createQuestionLayout.removeViewAt(createQuestionLayout.getChildCount() - 1);

        }
    }

    public boolean isValidInput(String s) {
        boolean isValidInput;
        if (s != null && s.length() > 0) {
            isValidInput = true;
        } else {
            isValidInput = false;
        }
        return isValidInput;
    }

    public boolean isQuestionValid(String q) {
        q = questionET.getText().toString();
        return isValidInput(q);
    }

//    public boolean areAllAnswersValid(String a) {
//
//        int countValid=0;
//        for (int i=0; i<createQuestionLayout.getChildCount(); i++) {
//            ViewGroup viewGroup = (ViewGroup) createQuestionLayout.getChildAt(i);
//            EditText et = (EditText) viewGroup.getChildAt(0);
//           if (a == et.getText().toString()) {
//               if (isValidInput(a)) {
//                   countValid++;
//               }
//           }
//
//        return isValidInput();
//    }

    public void setSaveQuestionButtonState(String s) {
        saveQuestion.setEnabled(false);
        if (isValidInput(s)) {
            if (createQuestionLayout.getChildCount() != 0) {
                int countValidFields=0;
                int countOff=0;
                for (int i=0; i<createQuestionLayout.getChildCount(); i++) {
                    ViewGroup viewGroup = (ViewGroup) createQuestionLayout.getChildAt(i);
                    EditText et = (EditText) viewGroup.getChildAt(0);
                    if (isValidInput(s)) {
                        countValidFields++;
                    }
                    if (isCorrect[i].isChecked() == false) {
                        countOff++;
                    }
                }
                if (countValidFields == createQuestionLayout.getChildCount() && countOff != createQuestionLayout.getChildCount()) {
                    saveQuestion.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        setSaveQuestionButtonState(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void notifyUpdate() {

    }

}
