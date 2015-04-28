package fr.cmoatoto.quiz.lib.activities.gamerules.tenquestions;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.Player;

import java.util.ArrayList;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.FinishActivity;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.elements.QuestionList;
import fr.cmoatoto.quiz.lib.utils.game.AchievementUtils;
import fr.cmoatoto.quiz.lib.utils.game.LeaderBoardUtils;

public class Finish10QuestionsActivity extends FinishActivity {

    private static final String TAG = Finish10QuestionsActivity.class.getName();

    public static final String KEY_ACHIEVEMENT_10QUESTIONS_FORGOTTOPLAY = "fr.cmoatoto.quiz.lib.activities.FinishActivity.KeyAchievement10QuestionsForgottoplay";
    public static final String KEY_ACHIEVEMENT_10QUESTIONS_ALLGOOD = "fr.cmoatoto.quiz.lib.activities.FinishActivity.KeyAchievement10QuestionsAllgood";
    public static final String KEY_ACHIEVEMENT_10QUESTIONS_MASTERLOOSER = "fr.cmoatoto.quiz.lib.activities.FinishActivity.KeyAchievement10QuestionsMasterlooser";

    private Finish10QuestionsActivity mActivity = this;

    @Override
    protected void initView() {
        super.initView();

        String textScore = getString(R.string.quiz_activity_finish_score2, mPoints, (10 * Integer.valueOf(mGameRules.getValue1())));

        ((TextView) findViewById(R.id.activity_finish_textview_score)).setText(
                getResources().getQuantityString(R.plurals.quiz_activity_finish_totalpoints,
                        Math.max(mPoints, 1),
                        textScore));
    }

    @Override
    public void onRestartClick(View v) {
        Intent i = new Intent(this, Question10QuestionsActivity.class);
        i.putExtra(QuestionActivity.KEY_QUESTION_LIST, (ArrayList<QuestionList.Question>) QuestionList.getInstance(this).getNext10Questions());
        i.putExtra(QuestionActivity.KEY_GAMERULES, mGameRules);
        startActivity(i);
        finish();
    }

    @Override
    protected String getShareText() {
        return "" + mPoints + " / " + (10 * Integer.valueOf(mGameRules.getValue1()));
    }

    protected void showConnexionSucceeded(Player currentPlayer) {
        LeaderBoardUtils.push10QuestionsScore(this, mPoints);
        if (mPoints >= 150) {
            AchievementUtils.pushAchievement10QuestionsGreatPlayer(this);
        }
        if (getIntent().getBooleanExtra(KEY_ACHIEVEMENT_10QUESTIONS_FORGOTTOPLAY, false)) {
            AchievementUtils.pushAchievement10QuestionsForgotToPlay(this);
        }
        if (getIntent().getBooleanExtra(KEY_ACHIEVEMENT_10QUESTIONS_ALLGOOD, false)) {
            AchievementUtils.pushAchievement10QuestionsAllGood(this);
        }
        if (getIntent().getBooleanExtra(KEY_ACHIEVEMENT_10QUESTIONS_MASTERLOOSER, false)) {
            AchievementUtils.pushAchievement10QuestionsMasterLooser(this);
        }
    }
}