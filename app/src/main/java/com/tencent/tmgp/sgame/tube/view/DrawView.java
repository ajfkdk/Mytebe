package com.tencent.tmgp.sgame.tube.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.tencent.tmgp.sgame.tube.data.DataProvider;
import com.tencent.tmgp.sgame.tube.data.DrawData;
import com.tencent.tmgp.sgame.tube.helper.ConfigHelper;
import com.tencent.tmgp.sgame.tube.helper.DrawDataHelper;
import com.tencent.tmgp.sgame.tube.helper.DrawHelper;
import com.tencent.tmgp.sgame.tube.helper.IconHelper;
import com.tencent.tmgp.sgame.tube.utils.ScreenUtils;

import java.util.List;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Thread drawThread;
    private boolean threadFlag;
    public static boolean controlFlag = true;


    public DrawView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        drawThread = new Thread(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        threadFlag = true;
        drawThread.start();//开启绘制线程
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        threadFlag = false;
        surfaceHolder.removeCallback(this);
    }

    @Override
    public void run() {
        float icon_x, icon_y;
        float ray_x, ray_y;
        float[] animalx = {(float) (0.00332 * 24171), (float) (0.00332 * -3286),
                (float) (0.00332 * -23613), (float) (0.00332 * 2682)};
        float[] animaly = {(float) ((float) (-0.00332 * -1253)), (float) ((float) (-0.00332 * 30179)),
                (float) ((float) (-0.00332 * 265)), (float) ((float) (-0.00332 * -29862))};


        Paint paint = new Paint();
        Paint paint_start = new Paint();
        Paint paint_red = new Paint();
        Paint paint_gray = new Paint();
        Paint paint_edge = new Paint();
        Paint paint_buffText = new Paint();
        Paint paint_buffRed = new Paint();
        Paint paint_buffBlue = new Paint();
        Paint paint_backhome = new Paint();
        Paint paint_blood = new Paint();
        Paint paint_ray = new Paint();
        Paint paint_rect = new Paint();
        Paint paint_ready = new Paint();
        Paint paint_hint = new Paint();
        Paint paint_cd = new Paint();
        Paint paint_cd_text = new Paint();
        Paint paint_cd_big = new Paint();
        Paint paint_deadline = new Paint();
        Paint paint_name = new Paint();
        Paint paint_bedge = new Paint();

        //等待对局开始文字
        paint_start.setColor(Color.WHITE);
        paint_start.setStyle(Paint.Style.FILL);
        paint_start.setTextSize(80f);
        paint_start.setTextAlign(Paint.Align.CENTER);

        //红色画笔--圆圈
        paint_red.setColor(Color.RED);
        paint_red.setStyle(Paint.Style.FILL);
        paint_red.setStrokeWidth(5f);


        //灰色画笔--血条框
        paint_gray.setColor(Color.GRAY);
        paint_gray.setStyle(Paint.Style.STROKE);
        paint_gray.setStrokeWidth(6f);

        //血条边缘画笔
        paint_bedge.setColor(Color.GRAY);
        paint_bedge.setStyle(Paint.Style.FILL);
        paint_bedge.setStrokeWidth(1f);

        //边缘画笔
        paint_edge.setColor(Color.WHITE);
        paint_edge.setStyle(Paint.Style.FILL);
        paint_edge.setStrokeWidth(2f);

        //red buff ready
        paint_buffRed.setColor(Color.RED);
        paint_buffRed.setStyle(Paint.Style.FILL);
        paint_buffRed.setStrokeWidth(5f);

        //blue buff ready
        paint_buffBlue.setColor(Color.BLUE);
        paint_buffBlue.setStyle(Paint.Style.FILL);
        paint_buffBlue.setStrokeWidth(5f);

        //red and blue buff text
        paint_buffText.setColor(Color.WHITE);
        paint_buffText.setStyle(Paint.Style.FILL);
        paint_buffText.setFakeBoldText(true);
        paint_buffText.setTextSize(25f);
        paint_buffText.setTextAlign(Paint.Align.CENTER);

        //血条画笔
        paint_blood.setColor(Color.rgb(223, 57, 49));
        paint_blood.setStyle(Paint.Style.STROKE);
        paint_blood.setStrokeWidth(6f);

        //射线画笔
        paint_ray.setColor(Color.WHITE);
        paint_ray.setStyle(Paint.Style.STROKE);
        paint_ray.setStrokeWidth(1.2f);

        //方框画笔
        paint_rect.setColor(Color.GREEN);
//        paint_rect.setColor(Color.WHITE);
        paint_rect.setStyle(Paint.Style.STROKE);
        paint_rect.setStrokeWidth(4f);

        paint_backhome.setColor(Color.GREEN);
        paint_backhome.setStyle(Paint.Style.FILL);

        //技能就绪画笔
        paint_ready.setColor(Color.parseColor("#1afa29"));
        paint_ready.setStyle(Paint.Style.FILL);
        paint_ready.setStrokeWidth(8);

        //技能提示画笔
        paint_hint.setColor(Color.parseColor("#00FFFF"));
        paint_hint.setStyle(Paint.Style.FILL);
        paint_hint.setTextSize(30f);
        paint_hint.setTextAlign(Paint.Align.CENTER);

        //技能CD画笔
        paint_cd.setColor(Color.RED);
        paint_cd.setStyle(Paint.Style.FILL);
        paint_cd.setTextSize(35f);
        Typeface font = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);
        paint_cd.setTypeface(font);
        paint_cd.setTextAlign(Paint.Align.CENTER);

        //技能CD text画笔
        paint_cd_text.setColor(Color.RED);
        paint_cd_text.setTextSize(30);

        paint_cd_big.setColor(Color.YELLOW);
        paint_cd_big.setTextSize(18f);
        paint_cd_big.setTextAlign(Paint.Align.CENTER);

        //死亡斜线画笔
        paint_deadline.setColor(Color.RED);
        paint_deadline.setStyle(Paint.Style.FILL);
        paint_deadline.setStrokeWidth(7f);

        //英雄name画笔
        paint_name.setColor(Color.WHITE);
        paint_name.setStyle(Paint.Style.FILL);
        paint_name.setTextSize(25f);
        paint_name.setTextAlign(Paint.Align.CENTER);

        float x = ScreenUtils.getScreenWidth();
        float y = ScreenUtils.getScreenHeight();
        float center_x, center_y;
        if (x > y) {
            center_x = x / 2f;
            center_y = y / 2f;
        } else {
            center_x = y / 2f;
            center_y = x / 2f;
        }


        while (threadFlag) {
            if (controlFlag && (DataProvider.isConnected || ConfigHelper.mode.equals("file"))) {
                try {
//                    获取画布
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        canvas = surfaceHolder.lockHardwareCanvas();
                    } else {
                        canvas = surfaceHolder.lockCanvas();
                    }
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    List<DrawData> drawDatas = DrawDataHelper.readData();
                    if(drawDatas.isEmpty())
                        continue;
                    if (DrawDataHelper.buff_data.get(0) == -996 && DrawDataHelper.buff_data.get(1) == -996) {
//                        canvas.drawText("等待对局开始中", 300, 150, paint_start);
                    } else {
                        //绘制野怪
                        if (DrawDataHelper.buff_data != null && DrawHelper.draw_buff) {
                            try {
                                int remain_time;
                                int camps;
                                if (ConfigHelper.camp == 1) {
                                    camps = 1;
                                } else {
                                    camps = -1;
                                }

                                for (int q = 0; q < 4; q++) {
                                    remain_time = DrawDataHelper.buff_data.get(q);
                                    if (remain_time != 100 && remain_time != 0) {
                                        canvas.drawText("" + remain_time,
                                                DrawHelper.icon_offset_x + 150 + camps * animalx[q],
                                                DrawHelper.icon_offset_y + 150 + camps * animaly[q],
                                                paint_buffText
                                        );
                                    } else if (remain_time == 100) {
                                        if (q == 0 || q == 2) {
                                            DrawHelper.drawConCircle(canvas, (float) (DrawHelper.icon_offset_x + 150 + camps * animalx[q]),
                                                    (float) DrawHelper.icon_offset_y + 150 + camps * animaly[q], 9, paint_buffBlue, paint_edge);
                                        } else {
                                            DrawHelper.drawConCircle(canvas, (float) (DrawHelper.icon_offset_x + 150 + camps * animalx[q]),
                                                    (float) DrawHelper.icon_offset_y + 150 + camps * animaly[q], 9, paint_buffRed, paint_edge);
                                        }
                                    }
                                }
                            } catch (Exception ignore) {

                            }

                        }
                        int cnt = 0;
                        if (DrawHelper.draw_cd) {
                            //绘制大招cd text
                            canvas.drawText("大招:", 585 + DrawHelper.icon_offset_x, 25, paint_cd_text);
                            //绘制技能cd text
                            canvas.drawText("技能:", 585 + DrawHelper.icon_offset_x, 75 + 25, paint_cd_text);
                        }
                        for (DrawData drawData : drawDatas) {
                            Bitmap mBitmap = IconHelper.getHeroIcon(drawData.HeroID);
                            if (mBitmap == null) continue;
                            //绘制CD

                            if (DrawHelper.draw_cd) {
                                int cd_head_x = 682 + cnt * 60 + DrawHelper.icon_offset_x;
                                int cd_head_y = 28 + 25;
                                //绘制cd头像
                                DrawHelper.drawBitMapByCenter(canvas, mBitmap, cd_head_x, cd_head_y, paint);
                                if (drawData.PercentBlood <= 0) {
                                    canvas.drawLine(cd_head_x - 14, cd_head_y - 14, cd_head_x + 14, cd_head_y + 14, paint_deadline);
                                }
                                //绘制大招冷却时间
                                if (drawData.CdD != 0)
                                    canvas.drawText("" + drawData.CdD, cd_head_x, 5 + 25, paint_cd);
                                else
                                    canvas.drawCircle(cd_head_x, 15, 15, paint_ready);
                                //绘制技能冷却时间
                                if (drawData.CdZ != 0)
                                    canvas.drawText("" + drawData.CdZ, cd_head_x, 80 + 22, paint_cd);
                                else
                                    canvas.drawCircle(cd_head_x, 65 + 25, 15, paint_ready);
                                cnt++;
                            }
                            if (drawData.PercentBlood > 0) {
                                icon_x = drawData.MapX + DrawHelper.icon_offset_x;
                                icon_y = drawData.MapY + DrawHelper.icon_offset_y;
                                if (mBitmap != null) {
                                    //绘制头像
                                    DrawHelper.drawBitMapByCenter(canvas, mBitmap, icon_x, icon_y, paint);
                                    if (drawData.Hc == 1) {
                                        canvas.drawCircle((float) (icon_x + 20 * Math.cos(((2 * 3.1415926) * 0.01 * DataProvider.timmer))),
                                                (float) (icon_y + 20 * Math.sin(((2 * 3.1415926) * 0.01 * DataProvider.timmer))), 7, paint_backhome);
                                        canvas.drawCircle((float) (icon_x + 20 * Math.cos(((2 * 3.1415926) * 0.01 * (DataProvider.timmer - 5)))),
                                                (float) (icon_y + 20 * Math.sin(((2 * 3.1415926) * 0.01 * (DataProvider.timmer - 8)))), 5, paint_backhome);
                                        canvas.drawCircle((float) (icon_x + 20 * Math.cos(((2 * 3.1415926) * 0.01 * (DataProvider.timmer - 9)))),
                                                (float) (icon_y + 20 * Math.sin(((2 * 3.1415926) * 0.01 * (DataProvider.timmer - 15)))), 3, paint_backhome);
                                    }
                                    //设置渐变血条
                                    if (drawData.PercentBlood < 0.6 && drawData.PercentBlood > 0.3) {
                                        paint_ray.setColor(Color.rgb(255, 140, 0));
                                    } else if (drawData.PercentBlood <= 0.3) {
                                        paint_ray.setColor(Color.RED);
                                    } else {
                                        paint_ray.setColor(Color.GREEN);
                                    }
                                    //绘制条形血条
                                    if (!DrawHelper.draw_circle) {
                                        RectF rectF = DrawHelper.getRectByCenter(icon_x, icon_y + 28, 58, 8);
//                                        canvas.drawRoundRect(rectF, 5, 5, paint_bedge);
                                        canvas.drawLine(icon_x - 25, icon_y + 28, icon_x - 25 + 50 * drawData.PercentBlood, icon_y + 28, paint_red);
                                        //绘制圈圈血条
                                    } else {
                                        canvas.drawCircle(icon_x, icon_y, 20, paint_gray);
                                        canvas.drawArc(icon_x - 20, icon_y - 20, icon_x + 20, icon_y + 20, -90, 360 * drawData.PercentBlood, false, paint_blood);
                                    }

                                }

                                ray_x = drawData.RectX + DrawHelper.ray_offset_x;
                                ray_y = drawData.RectY + DrawHelper.ray_offset_y;

                                //绘制射线
                                if (DrawHelper.draw_ray) {
                                    canvas.drawLine(center_x, center_y, ray_x, (int) (ray_y - 0.65 * drawData.HHH), paint_ray);
                                }


                                // 绘制方框
                                if (DrawHelper.draw_rect) {

                                    float ry = (float) (ray_y - 0.65 * drawData.HHH);
                                    int rw = (int) (drawData.HHH * 0.7);
                                    int rh = (int) (drawData.HHH * 1.3);

//                                    // 绘制大地图技能
//                                    if (DrawHelper.draw_head) {
//                                        if (drawData.CdD != 0)
//                                            canvas.drawText("" + drawData.CdD, (float) (ray_x + 0.5 * rw - 15), (float) (ry - 0.5 * rh + 15), paint_cd_big);
//                                        else
//                                            canvas.drawCircle((float) (ray_x + 0.5 * rw - 15), (float) (ry - 0.5 * rh + 15), 7, paint_ready);
//                                        //绘制技能冷却时间
//                                        if (drawData.CdZ != 0)
//                                            canvas.drawText("" + drawData.CdZ, (float) (ray_x + 0.5 * rw - 15), (float) (ry + 0.5 * rh - 15), paint_cd_big);
//                                        else
//                                            canvas.drawCircle((float) (ray_x + 0.5 * rw - 15), (float) (ry + 0.5 * rh - 15), 7, paint_ready);
//                                    }
                                    RectF rectF = DrawHelper.getRectByCenter(ray_x, ry, rw, rh);
                                    canvas.drawRoundRect(rectF, 5, 5, paint_rect);
                                    //绘制英雄名称
//                                    canvas.drawText(IconHelper.getHeroName(drawData.HeroID), ray_x, ry - rh / 1.7f, paint_name);
                                    canvas.drawLine((int) (ray_x - drawData.HHH * 0.35 - 7), (int) (ray_y + 2), (int) (ray_x - drawData.HHH * 0.35 - 7), (int) (ray_y + 2 - (drawData.HHH * 1.3 + 4) * drawData.PercentBlood), paint_blood);

                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                    if (ConfigHelper.mode.equals("file")) {
                        try {
                            new Thread(DataProvider::refreshDrawData).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(50);//毫秒 50 100 185
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}