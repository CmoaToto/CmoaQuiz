package fr.cmoatoto.quiz.lib.activities.gamerules.splitscreen;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.FinishActivity;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.activities.questions.QuestionFragment;
import fr.cmoatoto.quiz.lib.activities.questions.QuestionPagerAdapter;
import fr.cmoatoto.quiz.lib.animations.GrowingViewRunnable;
import fr.cmoatoto.quiz.lib.animations.ViewBouncer;
import fr.cmoatoto.quiz.lib.elements.QuestionList.Question;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;
import fr.cmoatoto.quiz.lib.views.MyAutoScrollViewPager;
import fr.cmoatoto.quiz.lib.views.RotatingRelativeLayout;

public class QuestionSplitScreenActivity extends QuestionActivity {

    private static final String TAG = QuestionSplitScreenActivity.class.getName();

    private QuestionSplitScreenActivity mActivity = this;

    private View mMainView2;

    private MyAutoScrollViewPager mViewPager2;
    private TextView mTopLeftTextView2;
    private TextView mTopRightTextView2;

    private FrameLayout mGrowingTextViewContainer2;
    private TextView mGrowingTextView2;

    private ViewBouncer mBounceTopRightTextViewTask2;

    private Handler mGrowingTextHandler2 = new Handler();
    private GrowingViewRunnable mGrowingTextRunnable2;

    private QuestionPagerAdapter mAdapter2;

