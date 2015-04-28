package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

public class OnClickPreference extends Preference {

    private OnClickPreferenceListener mListener;

    public OnClickPreference(Context context) {
        super(context);
    }

    public OnClickPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnClickPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnClickListener(OnClickPreferenceListener listener) {
        mListener = listener;
    }

    @Override
    protected void onClick() {
        super.onClick();
        if (mListener != null) {
            mListener.onClick();
        }
    }

    public static interface OnClickPreferenceListener {
        public void onClick();
    }
}
