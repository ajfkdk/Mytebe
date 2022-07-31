package com.tencent.tmgp.sgame.tube.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.tencent.tmgp.sgame.tube.StubApplication;

public class DrawHelper {
    // 地图头像偏移
    public static int icon_offset_x;
    public static int icon_offset_y;

    //触摸坐标
    public static int touch_offset_x;
    public static int touch_offset_y;

    // 射线末端偏移
    public static int ray_offset_x;
    public static int ray_offset_y;

    // 是否绘制
    public static boolean draw_ray;
    public static boolean draw_rect;
    public static boolean draw_circle;
    public static boolean draw_cd;
    public static boolean draw_buff;
    public static boolean draw_head;
    public static boolean draw_aim=false;

    //绘制参数
    public static int rect_size;

    private static SharedPreferences sp;

    static {
        load();
    }

    public static void load() {
        sp = StubApplication.getContext().getSharedPreferences("draw", Context.MODE_PRIVATE);
        icon_offset_x = sp.getInt("icon_offset_x", 0);
        icon_offset_y = sp.getInt("icon_offset_y", 22);
        ray_offset_x = sp.getInt("ray_offset_x", 0);
        ray_offset_y = sp.getInt("ray_offset_y", 0);
        draw_rect = sp.getBoolean("draw_rect",true);
        draw_ray = sp.getBoolean("draw_ray",true);
        draw_buff = sp.getBoolean("draw_buff",true);
        draw_head = sp.getBoolean("draw_head",true);
        draw_circle = sp.getBoolean("draw_circle",false);
        draw_cd = sp.getBoolean("draw_cd",true);
        rect_size = sp.getInt("rect_size", 60);
    }

    public static void save(){
        sp.edit()
                .putInt("icon_offset_x", icon_offset_x)
                .putInt("icon_offset_y", icon_offset_y)
                .putInt("ray_offset_x",ray_offset_x)
                .putInt("ray_offset_y",ray_offset_y)
                .putBoolean("draw_ray",draw_ray)
                .putBoolean("draw_rect",draw_rect)
                .putBoolean("draw_buff",draw_buff)
                .putBoolean("draw_head",draw_head)
                .putBoolean("draw_circle",draw_circle)
                .putBoolean("draw_cd",draw_cd)
                .putInt("rect_size",rect_size)
                .apply();
    }

    public static RectF getRectByCenter(float center_x, float center_y, float width, float height){
        float left = center_x - width/2;
        float right = center_x + width/2;
        float top = center_y - height/2;
        float bottom = center_y + height/2;
        return new RectF(left,top,right,bottom);
    }

    public static void drawBitMapByCenter(Canvas canvas, Bitmap mBitmap, float center_x, float center_y, Paint paint){
        float icon_x = center_x - IconHelper.radius;
        float icon_y = center_y - IconHelper.radius;
        Paint paint_red = new Paint();
        paint_red.setColor(Color.RED);
        paint_red.setStyle(Paint.Style.FILL);
        canvas.drawCircle(center_x,center_y,IconHelper.radius+4,paint_red);
        canvas.drawBitmap(mBitmap, icon_x, icon_y, paint);

    }

    public static void drawConCircle(Canvas canvas,float center_x, float center_y,float r, Paint p_fill,Paint p_edge){
        canvas.drawCircle(center_x,center_y,r+2,p_edge);
        canvas.drawCircle(center_x,center_y,r,p_fill);
    }

//    public static void drawBloodBar(Canvas canvas,float cx,float cy,float percent,Paint p_fill,Paint p_edge){
//        int round = 5;
//        RectF rect_edge = getRectByCenter(cx,cy,57,10);
//        RectF rect_fill = getRectByCenter(cx,cy,54,7);
//        canvas.drawRoundRect(rect_edge,round,round,p_edge);
//        canvas.drawRoundRect(rect_fill,round,round,p_fill);
//    }
}
