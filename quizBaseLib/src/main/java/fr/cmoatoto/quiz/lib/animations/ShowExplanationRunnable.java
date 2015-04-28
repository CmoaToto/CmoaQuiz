package fr.cmoatoto.quiz.lib.animations;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;

public class ShowExplanationRunnable implements Runnable {

    private static final String TAG = ShowExplanationRunnable.class.getName();

    private View mExplanationView;
    private ScrollView mScrollView;

    private ValueAnimator mShowExplanationAnimation;

    /**
     * Create the runnable used to show the explanation.
     *
     * @param explanationView
     *            the explanation view
     */
    public ShowExplanationRunnable(View explanationView, ScrollView scrollView) {
        this.mExplanationView = explanationView;
        this.mScrollView = scrollView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void run() {

        if (!SharedPrefUtils.getAcceptAllAnimations(mExplanationView.getContext())) {
            mExplanationView.setVisibility(View.VISIBLE);

            LayoutParams params = (LayoutParams) mExplanationView.getLayoutParams();
            params.weight = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mExplanationView.setAlpha(1);
            }
            mExplanationView.setLayoutParams(params);
            mScrollView.smoothScrollBy(0, mExplanationView.getHeight());

            return;
        }

        mShowExplanationAnimation = ValueAnimator.ofFloat(0, 1);

        // Define the parameters of the animation
        mShowExplanationAnimation.setDuration(200);
        mShowExplanationAnimation.setInterpolator(new DecelerateInterpolator());

        // we set the marginRight from view mWidth to 0
        mShowExplanationAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            float value;
            LayoutParams params;

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                value = (Float) animator.getAnimatedValue();
                params = (LayoutParams) mExplanationView.getLayoutParams();

                // During the animation :
                // we change the weight and the alpha of the view to show the explanation
                params.weight = value;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mExplanationView.setAlpha(value);
                }

                mExplanationView.setLayoutParams(params);
            }
        });
        mShowExplanationAnimation.addListener(new Animator.AnimatorListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationStart(Animator animation) {
                LayoutParams params = (LayoutParams) mExplanationView.getLayoutParams();
                params.weight = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mExplanationView.setAlpha(0);
                }
                mExplanationView.setLayoutParams(params);

                // The view need to be VISIBLE
                mExplanationView.setVisibility(View.VISIBLE);
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationEnd(Animator animation) {
                LayoutParams params = (LayoutParams) mExplanationView.getLayoutParams();
                params.weight = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mExplanationView.setAlpha(1);
                }
                mExplanationView.setLayoutParams(params);
                mScrollView.smoothScrollBy(0, mExplanationView.getHeight());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mShowExplanationAnimation.start();
    }

}
