package fr.cmoatoto.quiz.lib.activities.gamerules.marathon;

import android.content.Intent;
import android.os.Bundle;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.FinishActivity;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.activities.questions.QuestionPagerAdapter;

public class QuestionMarathonActivity extends QuestionActivity {

    private static final String TAG = QuestionMarathonActivity.class.getName();

    private QuestionMarathonActivity mActivity = this;

    private int mWrongAnswerLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWrongAnswerLeft = Integer.valueOf(mGameRules.getValue1());
        if (mWrongAnswerLeft == -1) {
            mPointsTextView.setText(R.string.quiz_activity_question_marathon_wronganswersleft_unlimited);
        } else {
            mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_marathon_wronganswersleft,
                    Math.max(mWrongAnswerLeft, 1), mWrongAnswerLeft));
        }
    }

    @Override
    protected Intent insertExtraToFinishIntent(Intent i) {
        i = super.insertExtraToFinishIntent(i);

        i.putExtra(FinishActivity.KEY_QUESTION_POINTS, mViewPager.getCurrentItem() - Integer.valueOf(mGameRules.getValue1()));

        return i;
    }

    @Override
    protected QuestionPagerAdapter getQuestionPagerAdapter(JazzyViewPager viewPager) {
        return new QuestionPagerAdapter(this, viewPager, getSupportFragmentManager(), mQuestionList, true);
    }

    public boolean needCountDown() {
        return false;
    }

    public void onWrongAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
        super.onWrongAnswer(questionId, timeLeft, timeLeftInMs, player);

        if (mWrongAnswerLeft == 0) {
            mLost = true;
        } else if (mWrongAnswerLeft > 0) {
            mWrongAnswerLeft--;
            launchGrowingText(getString(R.string.quiz_fragment_question_false), getResources().getColor(R.color.holo_red_light));
            launchBounceView();
            mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_marathon_wronganswersleft,
                    Math.max(mWrongAnswerLeft, 1), mWrongAnswerLeft));
        }
    }

    public void onNewQuestion(int questionNumber) {
        super.onNewQuestion(questionNumber);

        mQuestionIndexTextView.setText(getString(R.string.quiz_activity_question_questionnumber, questionNumber));
    }

    @Override
    public void finishGame() {
        Intent i = new Intent(this, FinishMarathonActivity.class);
        i = insertExtraToFinishIntent(i);
        startActivity(i);
        finish();
    }
}
