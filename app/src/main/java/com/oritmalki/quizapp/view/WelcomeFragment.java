package com.oritmalki.quizapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oritmalki.quizapp.R;

/**
 * Created by Orit on 27.2.2018.
 */

public class WelcomeFragment extends Fragment implements View.OnClickListener {

    private Button selectQuiz;
    private Button createQuiz;
    private WelcomFragmentCallback mCallback;

    public static WelcomeFragment newInstance() {
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        return welcomeFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (WelcomFragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement WelcomFragmentCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        selectQuiz = view.findViewById(R.id.select_quiz);
        createQuiz = view.findViewById(R.id.create_quiz);

        selectQuiz.setOnClickListener(this::onClick);
        createQuiz.setOnClickListener(this::onClick);


        return view;
    }


    @Override
    public void onClick(View v) {
        mCallback.onClick(v);
    }
}
