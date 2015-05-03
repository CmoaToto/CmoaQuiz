package fr.cmoatoto.quiz.lib.backoffice;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.backoffice.pojo.AllQuestionGame;
import fr.cmoatoto.quiz.lib.network.NetworkManager;

public class BackOfficeHelper {

    private static final String TAG = BackOfficeHelper.class.getName();

    public static void sendAllQuestionGameAsync(final Context c, final AllQuestionGame allQuestionGame) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String jsonQuiz = new Gson().toJson(allQuestionGame);
                try {
                    Log.d(TAG, "json : " + jsonQuiz);
                    Log.d(TAG, "url : " + c.getString(R.string.quiz_config_backoffice_link));
                    String response = new NetworkManager().post(new URL(c.getString(R.string.quiz_config_backoffice_link)), jsonQuiz, NetworkManager.JSON);
                    Log.d(TAG, "receiving : " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void sendAllQuestionGameToGoogleFormAsync(final Context c, final AllQuestionGame allQuestionGame) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String url = "https://docs.google.com/forms/d/YourGoogleFormUrlKey/formResponse";

                    String form = "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getDate()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getPoints()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getDetailledPoints()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getAnsweredQuestion()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getTotalQuestion()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getDeviceId()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getPlayer().getIdGoogle()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getPlayer().getDisplayName()), "UTF-8") + "&" +
                            "entry.FillThisKey=" + URLEncoder.encode(String.valueOf(allQuestionGame.getPlayer().getImage()), "UTF-8");

                    Log.d(TAG, "form : " + form);
                    Log.d(TAG, "url : " + url);
                    String response = new NetworkManager().post(new URL(url), form, NetworkManager.FORM);
                    Log.d(TAG, "receiving : " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
