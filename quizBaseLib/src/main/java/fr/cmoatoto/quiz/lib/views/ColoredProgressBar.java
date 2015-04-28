package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import fr.cmoatoto.quiz.lib.R;

public class ColoredProgressBar extends View {

    private static final String TAG = ColoredProgressBar.class.getName();

    private int mMax = 100;
    private int mMidValue = -1;
    private int mProgressValue = 0;

    private int mMaxColor = Color.WHITE;
    private int mMidColor = Color.WHITE;
    private int mMinColor = Color.WHITE;

    private Paint mBackgroundPaint;
    private Paint mProgressPaint;

    public ColoredProgressBar(Context context) {
        super(context);
        init();
    }

    public ColoredProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColoredProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(getContext().getResources().getColor(R.color.quiz_progressbar_background));
        mProgressPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);
        canvas.drawRect(0, 0, (getWidth() * mProgressValue) / mMax, getHeight(), mProgressPaint);
    }

    @Override
    public void postInvalidate() {
        calculateColor();
        super.postInvalidate();
    }

    private void calculateColor() {
        if (mMinColor == mMaxColor && (mMidValue == -1 || mMidColor == mMinColor)) {
            mProgressPaint.setColor(mMinColor);
        } else if (mMidValue == -1){
            int a, r, g, b;
            float ratio = ((float) mProgressValue) / ((float) mMax);
            a = (int) (((Color.alpha(mMaxColor) - Color.alpha(mMinColor)) * ratio) + Color.alpha(mMinColor));
            r = (int) (((Color.red(mMaxColor) - Color.red(mMinColor)) * ratio) + Color.red(mMinColor));
            g = (int) (((Color.green(mMaxColor) - Color.green(mMinColor)) * ratio) + Color.green(mMinColor));
            b = (int) (((Color.blue(mMaxColor) - Color.blue(mMinColor)) * ratio) + Color.blue(mMinColor));
            mProgressPaint.setColor(Color.argb(a, r, g, b));
        } else if (mMinColor == mMidColor && mProgressValue < mMidValue) {
            mProgressPaint.setColor(mMinColor);
        } else if (mMaxColor == mMidColor && mProgressValue >= mMidValue) {
            mProgressPaint.setColor(mMaxColor);
        } else if (mProgressValue < mMidValue) {
            int a, r, g, b;
            float ratio = ((float) mProgressValue) / ((float) mMidValue);
            a = (int) (((Color.alpha(mMidColor) - Color.alpha(mMinColor)) * ratio) + Color.alpha(mMinColor));
            r = (int) (((Color.red(mMidColor) - Color.red(mMinColor)) * ratio) + Color.red(mMinColor));
            g = (int) (((Color.green(mMidColor) - Color.green(mMinColor)) * ratio) + Color.green(mMinColor));
            b = (int) (((Color.blue(mMidColor) - Color.blue(mMinColor)) * ratio) + Color.blue(mMinColor));
            mProgressPaint.setColor(Color.argb(a, r, g, b));
        } else if (mProgressValue >= mMidValue) {
            int a, r, g, b;
            float ratio = ((float) (mProgressValue - mMidValue)) / ((float) (mMax - mMidValue));
            a = (int) (((Color.alpha(mMaxColor) - Color.alpha(mMidColor)) * ratio) + Color.alpha(mMidColor));
            r = (int) (((Color.red(mMaxColor) - Color.red(mMidColor)) * ratio) + Color.red(mMidColor));
            g = (int) (((Color.green(mMaxColor) - Color.green(mMidColor)) * ratio) + Color.green(mMidColor));
            b = (int) (((Color.blue(mMaxColor) - Color.blue(mMidColor)) * ratio) + Color.blue(mMidColor));
            mProgressPaint.setColor(Color.argb(a, r, g, b));
        } else {
            Log.e(TAG, "calculate color : wrong case. study me : [0, " + mProgressValue + ", " + mMax + "], [" + mMinColor + ", " + mMidColor + ", " + mMaxColor + "]");
        }
    }

    public void setColor(int color) {
        mMaxColor = color;
        mMinColor = color;
        mMidColor = color;
        postInvalidate();
    }

    public void setMax(int max) {
        this.mMax = max;
        postInvalidate();
    }

    public int getMax() {
        return mMax;
    }

    public void setMidValue(int mid) {
        this.mMidValue = mid;
        postInvalidate();
    }

    public void setMaxColor(int color) {
        this.mMaxColor = color;
        postInvalidate();
    }

    public void setMidColor(int color) {
        this.mMidColor = color;
        postInvalidate();
    }

    public void setMinColor(int color) {
        this.mMinColor = color;
        postInvalidate();
    }

    public void setProgress(int progress) {
        this.mProgressValue = progress;
        postInvalidate();
    }

    public int getProgress() {
        return mProgressValue;
    }

    public int getCurrentColor() {
        return mProgressPaint.getColor();
    }

}
