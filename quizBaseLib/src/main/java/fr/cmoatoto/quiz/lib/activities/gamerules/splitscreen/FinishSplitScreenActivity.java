package fr.cmoatoto.quiz.lib.activities.gamerules.splitscreen;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.FinishActivity;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.animations.ViewBouncer;
import fr.cmoatoto.quiz.lib.elements.QuestionList;
import fr.cmoatoto.quiz.lib.elements.QuestionList.Question;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;
import fr.cmoatoto.quiz.lib.views.QuizButton;
import fr.cmoatoto.quiz.lib.views.RotatingLinearLayout;

public class FinishSplitScreenActivity extends FinishActivity {

    private static final String TAG = FinishSplitScreenActivity.class.getName();

    public static final String KEY_QUESTION_POINTS_2 = "fr.cmoatoto.quiz.lib.activities.FinishSplitScreenActivity.KeyQuestionPoints2";

    private FinishSplitScreenActivity mActivity = this;

    private QuizButton mRestartButton2;

    private int mPoints2;

    private ViewBouncer mBounceRestartButtonTask2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPoints2 = getIntent().getIntExtra(KEY_QUESTION_POINTS_2, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isIntentExtrasBad() {
        return mGameRules == null || mPoints2 < 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_finish_splitscreen;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void initView() {

        mMainView = findViewById(R.id.activity_finish_linearlayout_main);
        TypeFaceUtils.applyFontToHierarchyView(mMainView);

        RotatingLinearLayout rotatingView = (RotatingLinearLayout) findViewById(R.id.activity_finish_rotatinglinearlayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            rotatingView.setRotation(180);
        } else {
            rotatingView.rotate(180);
        }

        mRestartButton = (QuizButton) findViewById(R.id.activity_finish_button_restart);
        mBounceRestartButtonTask = new ViewBouncer(mRestartButton);

        mRestartButton2 = (QuizButton) findViewById(R.id.activity_finish_button_restart_2);
        mBounceRestartButtonTask2 = new ViewBouncer(mRestartButton2);

        String textScore = getString(R.string.quiz_activity_finish_score1, mPoints);
        String textScore2 = getString(R.string.quiz_activity_finish_score1, mPoints2);

        ((TextView) findViewById(R.id.activity_finish_textview_score)).setText(textScore);
        ((TextView) findViewById(R.id.activity_finish_textview_score_2)).setText(textScore2);

        if (mPoints > mPoints2) {
            findViewById(R.id.activity_finish_view_coloredbar).setBackgroundColor(getResources().getColor(R.color.holo_green_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result)).setTextColor(getResources().getColor(R.color.holo_green_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result)).setText(R.string.quiz_activity_finish_winner);
            mRestartButton.setColor(getResources().getColor(R.color.holo_green_light));

            findViewById(R.id.activity_finish_view_coloredbar_2).setBackgroundColor(getResources().getColor(R.color.holo_red_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result_2)).setTextColor(getResources().getColor(R.color.holo_red_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result_2)).setText(R.string.quiz_activity_finish_looser);
            mRestartButton2.setColor(getResources().getColor(R.color.holo_red_light));
        } else if (mPoints < mPoints2) {
            findViewById(R.id.activity_finish_view_coloredbar_2).setBackgroundColor(getResources().getColor(R.color.holo_green_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result_2)).setTextColor(getResources().getColor(R.color.holo_green_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result_2)).setText(R.string.quiz_activity_finish_winner);
            mRestartButton2.setColor(getResources().getColor(R.color.holo_green_light));

            findViewById(R.id.activity_finish_view_coloredbar).setBackgroundColor(getResources().getColor(R.color.holo_red_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result)).setTextColor(getResources().getColor(R.color.holo_red_light));
            ((TextView) findViewById(R.id.activity_finish_textview_result)).setText(R.string.quiz_activity_finish_looser);
            mRestartButton.setColor(getResources().getColor(R.color.holo_red_light));
        } else { // mPoints1 == mPoints2
            int tieColor = getResources().getColor(R.color.holo_orange_light);

            findViewById(R.id.activity_finish_view_coloredbar).setBackgroundColor(tieColor);
            ((TextView) findViewById(R.id.activity_finish_textview_result)).setTextColor(tieColor);
            ((TextView) findViewById(R.id.activity_finish_textview_result)).setText(R.string.quiz_activity_finish_tie);
            mRestartButton.setColor(tieColor);

            findViewById(R.id.activity_finish_view_coloredbar_2).setBackgroundColor(tieColor);
            ((TextView) findViewById(R.id.activity_finish_textview_result_2)).setTextColor(tieColor);
            ((TextView) findViewById(R.id.activity_finish_textview_result_2)).setText(R.string.quiz_activity_finish_tie);
            mRestartButton2.setColor(tieColor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SharedPrefUtils.getAcceptAllAnimations(this)) {
            mBounceRestartButtonTask2.bounce();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBounceRestartButtonTask2.cancel();
    }

    public void onRestartClick(View v) {
        Intent i = new Intent(this, QuestionSplitScreenActivity.class);
        i.putExtra(QuestionActivity.KEY_QUESTION_LIST, (ArrayList<Question>) QuestionList.getInstance(this).getNext10Questions());
        i.putExtra(QuestionActivity.KEY_GAMERULES, mGameRules);
        startActivity(i);
        finish();
    }

    @Override
    protected void getNewQuestionListInit() {
        super.getNewQuestionListInit();
        mRestartButton2.setText(R.string.quiz_activity_finish_loading);
        mRestartButton2.setEnabled(false);
    }

    @Override
    protected void getNewQuestionListPostExecute(QuestionList questionList) {
        if (questionList.getNextMarathon().size() > 0) {
            if (mPoints > mPoints2) {
                mRestartButton.setText(R.string.quiz_activity_finish_confirmwinner);
                mRestartButton2.setText(R.string.quiz_activity_finish_takerevenge);
            } else if (mPoints < mPoints2) {
                mRestartButton2.setText(R.string.quiz_activity_finish_confirmwinner);
                mRestartButton.setText(R.string.quiz_activity_finish_takerevenge);
            } else { // mPoints1 == mPoints2
                mRestartButton.setText(R.string.quiz_activity_finish_beathim);
                mRestartButton2.setText(R.string.quiz_activity_finish_beathim);
            }
        } else {
            mRestartButton.setText(R.string.quiz_activity_finish_errorloading);
            mRestartButton2.setText(R.string.quiz_activity_finish_errorloading);
        }
        mRestartButton.setEnabled(true);
        mRestartButton2.setEnabled(true);
    }
}
