package fr.cmoatoto.quiz.lib.activities.main;

import java.util.Locale;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.TwoStatePreference;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.MainActivity;
import fr.cmoatoto.quiz.lib.utils.PreferenceFragment;
import fr.cmoatoto.quiz.lib.views.OnClickPreference;
import fr.cmoatoto.quiz.lib.views.OnClickPreference.OnClickPreferenceListener;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference pref = (CheckBoxPreference) findPreference(getString(R.string.quiz_settings_acceptnottranslated_key));
        pref = (CheckBoxPreference) findPreference(getString(R.string.quiz_settings_acceptallanimations_key));
        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ((MainActivity) getActivity()).bounceStartButton((Boolean) newValue);
                return true;
            }
        });

        enablePref(getString(R.string.quiz_settings_selectable_signout_key), false);
    }

    public void checkPrefs() {
        if (Locale.getDefault().equals(Locale.ENGLISH)) {
            forcePref(getString(R.string.quiz_settings_acceptnottranslated_key), true);
        }
    }

    public void forcePref(String key, boolean value) {
        Preference connectionPref = findPreference(key);
        if (connectionPref instanceof TwoStatePreference) {
            enablePref(key, false);
            ((TwoStatePreference) connectionPref).setChecked(value);
        }
    }

    public void enablePref(String key, boolean enable) {
        findPreference(key).setEnabled(enable);
    }

    public void setSummary(String key, String summary) {
        Preference connectionPref = findPreference(key);
        connectionPref.setSummary(summary);
    }

    public void setSummary(String key, String summaryOn, String summaryOff) {
        Preference connectionPref = findPreference(key);
        if (connectionPref instanceof TwoStatePreference) {
            ((TwoStatePreference) connectionPref).setSummaryOn(summaryOn);
            ((TwoStatePreference) connectionPref).setSummaryOff(summaryOff);
        }
    }

    public void setChecked(String key, boolean checked) {
        Preference connectionPref = findPreference(key);
        if (connectionPref instanceof TwoStatePreference) {
            ((TwoStatePreference) connectionPref).setChecked(checked);
        }
    }

    public void setSignoutListener(OnClickPreferenceListener listener) {
        if (getActivity() != null) {
            OnClickPreference connectionPref = (OnClickPreference) findPreference(getString(R.string.quiz_settings_selectable_signout_key));
            connectionPref.setOnClickListener(listener);
        }
    }

}