package fr.cmoatoto.quiz.lib.animations;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

public class GrowingViewRunnable implements Runnable {

    private static final String TAG = GrowingViewRunnable.class.getName();

    private View mGrowingView;
    private View mGrowingViewContainer;

    private int mTime;
    private float mRatio;

    private ScaleAnimation growingScaleViewAnimation;
    private AlphaAnimation growingAlphaViewAnimation;

    /**
     * Create the runnable used to launch the view.
     *
     * @param growingView
     *            the growing view
     * @param time
     */
    public GrowingViewRunnable(View growingViewContainer, View growingView, int time, float ratio) {
        this.mGrowingView = growingView;
        this.mGrowingViewContainer = growingViewContainer;
        this.mTime = time;
        this.mRatio = ratio;
    }

    @Override
    public void run() {

        if (growingScaleViewAnimation != null && !growingScaleViewAnimation.hasEnded()) {
            growingScaleViewAnimation.cancel();
        }

        if (growingAlphaViewAnimation != null && !growingAlphaViewAnimation.hasEnded()) {
            growingAlphaViewAnimation.cancel();
        }

        // Define the parameters of the animations
        growingScaleViewAnimation = new ScaleAnimation(1, mRatio, 1, mRatio, mGrowingView.getWidth() / 2, 0);
        growingAlphaViewAnimation = new AlphaAnimation(1, 0);
        growingScaleViewAnimation.setDuration(mTime);
        growingAlphaViewAnimation.setDuration(mTime);
        growingScaleViewAnimation.setInterpolator(new DecelerateInterpolator());
        growingAlphaViewAnimation.setInterpolator(new DecelerateInterpolator());

        growingScaleViewAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mGrowingViewContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGrowingViewContainer.setVisibility(View.INVISIBLE);
            }
        });

        mGrowingViewContainer.setVisibility(View.VISIBLE);
        mGrowingView.setVisibility(View.VISIBLE);
        mGrowingViewContainer.startAnimation(growingScaleViewAnimation);
        mGrowingView.startAnimation(growingAlphaViewAnimation);
    }

}
