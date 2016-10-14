package com.reactiveapps.chatty.utils;


import android.app.Application;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.reactiveapps.chatty.App;

public class DisplayUtils {
    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) App.getInst().getApplicationContext().getSystemService(Application.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static float getDensity() {
        float density = getDisplayMetrics().density;
        return density;
    }

    public static int getScreenWidth() {
        int screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    public static int getScreenHeight() {
        int screenHeight = getDisplayMetrics().heightPixels;
        return screenHeight;
    }

    public static int dip2px(float dpValue) {
        final float scale = getDensity();
        return (int) (dpValue * scale + 0.5f);
    }
}
