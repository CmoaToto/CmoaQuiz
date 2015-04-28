package fr.cmoatoto.quiz.lib.activities.gamerules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.elements.GameRulesList.GameRules;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;
import fr.cmoatoto.quiz.lib.views.CardViewPagerIndicator;

public class GameRulesFragment extends Fragment {

    private static final String TAG = GameRulesFragment.class.getName();

    public static String KEY_GAMERULES = "fr.cmoatoto.quiz.lib.activities.gamerules.GameRulesFragment.KeyGameRules";
    public static String KEY_INDEX = "fr.cmoatoto.quiz.lib.activities.gamerules.GameRulesFragment.KeyIndex";
    public static String KEY_ELEMENTS = "fr.cmoatoto.quiz.lib.activities.gamerules.GameRulesFragment.KeyElements";

    private GameRules mGameRules;
    private int mIndex;
    private int mElements;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGameRules = (GameRules) getArguments().getSerializable(KEY_GAMERULES);
            mIndex = getArguments().getInt(KEY_INDEX, 0);
            mElements = getArguments().getInt(KEY_ELEMENTS, 1);
        }

        if (mGameRules == null) {
            Log.e(TAG, "Bad Bundle Extras, Destroying...");
            onDestroy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gamerules, null);
        TypeFaceUtils.applyFontToHierarchyView(v);

        TextView title = (TextView) v.findViewById(R.id.fragment_gamerules_textview_title);
        title.setText(mGameRules.getTitle());

        if (mGameRules.getPlayersId() == R.id.quiz_gamerules_players_solo_id) {
            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_person, 0, 0, 0);
        } else if (mGameRules.getPlayersId() == R.id.quiz_gamerules_players_duo_id) {
            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_duo, 0, 0, 0);
        } else if (mGameRules.getPlayersId() == R.id.quiz_gamerules_players_many_id) {
            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_group, 0, 0, 0);
        }

        ((CardViewPagerIndicator) v.findViewById(R.id.fragment_gamerules_indicator)).setIndexAndElements(mIndex, mElements);

        if (mGameRules.getId() == R.id.quiz_gamerules_marathon_id) {
            int wrongAnswerLeft = Integer.valueOf(mGameRules.getValue1());
            String secondText;
            if (wrongAnswerLeft == -1) {
                secondText = getString(R.string.quiz_activity_question_marathon_wronganswersleft_unlimited);
            } else {
                secondText = getResources().getQuantityString(R.plurals.quiz_activity_question_marathon_wronganswersleft,
                        Math.max(wrongAnswerLeft, 1), wrongAnswerLeft);
            }
            ((TextView) v.findViewById(R.id.fragment_gamerules_textview_explain)).setText(mGameRules.getExplanation() + "\n\n" + secondText);
        } else {
            ((TextView) v.findViewById(R.id.fragment_gamerules_textview_explain)).setText(mGameRules.getExplanation());
        }

        TextView highScoreView = (TextView) v.findViewById(R.id.fragment_gamerules_textview_highscore);
        int highscore = SharedPrefUtils.getHighScore(getActivity(), mGameRules.getHighScoreId());
        if (highscore >= 0) {
            String textHighScore = null;
            if (mGameRules.getId() == R.id.quiz_gamerules_10questions_id) {
                textHighScore = getString(R.string.quiz_activity_main_highscore2, highscore,
                        (10 * Integer.valueOf(mGameRules.getValue1())));
            } else if (mGameRules.getId() == R.id.quiz_gamerules_marathon_id) {
                textHighScore = getString(R.string.quiz_activity_main_highscore1, highscore);
            }

            highScoreView.setVisibility(View.VISIBLE);
            highScoreView.setText(textHighScore);
        } else {
            highScoreView.setVisibility(View.GONE);
        }

        return v;
    }
}