    private int mTotalPoints1 = 0;
    private int mTotalPoints2 = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(mTotalPoints1, 1), mTotalPoints1));
        mTopRightTextView2.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(mTotalPoints2, 1), mTotalPoints2));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_splitscreen;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void initView(View mainView) {

        mMainView = mainView.findViewById(R.id.activity_question_splitscreen_element_1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mMainView.setRotation(180);
        } else {
            ((RotatingRelativeLayout) mMainView).rotate(180);
        }
        TypeFaceUtils.applyFontToHierarchyView(mMainView);

        mMainView2 = mainView.findViewById(R.id.activity_question_splitscreen_element_2);
        TypeFaceUtils.applyFontToHierarchyView(mMainView2);

        mQuestionIndexTextView = (TextView) mMainView.findViewById(R.id.activity_question_textview_index);
        mPointsTextView = (TextView) mMainView.findViewById(R.id.activity_question_textview_points);
        mBouncePointsTextViewTask = new ViewBouncer(mPointsTextView).setInfiniteBounce(false).setTime(getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond)).setRatio(2f);

        mTopLeftTextView2 = (TextView) mMainView2.findViewById(R.id.activity_question_textview_index);
        mTopRightTextView2 = (TextView) mMainView2.findViewById(R.id.activity_question_textview_points);
        mBounceTopRightTextViewTask2 = new ViewBouncer(mTopRightTextView2).setInfiniteBounce(false).setTime(getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond)).setRatio(2f);

        mGrowingTextView = (TextView) mMainView.findViewById(R.id.activity_question_textview_growingtext);
        mGrowingTextViewContainer = (FrameLayout) mMainView.findViewById(R.id.activity_question_framelayout_growingtextcontainer);
        mGrowingTextRunnable = new GrowingViewRunnable(mGrowingTextViewContainer, mGrowingTextView, 2000, 10);

        mGrowingTextView2 = (TextView) mMainView2.findViewById(R.id.activity_question_textview_growingtext);
        mGrowingTextViewContainer2 = (FrameLayout) mMainView2.findViewById(R.id.activity_question_framelayout_growingtextcontainer);
        mGrowingTextRunnable2 = new GrowingViewRunnable(mGrowingTextViewContainer2, mGrowingTextView2, 2000, 10);

        mViewPager = (MyAutoScrollViewPager) mMainView.findViewById(R.id.activity_question_viewpager_question);
        mAdapter = getQuestionPagerAdapter(mViewPager, 1);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setTransitionEffect(TransitionEffect.Tablet);
        mViewPager.setSwypable(false);
        mViewPager.setScrollDuration(500);

        mViewPager.setOnPageChangeListener(this);

        mViewPager2 = (MyAutoScrollViewPager) mMainView2.findViewById(R.id.activity_question_viewpager_question);
        mViewPager2.setId(mViewPager.getId() + 1);
        mAdapter2 = getQuestionPagerAdapter(mViewPager2, 2);
        mViewPager2.setAdapter(mAdapter2);
        mViewPager2.setTransitionEffect(TransitionEffect.Tablet);
        mViewPager2.setSwypable(false);
        mViewPager2.setScrollDuration(500);

        mViewPager2.setOnPageChangeListener(this);
    }

    QuestionPagerAdapter getQuestionPagerAdapter(JazzyViewPager viewPager, int player) {
        return new QuestionPagerAdapter(this, viewPager, getSupportFragmentManager(), mQuestionList, true, player);
    }

    private void launchGrowingText(String text, int color, int player) {
        if (player == 1) {
            launchGrowingText(text, color);
        } else {
            launchGrowingText2(text, color);
        }
    }

    void launchGrowingText2(String text, int color) {

        mGrowingTextView2.setText(text);
        mGrowingTextView2.setTextColor(color);
        mGrowingTextHandler2.removeCallbacks(mGrowingTextRunnable2);
        mGrowingTextHandler2.post(mGrowingTextRunnable2);
    }

    void launchBounceView2() {
        if (SharedPrefUtils.getAcceptAllAnimations(this)) {
            mBounceTopRightTextViewTask2.bounce();
        }
    }

    public boolean mayShowExplanation(Question question, boolean answered) {
        return false;
    }

    public boolean needCountDown() {
        return false;
    }

    @Override
    public boolean usesSmallCards() {
        return true;
    }

    @Override
    public boolean mayShowExplanations() {
        return false;
    }

    @Override
    public void onAnswerClick(int player) {
        super.onAnswerClick(player);

        QuestionFragment questionFragment;
        if (player == 1) {
            questionFragment = getCurrentItem(mAdapter2, mViewPager2);
        } else {
            questionFragment = getCurrentItem(mAdapter, mViewPager);
        }
        questionFragment.blockAnswerButtons();
        questionFragment.showRightAnswer(false, getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond));
    }

    public void onRightAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
        super.onRightAnswer(questionId, timeLeft, timeLeftInMs, player);

        launchGrowingText(getString(R.string.quiz_fragment_question_right), getResources().getColor(R.color.holo_green_light), player);
        launchGrowingText(getString(R.string.quiz_fragment_question_toolate), getResources().getColor(R.color.holo_red_light), player == 1 ? 2 : 1);

        if (player == 1) {
            launchBounceView();
            getCurrentItem(mAdapter2, mViewPager2).setProgressBarColor(getResources().getColor(R.color.holo_red_light));
            mTotalPoints1++;
            mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(Math.abs(mTotalPoints1), 1), mTotalPoints1));
        } else {
            launchBounceView2();
            getCurrentItem(mAdapter, mViewPager).setProgressBarColor(getResources().getColor(R.color.holo_red_light));
            mTotalPoints2++;
            mTopRightTextView2.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(Math.abs(mTotalPoints2), 1), mTotalPoints2));
        }
    }

    public void onWrongAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
        super.onWrongAnswer(questionId, timeLeft, timeLeftInMs, player);

        launchGrowingText(getString(R.string.quiz_fragment_question_false), getResources().getColor(R.color.holo_red_light), player);
        launchGrowingText("+1", getResources().getColor(R.color.holo_green_light), player == 1 ? 2 : 1);

        if (player == 1) {
            launchBounceView2();
            getCurrentItem(mAdapter2, mViewPager2).setProgressBarColor(getResources().getColor(R.color.holo_green_light));
            mTotalPoints2++;
            mTopRightTextView2.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(Math.abs(mTotalPoints2), 1), mTotalPoints2));
        } else {
            launchBounceView();
            getCurrentItem(mAdapter, mViewPager).setProgressBarColor(getResources().getColor(R.color.holo_green_light));
            mTotalPoints1++;
            mPointsTextView.setText(getResources().getQuantityString(R.plurals.quiz_activity_question_totalpoints, Math.max(Math.abs(mTotalPoints1), 1), mTotalPoints1));
        }
    }

    public void onNextClick() {
        super.onNextClick();
    }

    @Override
    public void goToNext() {
        if (mTotalPoints1 < 10 && mTotalPoints2 < 10) {
            getCurrentItem(mAdapter, mViewPager).cancelNextBounce();
            getCurrentItem(mAdapter2, mViewPager2).cancelNextBounce();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1, true);
        } else {
            finishGame();
        }
    }

    public void onNewQuestion(int questionNumber) {
        super.onNewQuestion(questionNumber);

        mQuestionIndexTextView.setText(getString(R.string.quiz_activity_question_questionnumber, questionNumber));
        mTopLeftTextView2.setText(getString(R.string.quiz_activity_question_questionnumber, questionNumber));
    }

    @Override
    protected Intent insertExtraToFinishIntent(Intent i) {
        i = super.insertExtraToFinishIntent(i);
        i.putExtra(FinishActivity.KEY_QUESTION_POINTS, mTotalPoints1);
        i.putExtra(FinishSplitScreenActivity.KEY_QUESTION_POINTS_2, mTotalPoints2);
        return i;
    }

    @Override
    public void finishGame() {
        Intent i = new Intent(this, FinishSplitScreenActivity.class);
        i = insertExtraToFinishIntent(i);
        startActivity(i);
        finish();
    }
}
