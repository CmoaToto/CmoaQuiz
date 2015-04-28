package fr.cmoatoto.quiz.lib.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.main.MainFragment;
import fr.cmoatoto.quiz.lib.activities.main.SettingsFragment;
import fr.cmoatoto.quiz.lib.backoffice.pojo.Player;
import fr.cmoatoto.quiz.lib.utils.game.AchievementUtils;
import fr.cmoatoto.quiz.lib.utils.game.BaseActivity;
import fr.cmoatoto.quiz.lib.views.OnClickPreference.OnClickPreferenceListener;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    public final int REQUEST_ACHIEVEMENTS = 42;
    public final int REQUEST_LEADERBOARD = 43;

    private MainActivity mActivity = this;

    private ViewPager mViewPager;

    private MainFragment mMainFragment;
    private SettingsFragment mSettingsFragment;

    private Player mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        mViewPager = (ViewPager) getLayoutInflater().inflate(R.layout.activity_main, null);

        initSettings();
        initMain();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mSettingsFragment;
                    case 1:
                        return mMainFragment;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        setContentView(mViewPager);
        mViewPager.setCurrentItem(1, false);
    }

    private void initMain() {
        mMainFragment = (MainFragment) Fragment.instantiate(this, MainFragment.class.getName());
    }

    private void initSettings() {
        mSettingsFragment = (SettingsFragment) Fragment.instantiate(this, SettingsFragment.class.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSettingsFragment.checkPrefs();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 1) {
            mViewPager.setCurrentItem(1, true);
        } else {
            super.onBackPressed();
        }
    }

    public void onSettingsClick(View v) {
        mViewPager.setCurrentItem(0, true);
    }

    public void onStartClick(View v) {
        mMainFragment.onStartClick(v);
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public SettingsFragment getSettingsFragment() {
        return mSettingsFragment;
    }

    /**
     * Called to notify us that sign in failed. Notice that a failure in sign in is not necessarily due to an error; it might be that the user never signed in,
     * so our attempt to automatically sign in fails because the user has not gone through the authorization flow. So our reaction to sign in failure is to show
     * the sign in button. When the user clicks that button, the sign in process will start/resume.
     */
    @Override
    protected void showConnexionFailed() {
        // Sign-in has failed. So show the user the sign-in button so they can click the "Sign-in" button.
        mMainFragment.showSignInBar();
        mSettingsFragment.enablePref(getString(R.string.quiz_settings_selectable_signout_key), false);
    }

    /**
     * Called to notify us that sign in succeeded. We react by loading the loot from the cloud and updating the UI to show a sign-out button.
     * @param currentPlayer
     */
    @Override
    public void showConnexionSucceeded(com.google.android.gms.games.Player currentPlayer) {
        // Sign-in worked!
        mMainFragment.showSignOutBar();
        AchievementUtils.pushAchievementSignIn(this);

        mPlayer = new Player(currentPlayer);
        mSettingsFragment.enablePref(getString(R.string.quiz_settings_selectable_signout_key), true);


        mSettingsFragment.setSignoutListener(new OnClickPreferenceListener() {

            @Override
            public void onClick() {
                signOutButtonClicked();
            }

        });
    }

    public void bounceStartButton(Boolean bounce) {
        mMainFragment.bounceStartButton(bounce);
    }

}
