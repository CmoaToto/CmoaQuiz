package fr.cmoatoto.quiz.lib.views;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

public class MyAutoScrollViewPager extends JazzyViewPager {

    private static final String TAG = MyAutoScrollViewPager.class.getName();

    private boolean mSwypable = true;

    private MyScroller mScroller = null;

    public MyAutoScrollViewPager(Context context) {
        super(context);
        postInitViewPager();
    }

    public MyAutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    /**
     * Override the Scroller instance with our own class so we can change the duration
     */
    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            mScroller = new MyScroller(getContext(), new AccelerateDecelerateInterpolator());
            scroller.set(this, mScroller);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't set special ScrollDuration");
            e.printStackTrace();
        }
    }

    /**
     * Set the duration of the scroll
     */
    public void setScrollDuration(int duration) {
        mScroller.setScrollDuration(duration);
    }

    private class MyScroller extends Scroller {

        private int mDuration = -1;

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @SuppressLint("NewApi")
        public MyScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        /**
         * Set the duration
         */
        public void setScrollDuration(int duration) {
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration != -1 ? mDuration : duration);
        }

    }

    public void setSwypable(boolean swypable) {
        this.mSwypable = swypable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return mSwypable && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return mSwypable && super.onInterceptTouchEvent(arg0);
    }

}
