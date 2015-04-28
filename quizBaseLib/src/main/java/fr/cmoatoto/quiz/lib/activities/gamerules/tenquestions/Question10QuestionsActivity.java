package fr.cmoatoto.quiz.lib.activities.gamerules.tenquestions;

import android.content.Intent;
import android.os.Bundle;
import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.FinishActivity;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.activities.questions.QuestionFragment;

public class Question10QuestionsActivity extends QuestionActivity {

    private static final String TAG = Question10QuestionsActivity.class.getName();

    private Question10QuestionsActivity mActivity = this;

    private int mTotalPoints = 0;

    private boolean mAchievement_forgotToPlay = true;
    private boolean mAchievement_allGood = true;
    private boolean mAchievement_masterLooser = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(mTotalPoints, 1), mTotalPoints));
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

    protected Intent insertExtraToFinishIntent(Intent i) {
        i = super.insertExtraToFinishIntent(i);

        i.putExtra(Finish10QuestionsActivity.KEY_ACHIEVEMENT_10QUESTIONS_FORGOTTOPLAY, mAchievement_forgotToPlay);
        i.putExtra(Finish10QuestionsActivity.KEY_ACHIEVEMENT_10QUESTIONS_ALLGOOD, mAchievement_allGood);
        i.putExtra(Finish10QuestionsActivity.KEY_ACHIEVEMENT_10QUESTIONS_MASTERLOOSER, mAchievement_masterLooser);
        i.putExtra(FinishActivity.KEY_QUESTION_POINTS, mTotalPoints);
        return i;
    }

    void cancelAchievementForgotToPlay() {
        mAchievement_forgotToPlay = false;
    }

    void cancelAchievementAllGood() {
        mAchievement_allGood = false;
    }

    void cancelAchievementMasterLooser() {
        mAchievement_masterLooser = false;
    }

    public boolean needCountDown() {
        return true;
    }

    public void onMidTime(int color) {
        super.onMidTime(color);

        launchGrowingText(getString(R.string.quiz_activity_question_hurryup), color);
    }

    public void onEndTime(int questionId) {
        super.onEndTime(questionId);

        cancelAchievementAllGood();
        cancelAchievementMasterLooser();

    }

    public void onRightAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
        super.onRightAnswer(questionId, timeLeft, timeLeftInMs, player);

        launchGrowingText("" + timeLeft, getResources().getColor(R.color.holo_green_light));

        cancelAchievementMasterLooser();
    }

    public void onWrongAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
        super.onWrongAnswer(questionId, timeLeft, timeLeftInMs, player);

        cancelAchievementAllGood();
    }

    public void onNextClick() {
        super.onNextClick();

        cancelAchievementForgotToPlay();
    }

    public void onAddPoint(int point, boolean addPointToOtherPlayer) {
        super.onAddPoint(point, addPointToOtherPlayer);

        mTotalPoints += point;
        mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(mTotalPoints, 1), mTotalPoints));
        launchBounceView();
    }

    public void onNewQuestion(int questionNumber) {
        super.onNewQuestion(questionNumber);

        mQuestionIndexTextView.setText(getString(R.string.quiz_activity_question_10questions_questionnumber, questionNumber));
    }

    @Override
    public int getTotalTime() {
        return getResources().getInteger(R.integer.quiz_config_10questions_timeforanswerinsecond);
    }

    @Override
    public void finishGame() {
        Intent i = new Intent(this, Finish10QuestionsActivity.class);
        i = insertExtraToFinishIntent(i);
        startActivity(i);
        finish();
    }
}
