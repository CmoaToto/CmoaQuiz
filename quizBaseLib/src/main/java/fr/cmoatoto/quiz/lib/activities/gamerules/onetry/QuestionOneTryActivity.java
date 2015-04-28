package fr.cmoatoto.quiz.lib.activities.gamerules.onetry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.FinishActivity;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.activities.questions.QuestionFragment;
import fr.cmoatoto.quiz.lib.backoffice.BackOfficeHelper;
import fr.cmoatoto.quiz.lib.backoffice.pojo.AllQuestionGame;
import fr.cmoatoto.quiz.lib.backoffice.pojo.Player;

public class QuestionOneTryActivity extends QuestionActivity {

    private static final String TAG = QuestionOneTryActivity.class.getName();

    public static final String KEY_PLAYER = "fr.cmoatoto.quiz.lib.activities.QuestionOneTryActivity.KeyPlayer";

    private QuestionOneTryActivity mActivity = this;

    private int mTotalPoints = 0;
    private int mDetailledPoints = 0;
    private int mQuestionNumber = 1;
    private int mTotalQuestion = 1;
    private Player mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayer = (Player) getIntent().getExtras().getSerializable(KEY_PLAYER);

        mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(mTotalPoints, 1), mTotalPoints));
        mTotalQuestion = mViewPager.getAdapter().getCount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mOnPause) {
            mOnPause = false;
            ((QuestionFragment) mAdapter.getItem(mViewPager.getCurrentItem())).startCountDown();
        } else {
            onPageSelected(0);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.quiz_activity_question_onetry_backpressed, Toast.LENGTH_SHORT).show();
    }

    protected Intent insertExtraToFinishIntent(Intent i) {
        i = super.insertExtraToFinishIntent(i);

        i.putExtra(FinishActivity.KEY_QUESTION_POINTS, mTotalPoints);
        i.putExtra(FinishOneTryActivity.KEY_QUESTION_POINTS_MAX, mTotalQuestion * getTotalTime());
        return i;
    }

    public boolean needCountDown() {
        return true;
    }

    public boolean shouldSaveQuestionOnBackOffice() {
        return true;
    }

    public void onMidTime(int color) {
        super.onMidTime(color);

        launchGrowingText(getString(R.string.quiz_activity_question_hurryup), color);
    }

    public void onRightAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
        super.onRightAnswer(questionId, timeLeft, timeLeftInMs, player);

        mDetailledPoints += timeLeftInMs;
        launchGrowingText("" + timeLeft, getResources().getColor(R.color.holo_green_light));
    }

    public void onAddPoint(int point, boolean addPointToOtherPlayer) {
        super.onAddPoint(point, addPointToOtherPlayer);

        mTotalPoints += point;
        mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(mTotalPoints, 1), mTotalPoints));
        launchBounceView();
    }

    @Override
    public void goToNext() {

        AllQuestionGame AllQuestionGame = new AllQuestionGame(mActivity, mTotalPoints, mDetailledPoints, mQuestionNumber, mTotalQuestion, mPlayer);
        BackOfficeHelper.sendAllQuestionGameAsync(mActivity, AllQuestionGame);

        super.goToNext();
    }

    public void onNewQuestion(int questionNumber) {
        super.onNewQuestion(questionNumber);

        mQuestionNumber = questionNumber;
        mQuestionIndexTextView.setText(getString(R.string.quiz_activity_question_onetry_questionnumber, questionNumber, mQuestionList.size()));
    }

    @Override
    public void finishGame() {
        Intent i = new Intent(this, FinishOneTryActivity.class);
        i = insertExtraToFinishIntent(i);
        startActivity(i);
        finish();
    }

    @Override
    public int getTotalTime() {
        return getResources().getInteger(R.integer.quiz_config_onetry_timeforanswerinsecond);
    }
}
