package fr.cmoatoto.quiz.lib.utils;

import android.content.Context;
import android.util.TypedValue;

public class DimenUtils {
    
    public static int dpToPx(Context c, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static float spToPx(Context c, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources().getDisplayMetrics());
    }
}
