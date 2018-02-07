package com.oritmalki.quizapp.view;

import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.view.QuestionFragment.OnButtonClickListener;

/**
 * Created by Orit on 4.2.2018.
 */

public class FinalScoreActivity extends AppCompatActivity implements View.OnClickListener {

    OnButtonClickListener mOnButtonClickListener;
    TextView finalScoreTV;
    Button tryAgainButt;
    Button reviewBut;
    Button quitBut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_score_activity);
        finalScoreTV = findViewById(R.id.final_score);
        tryAgainButt = findViewById(R.id.try_again_butt);
        quitBut = findViewById(R.id.quit_but);
        reviewBut = findViewById(R.id.review_answers);

        tryAgainButt.setOnClickListener(this);
        quitBut.setOnClickListener(this);
        reviewBut.setOnClickListener(this);

        int finalScore = getIntent().getExtras().getInt("Score");
        finalScoreTV.setText(String.valueOf(finalScore));
        Toast.makeText(this, "Final Score: " + finalScore, Toast.LENGTH_SHORT).show();


    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_answers:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                //set right and wrong textViews visible in fragment
                break;
            case R.id.quit_but:
                this.finishAffinity();
                 break;
            case R.id.try_again_butt:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);

        }
    }
}
