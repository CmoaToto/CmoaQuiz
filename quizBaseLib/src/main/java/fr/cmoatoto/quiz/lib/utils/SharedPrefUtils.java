package fr.cmoatoto.quiz.lib.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.cmoatoto.quiz.lib.R;

public class SharedPrefUtils {

    /** Special separator to create String from List */
    private static final String SEPARATOR = "&sprt;";

    /** The list of question Ids allready answered (right) by the user */
    private static final String ANSWERED_QUESTION_IDS = "answered_question_ids";

    /** The last selected gamerule (index in the viewpager) */
    private static final String LAST_SELECTED_GAMERULE_INDEX = "last_selected_gamerule_index";

    /** Should show or not the Tutorial (loads a different gamerule xml) */
    private static final String SHOW_TUTORIAL_INDEX = "show_tutorial_index";

    /** Stock String preferences */
    private static void setPreference(Context c, String name, String value) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putString(name, value).commit();
    }

    /** Stock Boolean preferences */
    private static void setPreference(Context c, String name, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putBoolean(name, value).commit();
    }

    /** Stock Integer preferences */
    private static void setPreference(Context c, String name, int value) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().putInt(name, value).commit();
    }

    /** Stock Set preferences (in String format to avoid compatibility problems) */
    private static void setStringSetPreference(Context c, String name, Set<String> values) {
        setPreference(c, name, setToString(values));
    }

    /** Return pref value as string, if not found, will return default */
    private static String getStringPreference(Context c, String name, String defaut) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(name, defaut);
    }

    /** Return pref value as boolean, if not found, will return default */
    private static boolean getBooleanPreference(Context c, String name, boolean defaut) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(name, defaut);
    }

    /** Return pref value as int, if not found, will return default */
    private static int getIntPreference(Context c, String name, int defaut) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(c).getInt(name, defaut);
        } catch (Throwable t) {
            return defaut;
        }
    }

    /** Return pref value as set of string, if not found, will return default */
    private static Set<String> getSetPreference(Context c, String name, Set<String> defaut) {
        return stringToSet(PreferenceManager.getDefaultSharedPreferences(c).getString(name, setToString(defaut)));
    }

    /** Clear prefs for the specified key */
    private static void removePreference(Context c, String key) {
        PreferenceManager.getDefaultSharedPreferences(c).edit().remove(key).commit();
    }

    /**
     * @param values
     *            a set of values
     * @return a String with all the values separated by the value of SEPARATOR
     */
    private static String setToString(Set<String> values) {
        String setValues = "";
        for (String value : values) {
            setValues += value + SEPARATOR;
        }
        if (values != null && !values.isEmpty()) {
            return setValues.substring(0, setValues.length() - SEPARATOR.length());
        }
        return setValues;
    }

    /**
     * @param values
     *            a String with all the values separated by the value of SEPARATOR
     * @return a set of values
     */
    private static Set<String> stringToSet(String values) {
        Set<String> setValues = new HashSet<>();
        for (String value : Arrays.asList(values.split(SEPARATOR))) {
            setValues.add(value);
        }
        return setValues;
    }

    // And now the public getters/setters

    // List of question Ids

    /** Get the list of question Ids allready answered (right) by the user */
    public static List<Integer> getAnsweredQuestionIdList(Context c) {
        List<Integer> answeredQuestionIdList = new ArrayList<>();
        Set<String> answeredQuestionIdSet = getSetPreference(c, ANSWERED_QUESTION_IDS, new HashSet<String>());
        for (String id : answeredQuestionIdSet) {
            try {
                if (!id.equals(""))
                    answeredQuestionIdList.add(Integer.valueOf(id));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return answeredQuestionIdList;
    }

    /** Add an id to the list of question Ids allready answered (right) by the user */
    public static void addAnsweredQuestionId(Context c, int id) {
        Set<String> answeredQuestionIdSet = getSetPreference(c, ANSWERED_QUESTION_IDS, new HashSet<String>());
        answeredQuestionIdSet.add(String.valueOf(id));
        setStringSetPreference(c, ANSWERED_QUESTION_IDS, answeredQuestionIdSet);
    }

    /** Clear the list of question Ids allready answered (right) by the user */
    public static void clearAnsweredQuestionIds(Context c) {
        removePreference(c, ANSWERED_QUESTION_IDS);
    }

    // High Score

    /** Get The High Score (-1 by default == never finished) */
    public static int getHighScore(Context c, int highScoreId) {
        return getIntPreference(c, String.valueOf(highScoreId), -1);
    }

    public static void setHighScore(Context c, int highScoreId, int score) {
        setPreference(c, String.valueOf(highScoreId), score);
    }

    // Accept translated questions

    public static boolean getAcceptNotTranslatedQuestion(Context c) {
        return getBooleanPreference(c, c.getString(R.string.quiz_settings_acceptnottranslated_key),
                c.getResources().getBoolean(R.bool.quiz_settings_acceptnottranslated_default));
    }

    public static void setAcceptOnlyTranslatedQuestion(Context c, boolean acceptNotTranslatedQuestions) {
        setPreference(c, c.getString(R.string.quiz_settings_acceptnottranslated_key), acceptNotTranslatedQuestions);
    }

    // Accept all animations

    public static boolean getAcceptAllAnimations(Context c) {
        return getBooleanPreference(c, c.getString(R.string.quiz_settings_acceptallanimations_key),
                c.getResources().getBoolean(R.bool.quiz_settings_acceptallanimations_default));
    }

    public static void setAcceptAllAnimations(Context c, boolean acceptAcceptAllAnimations) {
        setPreference(c, c.getString(R.string.quiz_settings_acceptallanimations_key), acceptAcceptAllAnimations);
    }

    // Last selected Gamrule (index of the viewpager)

    public static int getLastSelectedGameRule(Context c) {
        return getIntPreference(c, LAST_SELECTED_GAMERULE_INDEX, 0);
    }

    public static void setLastSelectedGameRule(Context c, int lastSelectedGameRule) {
        setPreference(c, LAST_SELECTED_GAMERULE_INDEX, lastSelectedGameRule);
    }

    // Should show or not the Tutorial (loads a different gamerule xml)

    public static boolean getShowTutorial(Context c) {
        return getBooleanPreference(c, SHOW_TUTORIAL_INDEX, c.getResources().getBoolean(R.bool.quiz_config_hastutorial));
    }

    public static void setShowTutorial(Context c, boolean showTutorial) {
        setPreference(c, SHOW_TUTORIAL_INDEX, showTutorial);
    }

}
