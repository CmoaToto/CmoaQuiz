package fr.cmoatoto.quiz.lib.activities;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.animations.ViewBouncer;
import fr.cmoatoto.quiz.lib.elements.GameRulesList.GameRules;
import fr.cmoatoto.quiz.lib.elements.QuestionList;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;
import fr.cmoatoto.quiz.lib.utils.game.BaseActivity;
import fr.cmoatoto.quiz.lib.views.QuizButton;

public abstract class FinishActivity extends BaseActivity {

    private static final String TAG = FinishActivity.class.getName();

    public static final String KEY_QUESTION_POINTS = "fr.cmoatoto.quiz.lib.activities.FinishActivity.KeyQuestionPoints";
    public static final String KEY_GAMERULES = "fr.cmoatoto.quiz.lib.activities.FinishActivity.KeyGameRules";

    private FinishActivity mActivity = this;

    protected View mMainView;
    protected QuizButton mRestartButton;

    protected int mPoints;

    protected GameRules mGameRules;

    protected ViewBouncer mBounceRestartButtonTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.show_from_right, R.anim.hide_to_left);

        mPoints = getIntent().getIntExtra(KEY_QUESTION_POINTS, -1);
        mGameRules = (GameRules) getIntent().getExtras().getSerializable(KEY_GAMERULES);
        if (isIntentExtrasBad()) {
            Log.e(TAG, "Bad Intent Extras, Finishing...");
            finish();
            return;
        }

        setContentView(getLayoutId());

        initView();
    }

    protected boolean isIntentExtrasBad() {
        return mPoints == -1 || mGameRules == null;
    }

    protected int getLayoutId() {
        return R.layout.activity_finish;
    }

    protected void initView() {

        mMainView = findViewById(R.id.activity_finish_linearlayout_main);
        TypeFaceUtils.applyFontToHierarchyView(mMainView);

        mRestartButton = (QuizButton) findViewById(R.id.activity_finish_button_restart);
        mRestartButton.setColor(getResources().getColor(R.color.holo_orange_light));
        mBounceRestartButtonTask = new ViewBouncer(mRestartButton);

        int exHighscore = SharedPrefUtils.getHighScore(this, mGameRules.getHighScoreId());
        if (exHighscore < mPoints) {
            SharedPrefUtils.setHighScore(this, mGameRules.getHighScoreId(), mPoints);
        } else {
            findViewById(R.id.activity_finish_textview_newhighscore).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewQuestionList();

        if (SharedPrefUtils.getAcceptAllAnimations(this)) {
            mBounceRestartButtonTask.bounce();
        }
    }

    @Override
    public void onBackPressed() {
        onBackClick(null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBounceRestartButtonTask.cancel();
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.show_from_left, R.anim.hide_to_right);
        super.finish();
    }

    public void onBackClick(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void onRestartClick(View v) {
        finish();
    }

    public void onShareClick(View v) {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.quiz_activity_finish_sharesubject, getString(R.string.quiz_app_name)));
        String textScore = getShareText();
        String appLink = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
        i.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.quiz_activity_finish_sharetext, getResources().getQuantityString(R.plurals.quiz_activity_finish_totalpoints, Math.max(mPoints, 1), textScore), getString(R.string.quiz_app_name), appLink));
        startActivity(Intent.createChooser(i, getResources().getString(R.string.quiz_activity_finish_sharetitle)));
    }

    protected String getShareText() {
        return "";
    }

    private void getNewQuestionList() {
        (new AsyncTask<Void, Void, Void>() {

            QuestionList questionList = QuestionList.getInstance(mActivity);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getNewQuestionListInit();
            }

            @Override
            protected Void doInBackground(Void... params) {
                questionList.prepareQuestions();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                getNewQuestionListPostExecute(questionList);
            }
        }).execute();
    }

    protected void getNewQuestionListInit() {
        mRestartButton.setText(R.string.quiz_activity_finish_loading);
        mRestartButton.setEnabled(false);
    }

    protected void getNewQuestionListPostExecute(QuestionList questionList) {
        if (questionList.getNextMarathon().size() > 0) {
            mRestartButton.setText(R.string.quiz_activity_finish_restart);
        } else {
            mRestartButton.setText(R.string.quiz_activity_finish_errorloading);
        }
        mRestartButton.setEnabled(true);
    }
}
