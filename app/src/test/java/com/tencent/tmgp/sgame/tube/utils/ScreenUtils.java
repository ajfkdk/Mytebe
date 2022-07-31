package com.tencent.tmgp.sgame.tube.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.tencent.tmgp.sgame.tube.StubApplication;

public class ScreenUtils {
    private static DisplayMetrics displayMetrics;

    static {
        WindowManager windowManager = (WindowManager) (StubApplication.getContext().getSystemService(Context.WINDOW_SERVICE));
        displayMetrics = new DisplayMetrics();
        assert windowManager != null;
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    static Point getRealSize(){
        WindowManager wm = (WindowManager) (StubApplication.getContext().getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getRealSize(p);
        return p;
    }

//    public static int getScreenWidth() {
//        return displayMetrics.widthPixels;
//    }
//
//    public static int getScreenHeight() {
//        return displayMetrics.heightPixels;
//    }

    public static int getScreenWidth() {
        return getRealSize().x;
    }

    public static int getScreenHeight() {
        return getRealSize().y;
    }

    public static int dp2px(int dp) {
        return dp * displayMetrics.densityDpi / 160;
    }

    public static int dp2px(Context context, float dp) {
        return (int) (dp * displayMetrics.density + 0.5f);
    }

    public static float px2dp(int px) {
        return (px / displayMetrics.density + 0.5f);
    }

    public static int sp2px(float sp) {
        return (int) (sp * displayMetrics.scaledDensity + 0.5f);
    }
}
