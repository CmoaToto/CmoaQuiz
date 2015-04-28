package fr.cmoatoto.quiz.lib.animations;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout.LayoutParams;

import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;

public class ButtonsBackgroundRunnable implements Runnable {

    private static final String TAG = ButtonsBackgroundRunnable.class.getName();

    private View buttonBackgroundView;
    private int goalViewMarginTop = -1;
    private int goalViewHeight = -1;
    private int marginDiffBetweenTwoViews = -1;
    private int heightDiffBetweenTwoViews = -1;
    private int currentHeight;
    private int currentTopMargin;

    private ValueAnimator buttonBackgroundAnimation;

    private boolean shown = false;

    /**
     * Create the runnable used to show / move the background of the answers buttons.
     *
     * @param buttonBackgroundView
     *            the view of the button Background
     */
    public ButtonsBackgroundRunnable(View buttonBackgroundView) {
        this.buttonBackgroundView = buttonBackgroundView;
    }

    public void setGoalView(View goalView) {
        this.goalViewMarginTop = goalView.getTop();
        this.goalViewHeight = goalView.getHeight();
    }

    @Override
    public void run() {

        // We check if the goal view is set
        if (goalViewMarginTop == -1 || goalViewHeight == -1) {
            return;
        }

        if (!SharedPrefUtils.getAcceptAllAnimations(buttonBackgroundView.getContext())) {
            buttonBackgroundView.setVisibility(View.VISIBLE);

            LayoutParams params = (LayoutParams) buttonBackgroundView.getLayoutParams();
            params.topMargin = goalViewMarginTop;
            params.height = goalViewHeight;
            buttonBackgroundView.setLayoutParams(params);

            return;
        }

        // init the buttons background values just in case
        currentTopMargin = buttonBackgroundView.getTop();
        currentHeight = buttonBackgroundView.getHeight();

        // Cancel any running animation and define the values for the animation
        if (buttonBackgroundAnimation != null && buttonBackgroundAnimation.isRunning()) {
            buttonBackgroundAnimation.cancel();
        }
        buttonBackgroundAnimation = ValueAnimator.ofFloat(0, 1);

        // Define the parameters of the animation
        buttonBackgroundAnimation.setDuration(500);
        buttonBackgroundAnimation.setInterpolator(new BounceInterpolator());

        // If it is the first time we show the view (the first time we click on an answer)
        if (!shown) {
            // we set the height from 0 to the button's view height
            // we set the position of the view as the middle of the two views are equal
            buttonBackgroundAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                float value;
                LayoutParams params;

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    value = (Float) animator.getAnimatedValue();
                    params = (LayoutParams) buttonBackgroundView.getLayoutParams();

                    // During the animation :
                    // we change the top margins of the view to move to the new selected button
                    currentTopMargin = (int) (goalViewMarginTop + ((goalViewHeight / 2) * (1 - value)));
                    // we change the height the view to create an "opening"
                    currentHeight = (int) (goalViewHeight * value);

                    params.topMargin = currentTopMargin;
                    params.height = currentHeight;
                    buttonBackgroundView.setLayoutParams(params);
                }
            });
            buttonBackgroundAnimation.addListener(new Animator.AnimatorListener() {

                private boolean canceled = false;

                @Override
                public void onAnimationStart(Animator animation) {
                    // The view need to be VISIBLE
                    buttonBackgroundView.setVisibility(View.VISIBLE);
                    LayoutParams params = (LayoutParams) buttonBackgroundView.getLayoutParams();
                    currentTopMargin = goalViewMarginTop + (goalViewHeight / 2);
                    currentHeight = 0;
                    params.topMargin = currentTopMargin;
                    params.height = currentHeight;
                    buttonBackgroundView.setLayoutParams(params);
                    shown = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!canceled) {
                        LayoutParams params = (LayoutParams) buttonBackgroundView.getLayoutParams();
                        params.topMargin = goalViewMarginTop;
                        params.height = goalViewHeight;
                        buttonBackgroundView.setLayoutParams(params);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    canceled = true;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

        } else {
            // we set the height from current height to the button's view height if different
            // we set the position of the view equal as the button's view
            buttonBackgroundAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                private int marginStart = buttonBackgroundView.getTop();
                private int heightStart = buttonBackgroundView.getHeight();
                float value;
                LayoutParams params;

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    value = (Float) animator.getAnimatedValue();
                    params = (LayoutParams) buttonBackgroundView.getLayoutParams();

                    // During the animation :
                    // we change the top margins of the view to create an "opening"
                    if (marginDiffBetweenTwoViews != 0) {
                        currentTopMargin = (int) (marginStart + (marginDiffBetweenTwoViews * value));
                    }
                    // we change the height the view to create an "opening"
                    if (heightDiffBetweenTwoViews != 0) {
                        currentHeight = (int) (heightStart + (heightDiffBetweenTwoViews * value));
                    }

                    params.topMargin = currentTopMargin;
                    params.height = currentHeight;
                    buttonBackgroundView.setLayoutParams(params);
                }
            });
            buttonBackgroundAnimation.addListener(new Animator.AnimatorListener() {

                private boolean canceled = false;

                @Override
                public void onAnimationStart(Animator animation) {
                    marginDiffBetweenTwoViews = goalViewMarginTop - currentTopMargin;
                    heightDiffBetweenTwoViews = goalViewHeight - currentHeight;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!canceled) {
                        LayoutParams params = (LayoutParams) buttonBackgroundView.getLayoutParams();
                        params.topMargin = goalViewMarginTop;
                        params.height = goalViewHeight;
                        buttonBackgroundView.setLayoutParams(params);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    canceled = true;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }

        buttonBackgroundAnimation.start();
    }

}
