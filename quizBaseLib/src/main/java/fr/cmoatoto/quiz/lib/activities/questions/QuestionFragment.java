package fr.cmoatoto.quiz.lib.activities.questions;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.activities.QuestionActivity;
import fr.cmoatoto.quiz.lib.animations.ButtonNextIncomeRunnable;
import fr.cmoatoto.quiz.lib.animations.ButtonsBackgroundRunnable;
import fr.cmoatoto.quiz.lib.animations.ShowExplanationRunnable;
import fr.cmoatoto.quiz.lib.animations.ViewBouncer;
import fr.cmoatoto.quiz.lib.elements.QuestionList.Question;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils.ColorFont;
import fr.cmoatoto.quiz.lib.views.AnswerButton;
import fr.cmoatoto.quiz.lib.views.ColoredProgressBar;
import fr.cmoatoto.quiz.lib.views.QuizButton;

public class QuestionFragment extends Fragment implements OnClickListener {

    private static final String TAG = QuestionActivity.class.getName();

    public static String KEY_QUESTION = "fr.cmoatoto.quiz.lib.activities.questions.QuestionFragment.KeyQuestion";
    public static String KEY_PLAYER = "fr.cmoatoto.quiz.lib.activities.questions.QuestionFragment.KeyPlayer";

    private QuestionActivity mActivity;

    private Question mQuestion;

    private int mPlayer = 0;

    private boolean mBlockAnswerButtons = false;

    private List<AnswerButton> mButtons = new ArrayList<>();
    private View mButtonsBackground;
    private QuizButton mNextButton;
    private ColoredProgressBar mProgressBar;
    private TextView mInfoTextView;
    private TextView mExplanationTextView;

    private CountDown mCountDownTask;
    private boolean mCanStartCountDown = false;
    private long mSavedCountDownEndTime = -1;

    private Handler mButtonHandler = new Handler();
    private ButtonsBackgroundRunnable mButtonRunnable;

    private Handler mNextButtonHandler = new Handler();
    private ButtonNextIncomeRunnable mNextButtonRunnable;

    private Handler mShowExplanationHandler = new Handler();
    private ShowExplanationRunnable mShowExplanationRunnable;

