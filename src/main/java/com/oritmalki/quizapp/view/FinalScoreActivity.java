package com.oritmalki.quizapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.oritmalki.quizapp.R;

/**
 * Created by Orit on 4.2.2018.
 */

public class FinalScoreActivity extends AppCompatActivity {

    TextView finalScoreTV;
    Button tryAgainButt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_score_activity);
        finalScoreTV = findViewById(R.id.final_score);
        tryAgainButt = findViewById(R.id.try_again_butt);
        tryAgainButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        int finalScore = getIntent().getExtras().getInt("Score");
        finalScoreTV.setText(String.valueOf(finalScore));
    }
}
