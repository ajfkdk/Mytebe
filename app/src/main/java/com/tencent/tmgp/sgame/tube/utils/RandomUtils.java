package com.tencent.tmgp.sgame.tube.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;

import java.io.FileOutputStream;
import java.util.Random;

public class RandomUtils {
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //len = 26+26+10 = 62
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        sb.append(str.charAt(random.nextInt(52)));
        for(int i=1;i<length;i++){
            int number=random.nextInt(62); //random_max = 62 不包含62
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomPkgName(){
        Random random=new Random();
        int len = random.nextInt(3)+2;
        return  "com.tencent.tmgp.sgame."+getRandomString(len);
    }

    public static String getRandomAppName(){
        Random random=new Random();
        int len = random.nextInt(7)+2;
        return getRandomString(len);
    }

    public static String getRandomFloatName(){
        return getRandomString(2);
    }

    public static int getRandomPort(){
        return (int) (Math.random() * 100) + 1+ 2025;
    }

    public static void makeRandomIcon(String appName,String filename){
        Random random = new Random();
        //随机颜色
//        int color = Color.BLACK;
        int color = Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255));

        //背景画笔
        Paint paint_circle = new Paint();
        paint_circle.setColor(color);
        paint_circle.setStyle(Paint.Style.FILL);
        paint_circle.setAntiAlias(true);

        //文字画笔
        Paint paint_text = new Paint();
        paint_text.setColor(Color.WHITE);
        paint_text.setStyle(Paint.Style.FILL);
        paint_text.setTextAlign(Paint.Align.CENTER);
        paint_text.setAntiAlias(true);
        paint_text.setTextSize(20f);

        //绘制图片
        Bitmap bmp = Bitmap.createBitmap(80,80, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawCircle(40,40,40,paint_circle);
        canvas.drawText(appName,40,45,paint_text);

        //保存图片
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
