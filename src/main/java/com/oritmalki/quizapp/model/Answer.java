package com.oritmalki.quizapp.model;

/**
 * Created by Orit on 31.1.2018.
 */

public class Answer {
    private String answer;
    private boolean isCorrect;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static final int ONE_CORRECT_ANSWER = 1;
    public static final int MULTIPLE_ANSWERS = 2;
    public static final int TEXT_ANSWER = 3;

    public Answer(String answer, Boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

}
