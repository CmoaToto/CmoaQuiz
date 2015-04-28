package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.Button;

import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.utils.DimenUtils;
import fr.cmoatoto.quiz.lib.utils.TypeFaceUtils;

public class QuizButton extends Button {

    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingLeftRight;

    private Paint mPaint;

    public QuizButton(Context context) {
        super(context);
        initQuizButton(context, null);
    }

    public QuizButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initQuizButton(context, attrs);
    }

    public QuizButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initQuizButton(context, attrs);
    }

    private void initQuizButton(Context context, AttributeSet attrs) {

        mPaddingTop = DimenUtils.dpToPx(getContext(), 1);
        mPaddingBottom = DimenUtils.dpToPx(getContext(), 1);
        mPaddingLeftRight = DimenUtils.dpToPx(getContext(), 1);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.QuizButton, 0, 0);
            mPaddingTop = a.getDimensionPixelSize(R.styleable.QuizButton_lineTop, mPaddingTop);
            mPaddingBottom = a.getDimensionPixelSize(R.styleable.QuizButton_lineBottom, mPaddingBottom);
        }

        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.semitransparent_button_default));

        setBackgroundResource(R.drawable.button_background_selector);

        TypeFaceUtils.applyFontToHierarchyView(this);
    }

    public void setDoubleText(String string, String string2) {
        SpannableString text = new SpannableString(string + "\n" + string2);
        text.setSpan(new RelativeSizeSpan(0.75f), string.length() + 1, text.length(), 0);
        setText(text, BufferType.SPANNABLE);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top + mPaddingTop, right, bottom + mPaddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mPaddingLeftRight, mPaddingTop, getWidth() - mPaddingLeftRight, getHeight() - mPaddingBottom, mPaint);
        super.onDraw(canvas);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        mPaint.setAlpha(250);
    }

}
