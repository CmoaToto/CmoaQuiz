package fr.cmoatoto.quiz.lib.backoffice;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
                    String response = new NetworkManager().post(new URL(c.getString(R.string.quiz_config_backoffice_link)), jsonQuiz);
                    Log.d(TAG, "receiving : " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
