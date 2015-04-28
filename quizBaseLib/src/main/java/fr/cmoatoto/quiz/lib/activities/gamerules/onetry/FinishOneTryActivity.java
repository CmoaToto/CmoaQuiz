package fr.cmoatoto.quiz.lib.activities.gamerules.onetry;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.FinishActivity;
import fr.cmoatoto.quiz.lib.elements.GameRulesList;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;

public class FinishOneTryActivity extends FinishActivity {

    private static final String TAG = FinishOneTryActivity.class.getName();

    public static final String KEY_QUESTION_POINTS_MAX = "fr.cmoatoto.quiz.lib.activities.FinishOneTryActivity.KeyQuestionPointsMax";

    private FinishOneTryActivity mActivity = this;

    private int mMaxPoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefUtils.setShowTutorial(this, false);
    }

    @Override
    protected void initView() {
        super.initView();

        mRestartButton.setVisibility(View.GONE);

        mMaxPoints = getIntent().getIntExtra(KEY_QUESTION_POINTS_MAX, -1);

        String textScore = getString(R.string.quiz_activity_finish_score2, mPoints, mMaxPoints);

        ((TextView) findViewById(R.id.activity_finish_textview_score)).setText(
                getResources().getQuantityString(R.plurals.quiz_activity_finish_totalpoints,
                        Math.max(mPoints, 1),
                        textScore));
    }

    @Override
    protected String getShareText() {
        return "" + mPoints + " / " + mMaxPoints;
    }

    public void onBackClick(View v) {
        GameRulesList.resetInstance();
        super.onBackClick(v);
    }
}
