package fr.cmoatoto.quiz.lib.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

class ImageColorUtils {

    // R' = a*R + b*G + c*B + d*A + e;
    // G' = f*R + g*G + h*B + i*A + j;
    // B' = k*R + l*G + m*B + n*A + o;
    // A' = p*R + q*G + r*B + s*A + t;

    // WHITE : 1, 1, 1, 1
    // BLACK : 0, 0, 0, 1

    public static Bitmap convertWhiteBitmapToColor(Bitmap bitmap, int colorInt) {

        float R = ((float) Color.red(colorInt)) / 255 / 3,
              G = ((float) Color.green(colorInt)) / 255 / 3,
              B = ((float) Color.blue(colorInt)) / 255 / 3,
              A = ((float) Color.alpha(colorInt)) / 255;

        float[] matrix = { R, R, R, 0, 0,
                           G, G, G, 0, 0,
                           B, B, B, 0, 0,
                           0, 0, 0, A, 0 };

        return convertBitmap(bitmap, matrix);
    }

    public static Bitmap convertBitmapToWhite(Bitmap bitmap, float A) {

        float[] matrix = { 1, 1, 1, 0, 0,
                           1, 1, 1, 0, 0,
                           1, 1, 1, 0, 0,
                           0, 0, 0, A, 0 };

        return convertBitmap(bitmap, matrix);
    }

    private static Bitmap convertBitmap(Bitmap bitmap, float[] matrix) {

        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix(matrix);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(colorFilter);
        Bitmap outBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return outBitmap;
    }

}
