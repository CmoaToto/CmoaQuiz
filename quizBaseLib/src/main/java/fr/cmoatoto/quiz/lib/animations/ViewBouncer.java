package fr.cmoatoto.quiz.lib.animations;

import android.os.Handler;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;

public class ViewBouncer {

    private static final String TAG = ViewBouncer.class.getName();

    private Handler bounceViewHandler = new Handler();
    private BounceViewRunnable bounceViewRunnable;

    private boolean mInfiniteBounce = true;
    private int mTime = 1000;
    private float mRatio = 1.1f;

    private View mView;

    public ViewBouncer(View v) {
        mView = v;
        bounceViewRunnable = new BounceViewRunnable();
    }

    public ViewBouncer setInfiniteBounce(boolean infiniteBounce) {
        this.mInfiniteBounce = infiniteBounce;
        return this;
    }

    public ViewBouncer setTime(int time) {
        this.mTime = time;
        return this;
    }

    public ViewBouncer setRatio(float ratio) {
        this.mRatio = ratio;
        return this;
    }

    public void bounce() {
        bounceViewHandler.removeCallbacks(bounceViewRunnable);
        bounceViewHandler.post(bounceViewRunnable);
    }

    public void cancel() {
        bounceViewHandler.removeCallbacks(bounceViewRunnable);
    }

    private class BounceViewRunnable implements Runnable {

        private ScaleAnimation bounceViewAnimation;

        @Override
        public void run() {

            if (mView != null && mView.getWidth() != 0 && mView.getHeight() != 0) {

                if (bounceViewAnimation != null && !bounceViewAnimation.hasEnded()) {
                    return;
                }

                bounceViewAnimation = new ScaleAnimation(1f, mRatio, 1f, mRatio, mView.getWidth() / 2, mView.getHeight() / 2);

                // Define the parameters of the animation
                bounceViewAnimation.setDuration(mTime);
                bounceViewAnimation.setInterpolator(new CycleInterpolator(0.5f));
                mView.setVisibility(View.VISIBLE);
                mView.startAnimation(bounceViewAnimation);
            }

            if (mInfiniteBounce) {
                bounceViewHandler.postDelayed(bounceViewRunnable, 2000);
            }
        }

    }

}
