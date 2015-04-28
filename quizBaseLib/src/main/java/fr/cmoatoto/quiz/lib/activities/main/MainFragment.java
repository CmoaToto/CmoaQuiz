package fr.cmoatoto.quiz.lib.activities.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

import java.util.ArrayList;
import java.util.List;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.MainActivity;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.activities.gamerules.GameRulesPagerAdapter;
import fr.cmoatoto.quiz.lib.activities.gamerules.marathon.QuestionMarathonActivity;
import fr.cmoatoto.quiz.lib.activities.gamerules.onetry.QuestionOneTryActivity;
import fr.cmoatoto.quiz.lib.activities.gamerules.splitscreen.QuestionSplitScreenActivity;
import fr.cmoatoto.quiz.lib.activities.gamerules.tenquestions.Question10QuestionsActivity;
import fr.cmoatoto.quiz.lib.animations.ViewBouncer;
import fr.cmoatoto.quiz.lib.elements.GameRulesList;
import fr.cmoatoto.quiz.lib.elements.GameRulesList.GameRules;
import fr.cmoatoto.quiz.lib.elements.QuestionList;
import fr.cmoatoto.quiz.lib.elements.QuestionList.Question;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;
import fr.cmoatoto.quiz.lib.views.QuizButton;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getName();

    private MainActivity mActivity;

    private QuizButton mStartButton;

    private View mMainView;

    private JazzyViewPager mGameRulesViewPager;
    private GameRulesPagerAdapter mGameRulesPagerAdapter;

    private ViewBouncer mBounceStartButtonTask;

    private static String DIALOG_TAG = "fr.cmoatoto.quiz.lib.activities.main.dialogtag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_main_main, null);

        TypeFaceUtils.applyFontToHierarchyView(mMainView);

        mMainView.findViewById(R.id.activity_main_button_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInClick(view);
            }
        });
        mMainView.findViewById(R.id.activity_main_button_achievements).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAchievementClick(view);
            }
        });
        mMainView.findViewById(R.id.activity_main_button_leaderboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLeaderboardClick(view);
            }
        });

        mGameRulesViewPager = (JazzyViewPager) mMainView.findViewById(R.id.activity_main_viewpager_gamerules);
        mGameRulesPagerAdapter = new GameRulesPagerAdapter(mGameRulesViewPager, mActivity.getSupportFragmentManager());
        mGameRulesViewPager.setAdapter(mGameRulesPagerAdapter);
        mGameRulesViewPager.setTransitionEffect(TransitionEffect.ZoomIn);
        mGameRulesViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SharedPrefUtils.setLastSelectedGameRule(mActivity, position);
            }
        });

        mStartButton = (QuizButton) mMainView.findViewById(R.id.activity_main_button_start);
        mStartButton.setColor(getResources().getColor(R.color.holo_orange_light));
        mBounceStartButtonTask = new ViewBouncer(mStartButton);

        initQuestionList();
        initGameRulesList();

        return mMainView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getNewQuestionList();

        if (SharedPrefUtils.getAcceptAllAnimations(mActivity)) {
            bounceStartButton(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bounceStartButton(false);
    }

    public void bounceStartButton(boolean bounce) {
        if (bounce) {
            mBounceStartButtonTask.bounce();
        } else {
            mBounceStartButtonTask.cancel();
        }
    }

    private void showGameRules() {
        List<GameRules> gameRules = GameRulesList.getInstance(mActivity).getAllGameRules();
        mGameRulesPagerAdapter.setGameRules(gameRules);
        getView().findViewById(R.id.activity_main_textview_swipetochoose).setVisibility(gameRules.size() <= 1 ? View.GONE: View.VISIBLE);

        mGameRulesViewPager.setCurrentItem(SharedPrefUtils.getLastSelectedGameRule(mActivity), false);
    }

    void onSignInClick(View v) {
        // start the sign-in flow
        mActivity.signInButtonClicked();
    }

    public void onStartClick(View v) {

        Intent i = null;
        GameRules gameRules = mGameRulesPagerAdapter.getGameRules(mGameRulesViewPager.getCurrentItem());
        if (gameRules.getId() == R.id.quiz_gamerules_10questions_id) {
            i = new Intent(mActivity, Question10QuestionsActivity.class);
            i.putExtra(QuestionActivity.KEY_QUESTION_LIST, (ArrayList<Question>) QuestionList.getInstance(mActivity).getNext10Questions());
        } else if (gameRules.getId() == R.id.quiz_gamerules_marathon_id) {
            i = new Intent(mActivity, QuestionMarathonActivity.class);
            i.putExtra(QuestionActivity.KEY_QUESTION_LIST, (ArrayList<Question>) QuestionList.getInstance(mActivity).getNextMarathon());
        } else if (gameRules.getId() == R.id.quiz_gamerules_splitscreen_id) {
            i = new Intent(mActivity, QuestionSplitScreenActivity.class);
            i.putExtra(QuestionActivity.KEY_QUESTION_LIST, (ArrayList<Question>) QuestionList.getInstance(mActivity).getNext10Questions());
        } else if (gameRules.getId() == R.id.quiz_gamerules_onetry_id) {
            if (mActivity.getPlayer() == null) {
                Toast.makeText(mActivity, mActivity.getString(R.string.quiz_activity_main_toastpleaseconnect), Toast.LENGTH_SHORT).show();
                return;
            }
            i = new Intent(mActivity, QuestionOneTryActivity.class);
            i.putExtra(QuestionOneTryActivity.KEY_PLAYER, mActivity.getPlayer());
            i.putExtra(QuestionActivity.KEY_QUESTION_LIST, (ArrayList<Question>) QuestionList.getInstance(mActivity).getAllQuestions());
        }
        if (i != null) {
            i.putExtra(QuestionActivity.KEY_GAMERULES, gameRules);
            startActivity(i);
        } else {
            initQuestionList();
        }
    }

    void onAchievementClick(View v) {
        mActivity.showAchievementsRequested();
    }

    void onLeaderboardClick(View v) {
        mActivity.showLeaderboardsRequested();
    }

    public void onSettingsClick(View v) {
        mActivity.onSettingsClick(v);
    }

    private void initQuestionList() {
        (new AsyncTask<Void, Void, Void>() {

            QuestionList questionList = QuestionList.getInstance(mActivity);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mStartButton.setText(R.string.quiz_activity_main_loading);
                mStartButton.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                questionList.initQuestionLists();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                mActivity.getSettingsFragment().setSummary(getString(R.string.quiz_settings_acceptnottranslated_key),
                        getString(R.string.quiz_settings_checkbox_acceptnottranslated_summaryon, questionList.getTotalNotTranslatedQuestion()),
                        getString(R.string.quiz_settings_checkbox_acceptnottranslated_summaryoff, questionList.getTotalNotTranslatedQuestion()));
                if (questionList.getTotalTranslatedQuestion() < 10) {
                    mActivity.getSettingsFragment().forcePref(getString(R.string.quiz_settings_acceptnottranslated_key), true);
                } else if (questionList.getTotalNotTranslatedQuestion() == 0) {
                    mActivity.getSettingsFragment().forcePref(getString(R.string.quiz_settings_acceptnottranslated_key), false);
                }
                getNewQuestionList();
            }
        }).execute();
    }

    private void getNewQuestionList() {
        // Next local Quizes
        (new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mStartButton.setText(R.string.quiz_activity_main_loading);
                mStartButton.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                QuestionList.getInstance(mActivity).prepareQuestions();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (QuestionList.getInstance(mActivity).getNextMarathon().size() > 0) {
                    mStartButton.setText(R.string.quiz_activity_main_start);
                } else {
                    mStartButton.setText(R.string.quiz_activity_main_errorloading);
                }
                mStartButton.setEnabled(true);
            }
        }).execute();
    }

    private void initGameRulesList() {
        (new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                GameRulesList.getInstance(mActivity).initGameRulesList();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                showGameRules();
            }
        }).execute();
    }

    /** Shows the "sign in" bar (explanation and button). */
    public void showSignInBar() {
        mMainView.findViewById(R.id.activity_main_button_signin).setVisibility(View.VISIBLE);
        mMainView.findViewById(R.id.activity_main_view_gameseparator).setVisibility(View.GONE);
        mMainView.findViewById(R.id.activity_main_button_achievements).setVisibility(View.GONE);
        mMainView.findViewById(R.id.activity_main_button_leaderboard).setVisibility(View.GONE);
    }

    /** Shows the "sign out" bar (explanation and button). */
    public void showSignOutBar() {
        mMainView.findViewById(R.id.activity_main_button_signin).setVisibility(View.GONE);
        mMainView.findViewById(R.id.activity_main_view_gameseparator).setVisibility(View.VISIBLE);
        mMainView.findViewById(R.id.activity_main_button_achievements).setVisibility(View.VISIBLE);
        mMainView.findViewById(R.id.activity_main_button_leaderboard).setVisibility(View.VISIBLE);
    }

}