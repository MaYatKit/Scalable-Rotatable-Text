package com.example.sticker.utils;

import android.content.Context;
import android.util.DisplayMetrics;


public class DensityUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }


    public static boolean isHighDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi > 320;
    }

}
