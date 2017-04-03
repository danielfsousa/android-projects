package com.example.daniel.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_CHEATS = "cheats";

    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mCheatsTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int mRemainingCheats = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mCheatsTextView = (TextView) findViewById(R.id.cheats_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);

        if (savedInstanceState != null) {
            mRemainingCheats = savedInstanceState.getInt(KEY_CHEATS, mRemainingCheats);
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionBank = (Question[]) savedInstanceState.getSerializable(KEY_QUESTIONS);
            checkButtonsEnabled();
        }

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        mCheatsTextView.setText(String.format(getString(R.string.remaining_cheats_text_view), mRemainingCheats));

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                checkButtonsEnabled();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                checkButtonsEnabled();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               nextQuestion();
           }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            decreaseCheats();
            Log.d(TAG, "mRemainingCheats = " + mRemainingCheats);
            mQuestionBank[mCurrentIndex].setCheated(CheatActivity.wasAnswerShown(data));
        }
    }

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        checkButtonsEnabled();
        checkQuizCompleted();
    }

    private void previousQuestion() {
        mCurrentIndex = (mCurrentIndex - 1) != -1
                ? ((mCurrentIndex - 1) % mQuestionBank.length)
                : (mQuestionBank.length - 1);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        checkButtonsEnabled();
        checkQuizCompleted();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (userPressedTrue == answerIsTrue) {
            if (mQuestionBank[mCurrentIndex].hasCheated()) {
                messageResId = R.string.judgment_toast;
            } else {
                messageResId = R.string.correct_toast;
            }
            mQuestionBank[mCurrentIndex].setAnswerStatus(Question.CORRECT);
        } else {
            messageResId = R.string.incorrect_toast;
            mQuestionBank[mCurrentIndex].setAnswerStatus(Question.INCORRECT);
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void checkButtonsEnabled() {
        if (mQuestionBank[mCurrentIndex].wasAnswered()) {
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
        } else {
            mFalseButton.setEnabled(true);
            mTrueButton.setEnabled(true);
        }

        if (mRemainingCheats == 0) {
            mCheatButton.setEnabled(false);
        } else {
            mCheatButton.setEnabled(true);
        }
    }

    private void checkQuizCompleted() {
        for (Question question : mQuestionBank) {
            if (question.getAnswerStatus() == Question.NOT_ANSWERED) {
                return;
            }
        }
        long porcentagem = Math.round(Question.getPercentage(mQuestionBank) * 100);
        Toast.makeText(this, "Resultado: " + porcentagem + "%", Toast.LENGTH_SHORT).show();
    }

    private void decreaseCheats() {
        if (!mQuestionBank[mCurrentIndex].hasCheated()) {
            mRemainingCheats--;
            mCheatsTextView.setText(String.format(getString(R.string.remaining_cheats_text_view), mRemainingCheats));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_CHEATS, mRemainingCheats);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putSerializable(KEY_QUESTIONS, mQuestionBank);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
