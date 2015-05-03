package fr.cmoatoto.quiz.lib.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TypeFaceUtils {

    private static Typeface defaultFont;
    private static Typeface lightFont;
    private static Typeface boldFont;
    private static Typeface hiddenFont;
    private static int colorDark = Color.argb(250, 50, 50, 50);
    private static int colorLight = Color.argb(250, 200, 200, 200);

    public static enum ColorFont {
        LIGHT, DARK, NONE
    }

    private static Typeface getFont(Context c, int typeface) {
        if (defaultFont == null) {
            defaultFont = Typeface.createFromAsset(c.getAssets(), "fonts/comfortaaregular.ttf");
        }
        if (lightFont == null) {
            lightFont = Typeface.createFromAsset(c.getAssets(), "fonts/comfortaalight.ttf");
        }
        if (boldFont == null) {
            boldFont = Typeface.createFromAsset(c.getAssets(), "fonts/comfortaabold.ttf");
        }
        if (hiddenFont == null) {
            hiddenFont = Typeface.createFromAsset(c.getAssets(), "fonts/redacted-script-regular.ttf");
        }
        if (typeface == Typeface.BOLD_ITALIC) {
            return hiddenFont;
        } else if (typeface == Typeface.BOLD) {
            return boldFont;
        } else if (typeface == Typeface.ITALIC) {
            return lightFont;
        } else {
            return defaultFont;
        }
    }

    public static void applyFontToHierarchyView(View v) {

        applyColorToHierarchyView(v, ColorFont.NONE);

        if (v.isInEditMode()) {
            return;
        }

        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                applyFontToHierarchyView(((ViewGroup) v).getChildAt(i));
            }
        } else if (v instanceof TextView) {
            Typeface typeface = ((TextView) v).getTypeface();
            if (typeface != null && typeface.isBold() && typeface.isItalic()) {
                ((TextView) v).setTypeface(getFont(v.getContext(), Typeface.BOLD_ITALIC));
            } else if ((typeface != null) && typeface.isBold()) {
                ((TextView) v).setTypeface(getFont(v.getContext(), Typeface.BOLD));
            } else if ((typeface != null) && typeface.isItalic()) {
                ((TextView) v).setTypeface(getFont(v.getContext(), Typeface.ITALIC));
            } else {
                ((TextView) v).setTypeface(getFont(v.getContext(), Typeface.NORMAL));
            }
        } else if (v instanceof Button) {
            Typeface typeface = ((Button) v).getTypeface();
            if (typeface != null && typeface.isBold() && typeface.isItalic()) {
                ((Button) v).setTypeface(getFont(v.getContext(), Typeface.BOLD_ITALIC));
            } else if ((typeface != null) && typeface.isBold()) {
                ((Button) v).setTypeface(getFont(v.getContext(), Typeface.BOLD));
            } else if ((typeface != null) && typeface.isItalic()) {
                ((Button) v).setTypeface(getFont(v.getContext(), Typeface.ITALIC));
            } else {
                ((Button) v).setTypeface(getFont(v.getContext(), Typeface.NORMAL));
            }
        }
    }

    public static void applyColorToHierarchyView(View v, ColorFont color) {
        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                applyColorToHierarchyView(((ViewGroup) v).getChildAt(i), color);
            }
        } else if (v instanceof TextView) {
            if (color == ColorFont.LIGHT) {
                ((TextView) v).setTextColor(colorLight);
            } else {
                ((TextView) v).setTextColor(colorDark);
            }
        } else if (v instanceof Button) {
            if (color == ColorFont.LIGHT) {
                ((Button) v).setTextColor(colorLight);
            } else {
                ((Button) v).setTextColor(colorDark);
            }
        }
    }

}
