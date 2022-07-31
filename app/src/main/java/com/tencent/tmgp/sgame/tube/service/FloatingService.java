package com.tencent.tmgp.sgame.tube.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.copy.mytebe.R;
import com.tencent.tmgp.sgame.tube.StubApplication;
import com.tencent.tmgp.sgame.tube.data.DataProvider;
import com.tencent.tmgp.sgame.tube.helper.ConfigHelper;
import com.tencent.tmgp.sgame.tube.helper.DrawHelper;
import com.tencent.tmgp.sgame.tube.utils.FileUtils;
import com.tencent.tmgp.sgame.tube.utils.ScreenUtils;
import com.tencent.tmgp.sgame.tube.view.DrawView;
import com.tencent.tmgp.sgame.tube.view.ISeekBarListener;


public class FloatingService extends Service {

    private Intent surfaceService;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams; //悬浮菜单
    private boolean isShow = false;
    private boolean isStarted = false;
    private View menuView;


    public FloatingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new FloatServiceBinder();
    }


    public class FloatServiceBinder extends Binder {
        public void callShowFloatWindow() {
            showFloatWindow();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initFloatWindow();
        surfaceService = new Intent(getApplication(), SurfaceService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConfigHelper.save();
        DrawHelper.save();
        windowManager.removeView(menuView);
    }

    //初始化悬浮视图
    private void initFloatWindow() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }else{
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SECURE;
        layoutParams.width = ScreenUtils.dp2px(30);
        layoutParams.height = ScreenUtils.dp2px(30);
        layoutParams.x = 100;
        layoutParams.y = 100;
    }

    @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
    private void showFloatWindow() {
        if(isShow)
            return;
        isShow = true;

        //视图绑定
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        menuView = layoutInflater.inflate(R.layout.menu, null);
        windowManager.addView(menuView, layoutParams);

        //控件绑定
        ImageButton imgButton = menuView.findViewById(R.id.imageButton);
        SwitchCompat menu_switch_run = menuView.findViewById(R.id.menu_switch_run);
        CheckBox menu_checkBox_ray = menuView.findViewById(R.id.menu_checkBox_ray);
        CheckBox menu_checkBox_rect = menuView.findViewById(R.id.menu_checkBox_rect);
        CheckBox menu_checkBox_buff = menuView.findViewById(R.id.menu_checkBox_buff);
        CheckBox menu_checkBox_cd = menuView.findViewById(R.id.menu_checkBox_cd);
        SeekBar menu_seekBar1 = menuView.findViewById(R.id.menu_seekBar);
        SeekBar menu_seekBar3 = menuView.findViewById(R.id.menu_seekBar3);
        Button menu_btn_exit = menuView.findViewById(R.id.menu_btn_exit);


        menu_btn_exit.setOnClickListener(view -> System.exit(0));




        //一键开启
        menu_switch_run.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isStarted = isChecked;
            if (isStarted) {
                if(ConfigHelper.mode.equals("socket")) {
                    DataProvider.initServer();
                    Toast.makeText(StubApplication.getContext(),"正在启动，请稍等...",Toast.LENGTH_LONG).show();
                }
                DataProvider.Timmer();
                DataProvider.initLocal(); //开启二进制
                startService(surfaceService);
                DrawView.controlFlag = true;
            } else {
                DataProvider.closeLocal();
                stopService(surfaceService);
                DrawView.controlFlag = false;
            }
        });


        //初始化checkbox seekBar RadioButton 默认选中状态
        menu_checkBox_ray.setChecked(DrawHelper.draw_ray);
        menu_checkBox_rect.setChecked(DrawHelper.draw_rect);
        menu_checkBox_buff.setChecked(DrawHelper.draw_buff);
        menu_checkBox_cd.setChecked(DrawHelper.draw_cd);

        menu_seekBar1.setProgress(DrawHelper.icon_offset_x + 200);
        menu_seekBar3.setProgress(DrawHelper.ray_offset_x + 400);



        //checkBox 监听
        menu_checkBox_ray.setOnCheckedChangeListener((buttonView, isChecked) -> DrawHelper.draw_ray = isChecked);
        menu_checkBox_rect.setOnCheckedChangeListener((buttonView, isChecked) -> DrawHelper.draw_rect = isChecked);
        menu_checkBox_buff.setOnCheckedChangeListener((buttonView, isChecked) -> DrawHelper.draw_buff = isChecked);
        menu_checkBox_cd.setOnCheckedChangeListener((buttonView, isChecked) -> DrawHelper.draw_cd = isChecked);

        menu_seekBar1.setOnSeekBarChangeListener(new ISeekBarListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                super.onProgressChanged(seekBar, i, b);
                DrawHelper.icon_offset_x = i-250;
            }
        });



        menu_seekBar3.setOnSeekBarChangeListener(new ISeekBarListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                super.onProgressChanged(seekBar, i, b);
                DrawHelper.ray_offset_x = i-400;
            }
        });

        //悬浮菜单
        imgButton.setOnTouchListener(new View.OnTouchListener() {
            private int mTouchStartX, mTouchStartY;//手指按下时坐标
            private boolean isMove = false;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://单击
                        isMove = false;
                        mTouchStartX = (int) event.getRawX();
                        mTouchStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://拖动
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int movedX = nowX - mTouchStartX;
                        int movedY = nowY - mTouchStartY;
                        if (movedX > 5 || movedY > 5) {
                            isMove = true;
                        }
                        mTouchStartX = nowX;
                        mTouchStartY = nowY;
                        layoutParams.x += movedX;
                        layoutParams.y += movedY;
                        windowManager.updateViewLayout(menuView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP://抬起
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return isMove;
            }
        });
        imgButton.setOnClickListener(new View.OnClickListener() {
            private boolean On = true;

            @Override
            public void onClick(View v) {
                if (!On) {
                    On = true;
                    layoutParams.width = ScreenUtils.dp2px(360);
                    layoutParams.height = ScreenUtils.dp2px(280);
                } else {
                    On = false;
                    layoutParams.width = ScreenUtils.dp2px(30);
                    layoutParams.height = ScreenUtils.dp2px(30);
                    //保存配置
                    DrawHelper.save();
                    ConfigHelper.save();
                }
                windowManager.updateViewLayout(menuView, layoutParams);
            }
        });

    }
}
