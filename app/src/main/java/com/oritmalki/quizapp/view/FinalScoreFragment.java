package com.oritmalki.quizapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oritmalki.quizapp.R;

import static com.oritmalki.quizapp.view.MainActivity.QUESTIONS_LIST_KEY;
import static com.oritmalki.quizapp.view.MainActivity.viewPager;

/**
 * Created by Orit on 4.2.2018.
 */

public class FinalScoreFragment extends Fragment implements View.OnClickListener {

    OnButtonClickListener mOnButtonClickListener;
    TextView finalScoreTV;
    Button tryAgainButt;
    Button reviewBut;
    Button quitBut;
    Button home;
    int finalScore;
    final static String ARGS_FINAL_SCORE = "args_final_score";


    public static FinalScoreFragment getInstance(int finalScore) {
        FinalScoreFragment finalScoreFragment = new FinalScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_FINAL_SCORE, finalScore);
        finalScoreFragment.setArguments(bundle);
        return finalScoreFragment;

    }

    //TODO add navigate back to quiz or select/create new quiz

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
        int finalScore = getArguments().getInt(ARGS_FINAL_SCORE);
        this.finalScore = finalScore;


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.final_score_fragment, container, false);


        finalScoreTV = view.findViewById(R.id.final_score);
        tryAgainButt = view.findViewById(R.id.try_again_butt);
        quitBut = view.findViewById(R.id.quit_but);
        reviewBut = view.findViewById(R.id.review_answers);
        home = view.findViewById(R.id.home);

        tryAgainButt.setOnClickListener(this);
        quitBut.setOnClickListener(this);
        reviewBut.setOnClickListener(this);
        home.setOnClickListener(this);

        finalScoreTV.setText(String.valueOf(finalScore));
        Toast.makeText(getContext(), "Final Score: " + finalScore, Toast.LENGTH_SHORT).show();

        return view;
    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_answers:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit();
                QuestionFragment.isInReview = true;
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.quit_but:
                getActivity().finishAffinity();
                break;
            case R.id.try_again_butt:

                QuestionFragment.isInReview = false;
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(QUESTIONS_LIST_KEY, WelcomeActivity.selectedQuiz);
                startActivity(intent);
                break;
            case R.id.home:
                Intent intent1 = new Intent(getContext(), WelcomeActivity.class);
                startActivity(intent1);
        }
    }




}