package com.tencent.tmgp.sgame.tube.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.tmgp.sgame.tube.StubApplication;
import com.tencent.tmgp.sgame.tube.utils.AppUtils;
import com.tencent.tmgp.sgame.tube.utils.ShellUtils;

public class ConfigHelper {
    //持久化存储
    public static SharedPreferences shp;
    public static boolean root;
    public static boolean aim;
    public static String package_name;
    public static String card;
    public static String password;
    public static String device_id;
    public static String version;
    public static int tss;
    public static int fps;
    public static String mode;
    public static int port;

    public static void save() {
        shp.edit()
                .putBoolean("root",root)
                .putBoolean("aim",aim)
                .putString("package_name", package_name)
                .putString("card", card)
                .putString("password", password)
                .putString("device_id", device_id)
                .putString("version", version)
                .putString("mode",mode)
                .putInt("tss",tss)
                .putInt("fps",fps)
                .putInt("port",port)
                .apply();
    }

    static{
        shp = StubApplication.getContext().getSharedPreferences("config",Context.MODE_PRIVATE);
        root = shp.getBoolean("root",false);
        aim = shp.getBoolean("aim",false);
        card = shp.getString("card","");
        password = shp.getString("password","");
        package_name = shp.getString("package_name", StubApplication.getContext().getPackageName());
        version = shp.getString("version", AppUtils.getVersionName(StubApplication.getContext()));
        tss = shp.getInt("tss",0);
        device_id = shp.getString("device_id",AppUtils.getDeviceId());
        fps = shp.getInt("fps",50);
        mode = shp.getString("mode","file");
        port = shp.getInt("port",8889);
    }

    public static String getDeviceId(){
        String id = ShellUtils.execCommand("cat /sys/class/net/wlan0/address",true).successMsg;
        if(id.equals(""))
            id = "12:34:56:18:20:35";
        return id;
    }

    //常量
    public static final String old_pg_name = "com.tencent.tmgp.sgame.tube";
    public static final String url_home = "https://www.lanzoui.com/b00nzkref";
    public static final String url_tutorial = "https://cnpanda.lanzous.com/b01nokkla";
    public static final String crash_tip = "程序崩溃";

    public static final String url = "https://w.eydata.net/"; //提交URL

//    public static final String api_singlecode = "ba594dd7ad7c2382"; //单码登录Api
//    public static final String api_deadline = "d29977eb2e5f7b8e"; //获取到期时间Api
//    public static final String api_bulletin = "e51dfb2fb808e39e"; //获取程序公告Api
//    public static final String api_version = "38097a4ef2ebb773"; //检测是否是最新版本Api
//    public static final String api_unbind = "cf93ed79cb006b1e"; //解绑

    public static final String api_singlecode = "2ef6c1efa7e4466f"; //单码登录Api
    public static final String api_deadline = "7da0e7a8aa7d7371"; //获取到期时间Api
    public static final String api_bulletin = "84b6d5161788614a"; //获取程序公告Api
    public static final String api_version = "4035102c77cdcbbc"; //检测是否是最新版本Api
    public static final String api_unbind = "d4c071e497d1a5a7"; //解绑

    //变量
    public static String bulletin;
    public static String deadline;
    public static Boolean logged;
    public static int camp;


}
