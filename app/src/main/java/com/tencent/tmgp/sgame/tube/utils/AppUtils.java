package com.tencent.tmgp.sgame.tube.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;

public class AppUtils {

    public static void getRoot(){
        try {
            Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceId(){
        String id = "12:34:56:18:20:35";
////        try {
////            id = ShellUtils.execCommand("cat /sys/class/net/wlan0/address", true).successMsg;
////        }catch (Exception e){
////            id = "12:34:56:18:20:35";
////        }
////        if(id.equals(""))
//            id = "12:34:56:18:20:35";
        return id;
    }

    public static synchronized String getAppName(Context context) {

        try {

            PackageManager packageManager = context.getPackageManager();

            PackageInfo packageInfo = packageManager.getPackageInfo(

                    context.getPackageName(), 0);

            int labelRes = packageInfo.applicationInfo.labelRes;

            return context.getResources().getString(labelRes);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }



    /**

     * [获取应用程序版本名称信息]

     * @param context

     * @return 当前应用的版本名称

     */

    public static synchronized String getVersionName(Context context) {

        try {

            PackageManager packageManager = context.getPackageManager();

            PackageInfo packageInfo = packageManager.getPackageInfo(

                    context.getPackageName(), 0);

            return packageInfo.versionName;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }





    /**

     * [获取应用程序版本名称信息]

     * @param context

     * @return 当前应用的版本名称

     */

    public static synchronized int getVersionCode(Context context) {

        try {

            PackageManager packageManager = context.getPackageManager();

            PackageInfo packageInfo = packageManager.getPackageInfo(

                    context.getPackageName(), 0);

            return packageInfo.versionCode;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return 0;

    }





    /**

     * [获取应用程序版本名称信息]

     * @param context

     * @return 当前应用的版本名称

     */

    public static synchronized String getPackageName(Context context) {

        try {

            PackageManager packageManager = context.getPackageManager();

            PackageInfo packageInfo = packageManager.getPackageInfo(

                    context.getPackageName(), 0);

            return packageInfo.packageName;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }





    /**

     * 获取图标 bitmap

     * @param context

     */

    public static synchronized Bitmap getBitmap(Context context) {

        PackageManager packageManager = null;

        ApplicationInfo applicationInfo = null;

        try {

            packageManager = context.getApplicationContext()

                    .getPackageManager();

            applicationInfo = packageManager.getApplicationInfo(

                    context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {

            applicationInfo = null;

        }

        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable

        BitmapDrawable bd = (BitmapDrawable) d;

        Bitmap bm = bd.getBitmap();

        return bm;

    }

}
