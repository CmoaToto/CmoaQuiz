package fr.cmoatoto.quiz.lib.utils.game;

import fr.cmoatoto.quiz.lib.R;

public class AchievementUtils {

    public static void pushAchievementSignIn(BaseActivity ga) {
        pushAchievement(ga, ga.getString(R.string.google_game_achivementid_signedin));
    }

    public static void pushAchievement10QuestionsForgotToPlay(BaseActivity ga) {
        pushAchievement(ga, ga.getString(R.string.google_game_achivementid_10questions_forgottoplay));
    }

    public static void pushAchievement10QuestionsAllGood(BaseActivity ga) {
        pushAchievement(ga, ga.getString(R.string.google_game_achivementid_10questions_allgood));
    }

    public static void pushAchievement10QuestionsMasterLooser(BaseActivity ga) {
        pushAchievement(ga, ga.getString(R.string.google_game_achivementid_10questions_masterlooser));
    }

    public static void pushAchievement10QuestionsGreatPlayer(BaseActivity ga) {
        pushAchievement(ga, ga.getString(R.string.google_game_achivementid_10questions_greatplayer));
    }

    private static void pushAchievement(BaseActivity ga, String achievement) {
        if (ga.isSignedIn()) {
            ga.unlockAchievement(achievement);
        }
    }

}
