package com.tencent.tmgp.sgame.tube.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tencent.tmgp.sgame.tube.data.DataProvider;
import com.tencent.tmgp.sgame.tube.helper.IconHelper;


public class BackgroundService extends Service {
    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建服务
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启服务
        IconHelper.init();
        DataProvider.init(); //二进制复制
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //摧毁服务
    }
}
