package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class RotatingRelativeLayout extends RelativeLayout {

    private static final String TAG = RotatingRelativeLayout.class.getName();
    private int mRotation;

    public RotatingRelativeLayout(Context context) {
        super(context);
    }

    public RotatingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotatingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
