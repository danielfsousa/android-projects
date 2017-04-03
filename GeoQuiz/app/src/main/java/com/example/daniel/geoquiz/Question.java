package com.example.daniel.geoquiz;

import android.util.Log;

import java.io.Serializable;

public class Question implements Serializable {

    private int mTextResId;
    private boolean mAnswerTrue;
    private int mAnswerStatus;
    private boolean mCheated;

    public static final int NOT_ANSWERED = 0;
    public static final int CORRECT = 1;
    public static final int INCORRECT = 2;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mAnswerStatus = 0;
        mCheated = false;
    }

    public boolean hasCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public int getAnswerStatus() {
        return mAnswerStatus;
    }

    public void setAnswerStatus(int answerStatus) {
        mAnswerStatus = answerStatus;
    }

    public boolean wasAnswered() {
        return this.getAnswerStatus() != Question.NOT_ANSWERED;
    }

    public static double getPercentage(Question[] questions) {
        int[] num = new int[]{0, 0, 0};

        for (Question question : questions) {
            num[question.getAnswerStatus()]++;
        }

        int total = questions.length - num[Question.NOT_ANSWERED];

        return (double) num[Question.CORRECT] / total;
    }
}
