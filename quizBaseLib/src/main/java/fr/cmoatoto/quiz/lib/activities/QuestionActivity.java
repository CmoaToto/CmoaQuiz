package fr.cmoatoto.quiz.lib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

import java.util.ArrayList;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.questions.QuestionFragment;
import fr.cmoatoto.quiz.lib.activities.questions.QuestionPagerAdapter;
import fr.cmoatoto.quiz.lib.animations.GrowingViewRunnable;
import fr.cmoatoto.quiz.lib.animations.ViewBouncer;
import fr.cmoatoto.quiz.lib.elements.GameRulesList.GameRules;
import fr.cmoatoto.quiz.lib.elements.QuestionList.Question;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;
import fr.cmoatoto.quiz.lib.utils.game.BaseActivity;
import fr.cmoatoto.quiz.lib.views.MyAutoScrollViewPager;

public abstract class QuestionActivity extends BaseActivity implements OnPageChangeListener {

    private static final String TAG = QuestionActivity.class.getName();

    public static final String KEY_QUESTION_LIST = "fr.cmoatoto.quiz.lib.activities.QuestionActivity.KeyQuestionList";
    public static final String KEY_GAMERULES = "fr.cmoatoto.quiz.lib.activities.QuestionActivity.KeyGameRules";

    private QuestionActivity mActivity = this;

    protected ArrayList<Question> mQuestionList;

    protected GameRules mGameRules;
    protected boolean mLost = false;

    protected MyAutoScrollViewPager mViewPager;
    protected TextView mQuestionIndexTextView;
    protected TextView mPointsTextView;

    protected FrameLayout mGrowingTextViewContainer;
    protected TextView mGrowingTextView;

    protected View mMainView;

    protected QuestionPagerAdapter mAdapter;

    protected ViewBouncer mBouncePointsTextViewTask;

    private Handler mGrowingTextHandler = new Handler();
    protected GrowingViewRunnable mGrowingTextRunnable;

    protected boolean mOnPause = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.show_from_right, R.anim.hide_to_left);

        mQuestionList = (ArrayList<Question>) getIntent().getExtras().getSerializable(KEY_QUESTION_LIST);
        mGameRules = (GameRules) getIntent().getExtras().getSerializable(KEY_GAMERULES);

        if (!checkExtras()) {
            Log.e(TAG, "Bad Intent Extras, Finishing...");
            finish();
            return;
        }

        setContentView(getContentViewId());

        initView(findViewById(R.id.activity_question_main));
    }

    boolean checkExtras() {
        return mQuestionList != null && mQuestionList.size() > 0 && mGameRules != null;
    }

    protected int getContentViewId() {
        return R.layout.activity_question;
    }

    protected void initView(View mainView) {

        mMainView = mainView;
        TypeFaceUtils.applyFontToHierarchyView(mMainView);

        mQuestionIndexTextView = (TextView) findViewById(R.id.activity_question_textview_index);
        mPointsTextView = (TextView) findViewById(R.id.activity_question_textview_points);
        mBouncePointsTextViewTask = new ViewBouncer(mPointsTextView).setInfiniteBounce(false)
                .setTime(getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond)).setRatio(2f);

        mGrowingTextView = (TextView) findViewById(R.id.activity_question_textview_growingtext);
        mGrowingTextViewContainer = (FrameLayout) findViewById(R.id.activity_question_framelayout_growingtextcontainer);
        mGrowingTextRunnable = new GrowingViewRunnable(mGrowingTextViewContainer, mGrowingTextView, 2000, 10);

        mViewPager = (MyAutoScrollViewPager) findViewById(R.id.activity_question_viewpager_question);
        mAdapter = getQuestionPagerAdapter(mViewPager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setTransitionEffect(TransitionEffect.Tablet);
        mViewPager.setSwypable(false);
        mViewPager.setScrollDuration(500);

        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mOnPause) {
            mOnPause = false;
            if (needCountDown()) {
                ((QuestionFragment) mAdapter.getItem(mViewPager.getCurrentItem())).startCountDown();
            }
        } else {
            onPageSelected(0);
        }
    }

    public void goToNext() {
        if (!mLost && mViewPager.getCurrentItem() + 1 < mQuestionList.size()) {
            getCurrentItem(mAdapter, mViewPager).cancelNextBounce();
            mBouncePointsTextViewTask.cancel();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        } else {
            finishGame();
        }
    }

    protected void finishGame() {
        finish();
    }

    protected Intent insertExtraToFinishIntent(Intent i) {
        i.putExtra(FinishActivity.KEY_GAMERULES, mGameRules);
        return i;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOnPause = true;
        mBouncePointsTextViewTask.cancel();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.show_from_left, R.anim.hide_to_right);

        if (needCountDown()) {
            getCurrentItem(mAdapter, mViewPager).stopCountDown();
        }
    }

    boolean isOnPause() {
        return mOnPause;
    }

    public GameRules getGameRules() {
        return mGameRules;
    }

    protected QuestionPagerAdapter getQuestionPagerAdapter(JazzyViewPager viewPager) {
        return new QuestionPagerAdapter(this, viewPager, getSupportFragmentManager(), mQuestionList);
    }

    protected void launchGrowingText(String text, int color) {
        mGrowingTextView.setText(text);
        mGrowingTextView.setTextColor(color);
        mGrowingTextHandler.removeCallbacks(mGrowingTextRunnable);
        mGrowingTextHandler.post(mGrowingTextRunnable);
    }

    protected void launchBounceView() {
        if (SharedPrefUtils.getAcceptAllAnimations(this)) {
            mBouncePointsTextViewTask.bounce();
        }
    }

    public boolean needCountDown() {
        return false;
    }

    public boolean needCountDownWhenFalse() {
        return false;
    }

    public boolean usesSmallCards() {
        return false;
    }

    public void onMidTime(int color) {
    }

    public void onEndTime(int questionId) {
    }

    public void onAnswerClick(int player) {
    }

    public void onRightAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
        SharedPrefUtils.addAnsweredQuestionId(this, questionId);
    }

    public void onWrongAnswer(int questionId, int timeLeft, int timeLeftInMs, int player) {
    }

    public boolean mayShowExplanations() {
        return true;
    }

    public boolean mayShowExplanation(Question question, boolean answered) {
        return mayShowExplanations() && !isOnPause() && question.getExplanation() != null
                && (answered || getResources().getBoolean(R.bool.quiz_config_showanswerwhenfalse));
    }

    public void onNextClick() {
    }

    public void onAddPoint(int point, boolean addPointToOtherPlayer) {
    }

    protected void onNewQuestion(int questionNumber) {
    }

    @Override
    public void onPageSelected(int position) {
        onNewQuestion(position + 1);

        if (!mOnPause && needCountDown()) {
            ((QuestionFragment) mAdapter.getItem(mViewPager.getCurrentItem())).startCountDown();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    protected QuestionFragment getCurrentItem(QuestionPagerAdapter adapter, JazzyViewPager viewPager) {
        return (QuestionFragment) adapter.getItem(viewPager.getCurrentItem());
    }

    public void onOtherPlayerAnswered(int timeLeft) {
    }

    public int getTotalTime() {
        return 0;
    }
}