    private ViewBouncer mBounceInfoTextViewTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mQuestion = (Question) getArguments().getSerializable(KEY_QUESTION);
            mPlayer = getArguments().getInt(KEY_PLAYER);
        }

        if (mQuestion == null) {
            Log.e(TAG, "Bad Bundle Extras, Destroying...");
            onDestroy();
            return;
        }

        mActivity = (QuestionActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;

        if (mActivity.usesSmallCards()) {
            v = inflater.inflate(R.layout.fragment_question_small, null);
        } else {
            v = inflater.inflate(R.layout.fragment_question, null);
        }
        TypeFaceUtils.applyFontToHierarchyView(v);

        ((TextView) v.findViewById(R.id.fragment_question_textview_question)).setText(mQuestion.getQuestion());
        ((TextView) v.findViewById(R.id.fragment_question_textview_questionby)).setText(getString(R.string.quiz_fragment_question_questionby, mQuestion.getSubmitter()));

        mExplanationTextView = (TextView) v.findViewById(R.id.fragment_question_textview_explanation);
        if (mActivity.mayShowExplanations()) {
            mExplanationTextView.setText(mQuestion.getExplanation());
            mShowExplanationRunnable = new ShowExplanationRunnable(mExplanationTextView, (ScrollView) v.findViewById(R.id.fragment_question_scrollview));
        } else {
            mExplanationTextView.setVisibility(View.GONE);
        }

        mButtons.add((AnswerButton) v.findViewById(R.id.fragment_question_button_answer1));
        mButtons.add((AnswerButton) v.findViewById(R.id.fragment_question_button_answer2));
        mButtons.add((AnswerButton) v.findViewById(R.id.fragment_question_button_answer3));
        for (AnswerButton button : mButtons) {
            button.setOnClickListener(this);
        }

        mNextButton = (QuizButton) v.findViewById(R.id.fragment_question_button_next);
        mNextButton.setColor(getResources().getColor(R.color.holo_orange_light));
        mNextButton.setOnClickListener(this);

        mButtonsBackground = v.findViewById(R.id.fragment_question_view_buttonbackground);
        mButtonRunnable = new ButtonsBackgroundRunnable(mButtonsBackground);
        mNextButtonRunnable = new ButtonNextIncomeRunnable(mNextButton);

        mProgressBar = (ColoredProgressBar) v.findViewById(R.id.fragment_question_progressbar);
        mInfoTextView = (TextView) v.findViewById(R.id.fragment_question_textview_countdown);
        mBounceInfoTextViewTask = new ViewBouncer(mInfoTextView).setInfiniteBounce(false).setTime(200).setRatio(1.2f);

        fillButtons();

        if (mActivity.needCountDown()) {
            mCountDownTask = new CountDown();
        } else {
            mProgressBar.setProgress(mProgressBar.getMax());
            setProgressBarColor(Color.BLACK);
            mInfoTextView.setText("");
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCanStartCountDown) {
            startCountDown();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelNextBounce();
    }

    public void cancelNextBounce() {
        mNextButtonRunnable.stopBounce();
    }

    public void startCountDown() {
        if (mCountDownTask == null) {
            mCanStartCountDown = true;
        } else if (mCountDownTask.getStatus() == Status.PENDING) {
            mCountDownTask.execute();
        }
    }

    public void stopCountDown() {
        if (mCountDownTask != null) {
            mCountDownTask.cancel(true);
        }
    }

    private void fillButtons() {
        Random r = new Random();

        List<AnswerButton> tmpbuttons = new ArrayList<>();

        for (int i = 1; mButtons.size() > 0; i++) {
            AnswerButton button = mButtons.get(r.nextInt(mButtons.size()));
            button.setIndex(i);
            button.setText(mQuestion.getAnswerfromIndex(i));
            tmpbuttons.add(button);
            mButtons.remove(button);
        }
        mButtons.addAll(tmpbuttons);
    }

    private void checkAnswer(int timeLeft, int timeLeftInMs) {
        mNextButton.setClickable(true);

        if (isRightAnswer()) {

            if (mActivity.mayShowExplanation(mQuestion, true)) {
                mShowExplanationHandler.post(mShowExplanationRunnable);
            }

            setProgressBarColor(getResources().getColor(R.color.holo_green_light));
            mInfoTextView.setTextColor(getResources().getColor(R.color.holo_green_light));
            mNextButton.setColor(getResources().getColor(R.color.holo_green_light));
            mButtonsBackground.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
            mInfoTextView.setText(R.string.quiz_fragment_question_right);

            mActivity.onRightAnswer(mQuestion.getId(), timeLeft, timeLeftInMs, mPlayer);

            if (mActivity.needCountDown()) {
                getPointsFromCountDown(timeLeft, false);
            } else {
                showNextButton(getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond));
            }
        } else {
            setProgressBarColor(getResources().getColor(R.color.holo_red_light));
            mInfoTextView.setTextColor(getResources().getColor(R.color.holo_red_light));
            mNextButton.setColor(getResources().getColor(R.color.holo_red_light));
            mInfoTextView.setText(R.string.quiz_fragment_question_false);

            mActivity.onWrongAnswer(mQuestion.getId(), timeLeft, timeLeftInMs, mPlayer);

            if (getResources().getBoolean(R.bool.quiz_config_showanswerwhenfalse)) {
                showRightAnswer(false, getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond));
            } else {
                mButtonsBackground.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
            }

            if (mActivity.needCountDownWhenFalse()) {
                getPointsFromCountDown(timeLeft, true);
            } else {
                showNextButton(getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond));
            }
        }
    }

    void getPointsFromCountDown(int timeLeft, boolean pointsGoToOtherPlayer) {
        setProgressBarColor(pointsGoToOtherPlayer? getResources().getColor(R.color.holo_red_light) :getResources().getColor(R.color.holo_green_light));
        mInfoTextView.setTextColor(pointsGoToOtherPlayer? getResources().getColor(R.color.holo_red_light) : getResources().getColor(R.color.holo_green_light));
        mNextButton.setColor(pointsGoToOtherPlayer? getResources().getColor(R.color.holo_red_light) : getResources().getColor(R.color.holo_green_light));
        new GetPointsFromCountDownTask(timeLeft, pointsGoToOtherPlayer).execute();
    }

    public void blockAnswerButtons() {
        if (mBlockAnswerButtons) {
            return;
        }
        mBlockAnswerButtons  = true;
        for (Button button : mButtons) {
            button.setClickable(false);
            button.setPressed(false);
            TypeFaceUtils.applyColorToHierarchyView(button, ((AnswerButton) button).isSelectedAnswer() ? ColorFont.DARK : ColorFont.LIGHT);
        }
    }

    public void showRightAnswer(boolean showNextButton, int delay) {
        mButtonsBackground.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
        if (delay <= 0) {
            new Handler().post(new ShowRightAnswerRunnable());
        } else {
            new Handler().postDelayed(new ShowRightAnswerRunnable(), delay);
        }

        if (showNextButton) {
            showNextButton(delay);
        }
    }

    public void setProgressBarColor(int color) {
        mProgressBar.setColor(color);
    }

    private boolean isRightAnswer() {
        for (AnswerButton button : mButtons) {
            if (button.isSelectedAnswer() && mQuestion.isRightAnswer(button.getIndex())) {
                button.setSelectedAnswer(false);
                return true;
            }
            button.setSelectedAnswer(false);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof AnswerButton) {
            onAnswerClick(v);
        } else if (v.equals(mNextButton)) {
            onNextClick(v);
        }
    }

    private void onAnswerClick(View v) {
        if (mBlockAnswerButtons) {
            return;
        }
        ((AnswerButton) v).setSelectedAnswer(true);
        blockAnswerButtons();
        mActivity.onAnswerClick(mPlayer);
        mButtonsBackground.setBackgroundColor(Color.BLACK);

        mButtonHandler.removeCallbacks(mButtonRunnable);
        mButtonRunnable.setGoalView(v);
        mButtonHandler.post(mButtonRunnable);

        if (mActivity.needCountDown()) {
            mCountDownTask.cancel(true);
        } else {
            checkAnswer(-1, 0);
        }
    }

    private void onNextClick(View v) {
        v.setClickable(false);

        mActivity.onNextClick();

        mActivity.goToNext();
    }

    private void showNextButton(int delay) {
        if (delay <= 0) {
            mNextButtonHandler.post(mNextButtonRunnable);
        } else {
            mNextButtonHandler.postDelayed(mNextButtonRunnable, delay);
        }
    }

    private class CountDown extends AsyncTask<Void, Integer, Void> {

        long endTime;
        int timeLeft;
        float calculedTime;
        int totalTime = mActivity.getTotalTime();
        boolean otherPlayerAnswered = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mInfoTextView.setText(String.valueOf(totalTime));
            mProgressBar.setProgress(mProgressBar.getMax());
            mProgressBar.setMaxColor(getResources().getColor(R.color.holo_green_light));
            mProgressBar.setMidColor(getResources().getColor(R.color.holo_orange_light));
            mProgressBar.setMinColor(getResources().getColor(R.color.holo_red_light));
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (mSavedCountDownEndTime != -1) {
                    endTime = mSavedCountDownEndTime;
                    timeLeft = (int) (mSavedCountDownEndTime - SystemClock.uptimeMillis()) / 1000;
                    mSavedCountDownEndTime = -1;
                } else {
                    endTime = SystemClock.uptimeMillis() + ((totalTime + 1) * 1000);
                    timeLeft = totalTime;
                }

                mProgressBar.setMax((int) (endTime - SystemClock.uptimeMillis()));
                mProgressBar.setMidValue(mProgressBar.getMax() / 2);
                mProgressBar.setProgress(mProgressBar.getMax());

                while (!isCancelled() && endTime - SystemClock.uptimeMillis() > 0) {
                    publishProgress((int) (endTime - SystemClock.uptimeMillis()));
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                mProgressBar.setProgress(values[0]);
                calculedTime = Math.min(((endTime - SystemClock.uptimeMillis()) / 1000f) + 1, totalTime);
                mInfoTextView.setText(String.format("%.1f", calculedTime));
                mInfoTextView.setTextColor(mProgressBar.getCurrentColor());

                if (timeLeft > calculedTime) {
                    timeLeft -= 1;

                    if (SharedPrefUtils.getAcceptAllAnimations(mActivity)) {
                        mBounceInfoTextViewTask.bounce();
                    }

                    if (timeLeft + 1 == totalTime / 2) {
                        mActivity.onMidTime(mProgressBar.getCurrentColor());
                    }
                }
            } catch (IllegalStateException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            handleOnCancelled();
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled();
            handleOnCancelled();
        }

        private void handleOnCancelled() {
            if (getActivity().isFinishing()) {
                return;
            }

            final int timeLeft = Math.min(totalTime, Math.max((int) ((endTime - SystemClock.uptimeMillis()) / 1000) + 1, 0));
            final int timeLeftInMs = Math.min(totalTime * 1000, Math.max((int) (endTime - SystemClock.uptimeMillis()) + 1, 0));
            if (!otherPlayerAnswered) {
                checkAnswer(timeLeft, timeLeftInMs);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mNextButton.setColor(getResources().getColor(R.color.holo_red_light));
            mInfoTextView.setText(R.string.quiz_fragment_question_toolate);
            mActivity.onEndTime(mQuestion.getId());
            mActivity.goToNext();
        }
    }

    private class GetPointsFromCountDownTask extends AsyncTask<Void, Integer, Void> {

        int startProgress;
        long endTime;
        int timeLeft;
        boolean pointsGoToOtherPlayer;

        public GetPointsFromCountDownTask(int timeLeft, boolean pointsGoToOtherPlayer) {
            this.timeLeft = timeLeft;
            this.pointsGoToOtherPlayer = pointsGoToOtherPlayer;
            startProgress = mProgressBar.getProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                long startTime = SystemClock.uptimeMillis();
                endTime = startTime + getResources().getInteger(R.integer.quiz_config_timeafteranswerinmillisecond);

                long diffTime = endTime - startTime;

                while (!isCancelled() && endTime - SystemClock.uptimeMillis() > 0) {
                    publishProgress((int) (((endTime - SystemClock.uptimeMillis()) * startProgress) / diffTime));
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (getActivity() == null) {
                cancel(true);
            }

            mProgressBar.setProgress(values[0]);
            if (timeLeft > (int) ((endTime - SystemClock.uptimeMillis()) / 1000)) {
                timeLeft -= 1;
                mActivity.onAddPoint(1, pointsGoToOtherPlayer);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressBar.setProgress(0);
            showNextButton(0);
        }
    }

    private class ShowRightAnswerRunnable implements Runnable {

        @Override
        public void run() {

            if (mActivity.mayShowExplanation(mQuestion, true)) {
                mShowExplanationHandler.post(mShowExplanationRunnable);
            }

            mButtonsBackground.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
            mButtonHandler.removeCallbacks(mButtonRunnable);
            for (AnswerButton button : mButtons) {
                if (mQuestion.isRightAnswer(button.getIndex())) {
                    mButtonRunnable.setGoalView(button);
                    mButtonHandler.post(mButtonRunnable);
                    TypeFaceUtils.applyColorToHierarchyView(button, ColorFont.DARK);
                } else {
                    TypeFaceUtils.applyColorToHierarchyView(button, ColorFont.LIGHT);
                }
            }
        }
    }
}
