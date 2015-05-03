package fr.cmoatoto.quiz.lib.backoffice.pojo;

import android.content.Context;
import android.provider.Settings;

public class AllQuestionGame {

    private long date;

    private int points;

    private long detailledPoints;

    private int answeredQuestion;

    private int totalQuestion;

    private String deviceId;

    private Player player;

    public AllQuestionGame() {
    }

    public AllQuestionGame(Context c, int points, long detailledPoints, int answeredQuestion, int totalQuestion, Player player) {
        this.date = System.currentTimeMillis();
        this.points = points;
        this.detailledPoints = detailledPoints;
        this.answeredQuestion = answeredQuestion;
        this.totalQuestion = totalQuestion;
        this.deviceId = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
        this.player = player;
    }

    public long getDate() {
        return date;
    }

    public int getPoints() {
        return points;
    }

    public long getDetailledPoints() {
        return detailledPoints;
    }

    public int getAnsweredQuestion() {
        return answeredQuestion;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Player getPlayer() {
        return player;
    }


    public boolean isUsable() {
        return !(date == 0 || points < 0 || detailledPoints < 0 || answeredQuestion < 1 ||
                totalQuestion < 1 || !player.isUsable());
    }
}
