package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class RotatingLinearLayout extends LinearLayout {

    private static final String TAG = RotatingLinearLayout.class.getName();
    private int mRotation;

    public RotatingLinearLayout(Context context) {
        super(context);
    }

    public RotatingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotatingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void rotate(int rotation) {
        mRotation = rotation;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mRotation != 0) {
            canvas.save();
            canvas.rotate(mRotation, this.getWidth() / 2, this.getHeight() / 2);
            super.dispatchDraw(canvas);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mRotation != 0) {
            ev.setLocation(getWidth() - ev.getX(), getHeight() - ev.getY());
        }
        return super.dispatchTouchEvent(ev);
    }

}
