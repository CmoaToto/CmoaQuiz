package fr.cmoatoto.quiz.lib.elements;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;

/**
 * Loads an XML description of a list of Game rules.
 * <p>
 * The GameRulesList contains XML that looks like the following snippet:
 * </p>
 * 
 * <pre>
 * &lt;GameRulesList&gt;
 *     &lt;GameRules
 *       gamerulesId="@id/quiz_gamerules_RULESNAME_id"
 *       gamerulesTitle="@string/quiz_gamerules_RULESNAME_title"
 *       gamerulesExplanation="@string/quiz_gamerules_RULESNAME_explanation"
 *       ... />
 *     ...
 * &lt;/GameRulesList&gt;
 * </pre>
 */
public class GameRulesList {

    private static GameRulesList _gamerulesListInstance;
    public static GameRulesList getInstance(Context c) {
        if (_gamerulesListInstance == null) {
            if (SharedPrefUtils.getShowTutorial(c)) {
                _gamerulesListInstance = new GameRulesList(c, R.xml.gamerulestutorial);
            } else {
                _gamerulesListInstance = new GameRulesList(c, R.xml.gamerules);
            }
        }
        return _gamerulesListInstance;
    }

    public static void resetInstance() {
        _gamerulesListInstance = null;
    }

    private static final String TAG = GameRulesList.class.getName();

    private static final String TAG_GAMERULES_LIST = "GameRulesList";
    private static final String TAG_GAMERULES = "GameRules";

    private static final String TAG_GAMERULES_ID = "gamerulesId";
    private static final String TAG_GAMERULES_TITLE = "gamerulesTitle";
    private static final String TAG_GAMERULES_EXPLANATION = "gamerulesExplanation";
    private static final String TAG_GAMERULES_HIGHSCORE = "gamerulesHighScore";
    private static final String TAG_GAMERULES_PLAYERS = "gamerulesPlayers";
    private static final String TAG_GAMERULES_VALUE1 = "gamerulesValue1";

    private Context mContext;
    private int mParserId;

    private List<GameRules> mAllGameRules = new ArrayList<>();

    /**
     * Container for {@link GameRules}s in the {@link GameRulesList}.
     */
    public static class GameRules implements Serializable {

        private int id;
        private String title;
        private String explanation;
        private int highScoreId;
        private int playersId;
        private String value1;

        public GameRules(Resources res, XmlResourceParser parser) {

            id = getIntegerValue(res, parser, TAG_GAMERULES_ID);
            title = getStringValue(res, parser, TAG_GAMERULES_TITLE);
            explanation = getStringValue(res, parser, TAG_GAMERULES_EXPLANATION);
            highScoreId = getIntegerValue(res, parser, TAG_GAMERULES_HIGHSCORE);
            playersId = getIntegerValue(res, parser, TAG_GAMERULES_PLAYERS);
            value1 = getStringValue(res, parser, TAG_GAMERULES_VALUE1);
        }

        private int getIntegerValue(Resources res, XmlResourceParser parser, String attribute) {
            int id = parser.getAttributeResourceValue(null, attribute, -1);
            return id != -1 ? id : parser.getAttributeIntValue(null, attribute, -1);
        }

        private String getStringValue(Resources res, XmlResourceParser parser, String attribute) {
            int id = parser.getAttributeResourceValue(null, attribute, -1);
            return id != -1 ? res.getString(id) : parser.getAttributeValue(null, attribute);
        }

        public boolean isUsable() {
            return id != -1 && title != null && explanation != null;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getExplanation() {
            return explanation;
        }

        public int getHighScoreId() {
            return highScoreId;
        }

        public int getPlayersId() {
            return playersId;
        }

        public String getValue1() {
            return value1;
        }

    }

    private GameRulesList(Context c, int parserId) {
        this.mContext = c;
        this.mParserId = parserId;
    }

    public void initGameRulesList() {
        Resources res = mContext.getResources();
        XmlResourceParser parser = res.getXml(mParserId);
        GameRules gameRules;
        mAllGameRules.clear();

        try {
            int event;
            while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
                if (event == XmlResourceParser.START_TAG) {
                    String tag = parser.getName();
                    if (TAG_GAMERULES_LIST.equals(tag)) {
                        parseQuestionListAttributes(res, parser);
                    } else if (TAG_GAMERULES.equals(tag)) {
                        gameRules = createGameRulesFromXml(res, parser);
                        if (gameRules.isUsable()) {
                            mAllGameRules.add(gameRules);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Parse error:" + e);
            e.printStackTrace();
        }
    }

    private void parseQuestionListAttributes(Resources res, XmlResourceParser parser) {
    }

    private GameRules createGameRulesFromXml(Resources res, XmlResourceParser parser) {
        return new GameRules(res, parser);
    }

    public List<GameRules> getAllGameRules() {
        return mAllGameRules;
    }

    public GameRules getGameRuleById(int id) {
        for (GameRules gamerules : mAllGameRules) {
            if (gamerules.getId() == id) {
                return gamerules;
            }
        }
        return null;
    }
}
