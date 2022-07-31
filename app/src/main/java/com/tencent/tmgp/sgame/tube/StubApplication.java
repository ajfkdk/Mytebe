package com.tencent.tmgp.sgame.tube;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;



public class StubApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (getResources() == null) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public static Context getContext() {
        return context;
    }

}
