package fr.cmoatoto.quiz.lib.utils.game;

import fr.cmoatoto.quiz.lib.R;

public class LeaderBoardUtils {

    public static void pushOneTryScore(BaseActivity ga, int points) {
        if (ga.isSignedIn()) {
            ga.submitScore(ga.getString(R.string.google_game_leaderboardid_onetry_bestscore), points);
        }
    }

    public static void push10QuestionsScore(BaseActivity ga, int points) {
        if (ga.isSignedIn()) {
            ga.submitScore(ga.getString(R.string.google_game_leaderboardid_10questions_bestscore), points);
        }
    }
}
