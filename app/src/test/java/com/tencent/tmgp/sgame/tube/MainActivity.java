package com.tencent.tmgp.sgame.tube;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tmgp.sgame.tube.data.DataProvider;
import com.tencent.tmgp.sgame.tube.helper.ApkHelper;
import com.tencent.tmgp.sgame.tube.helper.ConfigHelper;
import com.tencent.tmgp.sgame.tube.service.BackgroundService;
import com.tencent.tmgp.sgame.tube.service.FloatingService;
import com.tencent.tmgp.sgame.tube.utils.AppUtils;
import com.tencent.tmgp.sgame.tube.utils.FileUtils;
import com.tencent.tmgp.sgame.tube.utils.HttpUtils;
import com.tencent.tmgp.sgame.tube.utils.RandomUtils;
import com.tencent.tmgp.sgame.tube.utils.ShellUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.System.exit;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_log)
    Button btnLog;
    @BindView(R.id.et_log)
    EditText etLog;
    @BindView(R.id.btn_machine)
    Button btnMachine;
    private boolean isBind = false;
    private ServiceConnection floatConnection;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //获取root权限
        AppUtils.getRoot();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //随机包名
//        if (getPackageName().equals(ConfigHelper.old_pg_name)) {
//            new Thread(() -> {
//                ApkHelper apkHelper = new ApkHelper();
//                apkHelper.installSignedApk(MainActivity.this);
//                ShellUtils.execCommand("pm uninstall " + ConfigHelper.old_pg_name, true);
//            }).start();
//            Toast.makeText(this, "正在生成随机包名...", Toast.LENGTH_LONG).show();
//        }

        //初始化
        etLog.setText(ConfigHelper.card);
        startService(new Intent(getApplicationContext(), BackgroundService.class)); //下载头像包+二进制复制
        RandomUtils.makeRandomIcon(RandomUtils.getRandomFloatName(), FileUtils.getFilesPath() + "/mini.png"); //生成随机悬浮图标


        //申请悬浮窗
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            //若没有权限，提示获取.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        mHandler = new Handler();
        mHandler.postDelayed(new Thread(() -> {
            //获取公告
            String data = HttpUtils.sendPost(ConfigHelper.url + ConfigHelper.api_bulletin, "");
            ConfigHelper.bulletin = data;
            showDialog("公告", data);
        }), 100);


    }

    //登陆
    private void doLogin() {
        ConfigHelper.device_id= AppUtils.getDeviceId();
//        CrashReport.testJavaCrash();
        mHandler.postDelayed(new Thread(() -> {
            String data = HttpUtils.sendPost(ConfigHelper.url + ConfigHelper.api_singlecode, "SingleCode=" + ConfigHelper.card + "&Ver=" + ConfigHelper.version + "&Mac=" + ConfigHelper.device_id);
            //返回32位状态码登陆成功
            if (data.length() == 32) {
                ConfigHelper.deadline = HttpUtils.sendPost(ConfigHelper.url + ConfigHelper.api_deadline, "UserName=" + ConfigHelper.card);
                ConfigHelper.logged = true;
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                //存储登陆信息
                ConfigHelper.save();
                //开启悬浮窗
                floatServiceBind();
            } else if (data.equals("-402"))
                Toast.makeText(getApplicationContext(), "机器码错误，请点击下方解除绑定", Toast.LENGTH_SHORT).show();
                //登陆失败
            else {
                Toast.makeText(getApplicationContext(), "卡密到期或错误" + data, Toast.LENGTH_SHORT).show();
            }
        }), 100);
    }


    private void doUnbind() {
        String singleCode = etLog.getText().toString().trim();
        String data = HttpUtils.sendPost(ConfigHelper.url + ConfigHelper.api_unbind, "UserName=" + singleCode + "&Mac=" + ConfigHelper.device_id);

        if (data.equals("1"))
            showDialog("转绑卡密", "转绑成功，扣除时间");
        else if (data.equals("-21"))
            showDialog("转绑卡密", "机器码一样,无需转绑.");
        else
            showDialog("转绑卡密", data);
    }

    public void floatServiceBind() {
        if (!isBind) {
            Intent floatService = new Intent(this, FloatingService.class);
            floatConnection = new FloatServiceConnection();
            bindService(floatService, floatConnection, BIND_AUTO_CREATE);
            isBind = true;
        }
    }

    public void floatServiceUnBind() {
        if (isBind) {
            unbindService(floatConnection);
            isBind = false;
        }
    }

    //弹窗
    private void showDialog(final String title, final String data) {
        runOnUiThread(() -> {
            if (MainActivity.this.isFinishing())
                return;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(title);
            builder.setMessage(data);
            builder.setCancelable(false);
            builder.setPositiveButton("确定", null);
            builder.show();
        });
    }

    private void checkUpdate() {
        //强制更新
        String data = HttpUtils.sendPost(ConfigHelper.url + ConfigHelper.api_version, "Ver=" + ConfigHelper.version);
        if (!data.equals("1")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("检测到更新");
            builder.setMessage("请更新最新版本");
            builder.setCancelable(false);
            builder.setNegativeButton("官网", (p1, p2) -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConfigHelper.url_home)));
                exit(0);
            });
            builder.show();
        }
    }

    @OnClick({R.id.btn_log, R.id.btn_machine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log:
                ConfigHelper.card = etLog.getText().toString().trim();
                ConfigHelper.save();
                doLogin();
                checkUpdate();
                break;
            case R.id.btn_machine:
                doUnbind();
                break;
        }
    }

    public static class FloatServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FloatingService.FloatServiceBinder service = (FloatingService.FloatServiceBinder) iBinder;
            service.callShowFloatWindow();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }
}
