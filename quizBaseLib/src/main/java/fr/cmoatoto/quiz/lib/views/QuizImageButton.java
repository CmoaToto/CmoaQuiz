package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;
import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.utils.DimenUtils;

public class QuizImageButton extends ImageButton {

    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingLeftRight;

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTopBottom;

    private Paint mPaint;

    public QuizImageButton(Context context) {
        super(context);
        initQuizButton(null);
    }

    public QuizImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initQuizButton(attrs);
    }

    public QuizImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initQuizButton(attrs);
    }

    private void initQuizButton(AttributeSet attrs) {

        setMinimumHeight((int) getContext().getResources().getDimension(R.dimen.standard_height));

        mPaddingTop = DimenUtils.dpToPx(getContext(), 1);
        mPaddingBottom = DimenUtils.dpToPx(getContext(), 1);
        mPaddingLeftRight = DimenUtils.dpToPx(getContext(), 10);

        mPaddingLeft = DimenUtils.dpToPx(getContext(), 1);
        mPaddingRight = DimenUtils.dpToPx(getContext(), 1);
        mPaddingTopBottom = DimenUtils.dpToPx(getContext(), 10);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.QuizButton, 0, 0);
            mPaddingLeft = a.getDimensionPixelSize(R.styleable.QuizButton_lineLeft, mPaddingLeft);
            mPaddingTop = a.getDimensionPixelSize(R.styleable.QuizButton_lineTop, mPaddingTop);
            mPaddingRight = a.getDimensionPixelSize(R.styleable.QuizButton_lineRight, mPaddingRight);
            mPaddingBottom = a.getDimensionPixelSize(R.styleable.QuizButton_lineBottom, mPaddingBottom);
        }

        mPaint = new Paint();

        mPaint.setARGB(50, 200, 200, 200);

        setBackgroundResource(R.drawable.button_background_selector);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left + mPaddingLeft, top + mPaddingTop, right + mPaddingRight, bottom + mPaddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mPaddingLeftRight, 0, getWidth() - mPaddingLeftRight, mPaddingTop, mPaint);
        canvas.drawRect(mPaddingLeftRight, getHeight() - mPaddingBottom, getWidth() - mPaddingLeftRight, getHeight(), mPaint);
        canvas.drawRect(0, mPaddingTopBottom, mPaddingLeft, getHeight() - mPaddingTopBottom, mPaint);
        canvas.drawRect(getWidth() - mPaddingRight, mPaddingTopBottom, getWidth(), getHeight() - mPaddingTopBottom, mPaint);
    }

}
