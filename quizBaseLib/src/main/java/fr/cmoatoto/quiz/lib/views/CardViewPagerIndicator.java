package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.utils.DimenUtils;

public class CardViewPagerIndicator extends View {

    private int mIndex = 0;
    private int mElements = 1;

    private int mLineHeight;
    private int mIndicatorLeft;
    private int mIndicatorRight;

    private Paint mPaint;

    public CardViewPagerIndicator(Context context) {
        super(context);
        init(null);
    }

    public CardViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CardViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        mLineHeight = DimenUtils.dpToPx(getContext(), 1);

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.holo_blue_light));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mElements > 1 && oldw == 0 && w != 0) {
            mIndicatorLeft = mIndex * (getWidth() / mElements);
            mIndicatorRight = (mIndex + 1) * (getWidth() / mElements);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, getHeight() - mLineHeight, getWidth(), getHeight(), mPaint);
        if (mElements > 1) {
            canvas.drawRect(mIndicatorLeft, 0, mIndicatorRight, getHeight() - mLineHeight, mPaint);
        }
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setIndexAndElements(int index, int elements) {
        mIndex = index;
        mElements = elements;
        if (elements > 1) {
            mIndicatorLeft = mIndex * (getWidth() / mElements);
            mIndicatorRight = (mIndex + 1) * (getWidth() / mElements);
        }
    }

}
