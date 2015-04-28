package fr.cmoatoto.quiz.lib.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout.LayoutParams;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;

public class ButtonNextIncomeRunnable implements Runnable {

    private static final String TAG = ButtonNextIncomeRunnable.class.getName();

    private View mButtonNextView;

    private int mWidth = -1;

    private TranslateAnimation mButtonNextAnimation;

    private boolean mShown = false;

    private ViewBouncer mBounceNextButtonTask;

    /**
     * Create the runnable used to show the next button.
     *
     * @param buttonNextView the NextButton view
     */
    public ButtonNextIncomeRunnable(View buttonNextView) {
        this.mButtonNextView = buttonNextView;
        mBounceNextButtonTask = new ViewBouncer(mButtonNextView).setRatio(1.3f);
    }

    public void stopBounce() {
        mBounceNextButtonTask.cancel();
    }

    @Override
    public void run() {

        // Cancel any try to reshow the button
        if (mShown) {
            return;
        }

        if (!SharedPrefUtils.getAcceptAllAnimations(mButtonNextView.getContext())) {
            mButtonNextView.setVisibility(View.VISIBLE);

            LayoutParams params = (LayoutParams) mButtonNextView.getLayoutParams();
            params.rightMargin = 0;
            mButtonNextView.setLayoutParams(params);

            return;
        }

        mShown = true;

        // init the button mWidth
        mWidth = mButtonNextView.getWidth();

        mButtonNextAnimation = new TranslateAnimation(-mWidth, 0, 0, 0);

        // Define the parameters of the animation
        mButtonNextAnimation.setDuration(200);
        mButtonNextAnimation.setInterpolator(new DecelerateInterpolator());

        mButtonNextAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // The view need to be VISIBLE
                mButtonNextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBounceNextButtonTask.bounce();
            }
        });

        mButtonNextView.startAnimation(mButtonNextAnimation);
    }

}
