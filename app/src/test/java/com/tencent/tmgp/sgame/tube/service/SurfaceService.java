package com.tencent.tmgp.sgame.tube.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.tencent.tmgp.sgame.tube.view.DrawView;


public class SurfaceService extends Service {

    private WindowManager mWm;
    private WindowManager.LayoutParams mWmParam;
    private SurfaceView surfaceView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initSurfaceWindow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(surfaceView != null){
            mWm.removeView(surfaceView);
        }
        this.stopSelf();
        super.onDestroy();
    }

    //初始化悬浮视图
    private void initSurfaceWindow() {
        mWm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWmParam = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            mWmParam.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }else{
            mWmParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }


        mWmParam.format = PixelFormat.RGBA_8888;
        mWmParam.gravity = Gravity.START | Gravity.TOP;
        mWmParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS|
                WindowManager.LayoutParams.FLAG_SECURE
        ;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(dm);
        mWmParam.width = dm.widthPixels;
        mWmParam.height = dm.heightPixels;
        mWmParam.x = 0;
        mWmParam.y = 0;
    }

    private void showFloatingWindow() {
        surfaceView = new DrawView(this);
        surfaceView.setZOrderOnTop(true);
        surfaceView.setZOrderMediaOverlay(true);
        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mWm.addView(surfaceView, mWmParam);
    }
}
